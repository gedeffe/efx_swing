package at.bestsolution.efxclipse.runtime.workbench.renderers.swing;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.swing.JFrame;

import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MTrimBar;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.model.application.ui.basic.MWindowElement;
import org.eclipse.e4.ui.model.application.ui.menu.MMenu;
import org.eclipse.e4.ui.workbench.UIEvents;
import org.eclipse.e4.ui.workbench.modeling.ISaveHandler.Save;

import at.bestsolution.efxclipse.runtime.workbench.renderers.base.BaseRenderer;
import at.bestsolution.efxclipse.runtime.workbench.renderers.base.BaseWindowRenderer;
import at.bestsolution.efxclipse.runtime.workbench.renderers.base.widget.WCallback;
import at.bestsolution.efxclipse.runtime.workbench.renderers.base.widget.WLayoutedWidget;
import at.bestsolution.efxclipse.runtime.workbench.renderers.base.widget.WPropertyChangeHandler;
import at.bestsolution.efxclipse.runtime.workbench.renderers.base.widget.WWidget;
import at.bestsolution.efxclipse.runtime.workbench.renderers.base.widget.WWindow;

@SuppressWarnings("restriction")
public class DefWindowRenderer extends BaseWindowRenderer<JFrame> {

	@Override
	protected Save[] promptToSave(MWindow element,
			Collection<MPart> dirtyParts, WWindow<JFrame> widget) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Save promptToSave(MWindow element, MPart dirtyPart,
			WWindow<JFrame> widget) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Class<? extends WWindow<JFrame>> getWidgetClass(MWindow element) {
		return WindowImpl.class;
	}

	static class WindowImpl implements WWindow<JFrame> {
		private JFrame frame;
		private MWindow window;
		
		@Inject
		public WindowImpl(@Named(BaseRenderer.CONTEXT_DOM_ELEMENT) MWindow mWindow) {
			this.window = mWindow;
		}
		
		@Override
		public void setWidgetState(
				at.bestsolution.efxclipse.runtime.workbench.renderers.base.widget.WWidget.WidgetState state) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public at.bestsolution.efxclipse.runtime.workbench.renderers.base.widget.WWidget.WidgetState getWidgetState() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void setPropertyChangeHandler(
				WPropertyChangeHandler<? extends WWidget<MWindow>> handler) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void setDomElement(MWindow domElement) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public MWindow getDomElement() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void addStyleClasses(List<String> classnames) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void addStyleClasses(String... classnames) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void setStyleId(String id) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public JFrame getWidget() {
			if( frame == null ) {
				frame = new JFrame();
				frame.setBounds(window.getX(), window.getY(), window.getWidth(), window.getHeight());
			}
			return frame;
		}

		@Override
		public void deactivate() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void activate() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public boolean isActive() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void registerActivationCallback(WCallback<Boolean, Void> callback) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void setMainMenu(WLayoutedWidget<MMenu> menuWidget) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void setTopTrim(WLayoutedWidget<MTrimBar> trimBar) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void setLeftTrim(WLayoutedWidget<MTrimBar> trimBar) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void setRightTrim(WLayoutedWidget<MTrimBar> trimBar) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void setBottomTrim(WLayoutedWidget<MTrimBar> trimBar) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void addChild(WLayoutedWidget<MWindowElement> widget) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void addChild(int idx, WLayoutedWidget<MWindowElement> widget) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void removeChild(WLayoutedWidget<MWindowElement> widget) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void show() {
			getWidget().setVisible(true);
		}

		@Override
		public void close() {
			// TODO Auto-generated method stub
			
		}
		
		@Inject
		void setTitle(@Named(UIEvents.UILabel.LABEL) String title) {
			getWidget().setTitle(title);
		}
		
		@Inject
		void setX(@Named(UIEvents.Window.X) Integer x) {
//			getWidget().setBounds(x, 0, width, height)
		}
	}
}
