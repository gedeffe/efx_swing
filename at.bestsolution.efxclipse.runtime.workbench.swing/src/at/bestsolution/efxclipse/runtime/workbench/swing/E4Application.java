package at.bestsolution.efxclipse.runtime.workbench.swing;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RunnableFuture;

import javafx.scene.image.Image;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.RegistryFactory;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.EclipseContextFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.internal.services.EclipseAdapter;
import org.eclipse.e4.core.services.adapter.Adapter;
import org.eclipse.e4.core.services.contributions.IContributionFactory;
import org.eclipse.e4.core.services.log.ILoggerProvider;
import org.eclipse.e4.core.services.log.Logger;
import org.eclipse.e4.core.services.translation.TranslationProviderFactory;
import org.eclipse.e4.core.services.translation.TranslationService;
import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.e4.ui.internal.workbench.ActiveChildLookupFunction;
import org.eclipse.e4.ui.internal.workbench.ActivePartLookupFunction;
import org.eclipse.e4.ui.internal.workbench.E4Workbench;
import org.eclipse.e4.ui.internal.workbench.ExceptionHandler;
import org.eclipse.e4.ui.internal.workbench.ModelServiceImpl;
import org.eclipse.e4.ui.internal.workbench.PlaceholderResolver;
import org.eclipse.e4.ui.internal.workbench.ReflectionContributionFactory;
import org.eclipse.e4.ui.internal.workbench.ResourceHandler;
import org.eclipse.e4.ui.model.application.MAddon;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.IExceptionHandler;
import org.eclipse.e4.ui.workbench.IModelResourceHandler;
import org.eclipse.e4.ui.workbench.IResourceUtilities;
import org.eclipse.e4.ui.workbench.lifecycle.PostContextCreate;
import org.eclipse.e4.ui.workbench.lifecycle.ProcessAdditions;
import org.eclipse.e4.ui.workbench.lifecycle.ProcessRemovals;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPlaceholderResolver;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.osgi.service.datalocation.Location;

import at.bestsolution.efxclipse.runtime.osgi.util.LoggerCreator;
import at.bestsolution.efxclipse.runtime.workbench.swing.internal.LoggerProviderImpl;
import at.bestsolution.efxclipse.runtime.workbench.swing.internal.WorkbenchSwingActivator;

@SuppressWarnings("restriction")
public class E4Application implements IApplication {

	public static final String METADATA_FOLDER = ".metadata"; //$NON-NLS-1$
	private static final String VERSION_FILENAME = "version.ini"; //$NON-NLS-1$
	private static final String WORKSPACE_VERSION_KEY = "org.eclipse.core.runtime"; //$NON-NLS-1$
	private static final String WORKSPACE_VERSION_VALUE = "2"; //$NON-NLS-1$
	private static final String EXIT_CODE = "e4.osgi.exit.code";
	
	private String[] args;
	private Object lcManager;
	private E4Workbench workbench = null;
	private IModelResourceHandler handler;

	private IEclipseContext workbenchContext;

	private Location instanceLocation;

	at.bestsolution.efxclipse.runtime.core.log.Logger logger = LoggerCreator.createLogger(getClass());

	@Override
	public Object start(IApplicationContext applicationContext) throws Exception {
		workbench = createE4Workbench(applicationContext);
		workbenchContext = workbench.getContext();

		// Create and run the UI (if any)
		workbench.createAndRunUI(workbench.getApplication());
		
		// TODO Auto-generated method stub
		return IApplication.EXIT_OK;
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
	}

	
	
	public E4Workbench createE4Workbench(IApplicationContext applicationContext) {
		args = (String[]) applicationContext.getArguments().get(IApplicationContext.APPLICATION_ARGS);

		IEclipseContext appContext = createDefaultContext();
		
		//FIXME We need to fix this later on see ticket 256
//		ContextInjectionFactory.setDefault(appContext);
		try {
			Method m = ContextInjectionFactory.class.getMethod("setDefault", IEclipseContext.class);
			m.invoke(null, appContext);
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
			System.err.println("WARNING: You are running on an old and buggy DI container which is fixed in 4.2.2 builds. Consider upgradeing.");
		}
		

		
		appContext.set(IApplication.class, this);
		appContext.set(UISynchronize.class, new UISynchronize() {

			public void syncExec(final Runnable runnable) {
				if (javafx.application.Platform.isFxApplicationThread()) {
					runnable.run();
				} else {
					RunnableFuture<?> task = new FutureTask<Void>(runnable, null);
					
					javafx.application.Platform.runLater(task);

					try {
						// wait for task to complete
						task.get();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ExecutionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} finally {
						task.cancel(true);
					}
				}
			}

			public void asyncExec(Runnable runnable) {
				javafx.application.Platform.runLater(runnable);
			}
		});
		appContext.set(IApplicationContext.class, applicationContext);
		appContext.set(IResourceUtilities.class, new IResourceUtilities<Image>() {
			private WeakHashMap<URI, WeakReference<Image>> imageCache = new WeakHashMap<URI, WeakReference<Image>>();
			
			public Image imageDescriptorFromURI(URI iconPath) {
				WeakReference<Image> r = imageCache.get(iconPath);
				Image img = null;
				if( r != null ) {
					img = r.get();
				}
				
				if( img == null ) {
					try {
						InputStream in = new URL(iconPath.toString()).openStream();
						img = new Image(in);
						in.close();
						imageCache.put(iconPath, new WeakReference<Image>(img));
					} catch (MalformedURLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						logger.warning("could not find icon at: " + iconPath,e);
					}
				}
				
				return img;
			}
		});

		// Check if DS is running
		if (!appContext.containsKey("org.eclipse.e4.ui.workbench.modeling.EModelService")) {
			throw new IllegalStateException("Core services not available. Please make sure that a declarative service implementation (such as the bundle 'org.eclipse.equinox.ds') is available!");
		}

		// Get the factory to create DI instances with
		IContributionFactory factory = (IContributionFactory) appContext.get(IContributionFactory.class.getName());

		// Install the life-cycle manager for this session if there's one
		// defined
		String lifeCycleURI = getArgValue(E4Workbench.LIFE_CYCLE_URI_ARG, applicationContext, false);
		if (lifeCycleURI != null) {
			lcManager = factory.create(lifeCycleURI, appContext);
			if (lcManager != null) {
				// Let the manager manipulate the appContext if desired
				Boolean rv = (Boolean) ContextInjectionFactory.invoke(lcManager, PostContextCreate.class, appContext, Boolean.TRUE);
				if( rv != null && ! rv.booleanValue() ) {
					return null;
				}
			}
		}
		// Create the app model and its context
		MApplication appModel = loadApplicationModel(applicationContext, appContext);
		appModel.setContext(appContext);

		// Set the app's context after adding itself
		appContext.set(MApplication.class.getName(), appModel);

		// let the life cycle manager add to the model
		if (lcManager != null) {
			ContextInjectionFactory.invoke(lcManager, ProcessAdditions.class, appContext, null);
			ContextInjectionFactory.invoke(lcManager, ProcessRemovals.class, appContext, null);
		}

		// Create the addons
		IEclipseContext addonStaticContext = EclipseContextFactory.create();
		for (MAddon addon : appModel.getAddons()) {
			addonStaticContext.set(MAddon.class, addon);
			Object obj = factory.create(addon.getContributionURI(), appContext, addonStaticContext);
			addon.setObject(obj);
		}

		// Parse out parameters from both the command line and/or the product
		// definition (if any) and put them in the context
		String xmiURI = getArgValue(E4Workbench.XMI_URI_ARG, applicationContext, false);
		appContext.set(E4Workbench.XMI_URI_ARG, xmiURI);

//		String themeId = getArgValue(E4Application.THEME_ID, applicationContext, false);
//		appContext.set(E4Application.THEME_ID, themeId);
		appContext.set(E4Workbench.RENDERER_FACTORY_URI, getArgValue(E4Workbench.RENDERER_FACTORY_URI, applicationContext, false));

		// This is a default arg, if missing we use the default rendering engine
		String presentationURI = getArgValue(E4Workbench.PRESENTATION_URI_ARG, applicationContext, false);
		if (presentationURI == null) {
			presentationURI = PartRenderingEngine.engineURI;
		}
		appContext.set(E4Workbench.PRESENTATION_URI_ARG, presentationURI);

		// Instantiate the Workbench (which is responsible for
		// 'running' the UI (if any)...
		return workbench = new E4Workbench(appModel, appContext);
	}
	
	public static IEclipseContext createDefaultContext() {
		IEclipseContext serviceContext = E4Workbench.getServiceContext();
		final IEclipseContext appContext = serviceContext.createChild("WorkbenchContext");
		IExtensionRegistry registry = RegistryFactory.getRegistry();
		ExceptionHandler exceptionHandler = new ExceptionHandler();
		ReflectionContributionFactory contributionFactory = new ReflectionContributionFactory(registry);
		appContext.set(IContributionFactory.class.getName(), contributionFactory);

		// No default log provider available
		if (appContext.get(ILoggerProvider.class) == null) {
			serviceContext.set(ILoggerProvider.class, ContextInjectionFactory.make(LoggerProviderImpl.class, serviceContext));
		}
		
		appContext.set(Logger.class.getName(), serviceContext.get(ILoggerProvider.class).getClassLogger(E4Workbench.class));

		appContext.set(EModelService.class, new ModelServiceImpl(appContext));
		appContext.set(EPlaceholderResolver.class, new PlaceholderResolver());

		// translation
		String locale = Locale.getDefault().toString();
		appContext.set(TranslationService.LOCALE, locale);
		TranslationService bundleTranslationProvider = TranslationProviderFactory.bundleTranslationService(appContext);
		appContext.set(TranslationService.class, bundleTranslationProvider);

		appContext.set(Adapter.class.getName(), ContextInjectionFactory.make(EclipseAdapter.class, appContext));


		appContext.set(IServiceConstants.ACTIVE_PART, new ActivePartLookupFunction());
		appContext.set(IExceptionHandler.class.getName(), exceptionHandler);
		appContext.set(IExtensionRegistry.class.getName(), registry);

		appContext.set(IServiceConstants.ACTIVE_SHELL, new ActiveChildLookupFunction(IServiceConstants.ACTIVE_SHELL, E4Workbench.LOCAL_ACTIVE_SHELL));

		appContext.set(IExtensionRegistry.class.getName(), registry);
		appContext.set(IContributionFactory.class.getName(), contributionFactory);

		return appContext;
	}
	
	private String getArgValue(String argName, IApplicationContext appContext, boolean singledCmdArgValue) {
		// Is it in the arg list ?
		if (argName == null || argName.length() == 0)
			return null;

		if (singledCmdArgValue) {
			for (int i = 0; i < args.length; i++) {
				if (("-" + argName).equals(args[i]))
					return "true";
			}
		} else {
			for (int i = 0; i < args.length; i++) {
				if (("-" + argName).equals(args[i]) && i + 1 < args.length)
					return args[i + 1];
			}
		}

		final String brandingProperty = appContext.getBrandingProperty(argName);
		return brandingProperty == null ? System.getProperty(argName) : brandingProperty;
	}
	
	private MApplication loadApplicationModel(IApplicationContext appContext, IEclipseContext eclipseContext) {
		MApplication theApp = null;

		Location instanceLocation = WorkbenchSwingActivator.getDefault().getInstanceLocation();

		String appModelPath = getArgValue(E4Workbench.XMI_URI_ARG, appContext, false);
		Assert.isNotNull(appModelPath, E4Workbench.XMI_URI_ARG + " argument missing"); //$NON-NLS-1$
		final URI initialWorkbenchDefinitionInstance = URI.createPlatformPluginURI(appModelPath, true);

		eclipseContext.set(E4Workbench.INITIAL_WORKBENCH_MODEL_URI, initialWorkbenchDefinitionInstance);
		eclipseContext.set(E4Workbench.INSTANCE_LOCATION, instanceLocation);

		// Save and restore
		boolean saveAndRestore;
		String value = getArgValue(E4Workbench.PERSIST_STATE, appContext, false);

		saveAndRestore = value == null || Boolean.parseBoolean(value);

		eclipseContext.set(E4Workbench.PERSIST_STATE, Boolean.valueOf(saveAndRestore));

		// Persisted state
		boolean clearPersistedState;
		value = getArgValue(E4Workbench.CLEAR_PERSISTED_STATE, appContext, true);
		clearPersistedState = value != null && Boolean.parseBoolean(value);
		eclipseContext.set(E4Workbench.CLEAR_PERSISTED_STATE, Boolean.valueOf(clearPersistedState));

		// Delta save and restore
		boolean deltaRestore;
		value = getArgValue(E4Workbench.DELTA_RESTORE, appContext, false);
		deltaRestore = value == null || Boolean.parseBoolean(value);
		eclipseContext.set(E4Workbench.DELTA_RESTORE, Boolean.valueOf(deltaRestore));

		String resourceHandler = getArgValue(E4Workbench.MODEL_RESOURCE_HANDLER, appContext, false);

		if (resourceHandler == null) {
			resourceHandler = "bundleclass://org.eclipse.e4.ui.workbench/" + ResourceHandler.class.getName();
		}

		IContributionFactory factory = eclipseContext.get(IContributionFactory.class);

		handler = (IModelResourceHandler) factory.create(resourceHandler, eclipseContext);

		Resource resource = handler.loadMostRecentModel();
		theApp = (MApplication) resource.getContents().get(0);

		return theApp;
	}
}
