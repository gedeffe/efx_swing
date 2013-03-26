package at.bestsolution.efxclipse.runtime.workbench.renderers.swing;

import javax.inject.Inject;

import org.eclipse.e4.core.contexts.IEclipseContext;

import at.bestsolution.efxclipse.runtime.workbench.renderers.base.BaseAreaRenderer;
import at.bestsolution.efxclipse.runtime.workbench.renderers.base.BaseMenuBarRenderer;
import at.bestsolution.efxclipse.runtime.workbench.renderers.base.BaseMenuItemRenderer;
import at.bestsolution.efxclipse.runtime.workbench.renderers.base.BaseMenuRenderer;
import at.bestsolution.efxclipse.runtime.workbench.renderers.base.BaseMenuSeparatorRenderer;
import at.bestsolution.efxclipse.runtime.workbench.renderers.base.BasePartMenuRenderer;
import at.bestsolution.efxclipse.runtime.workbench.renderers.base.BasePartRenderer;
import at.bestsolution.efxclipse.runtime.workbench.renderers.base.BasePerspectiveRenderer;
import at.bestsolution.efxclipse.runtime.workbench.renderers.base.BasePerspectiveStackRenderer;
import at.bestsolution.efxclipse.runtime.workbench.renderers.base.BasePlaceholderRenderer;
import at.bestsolution.efxclipse.runtime.workbench.renderers.base.BasePopupMenuRenderer;
import at.bestsolution.efxclipse.runtime.workbench.renderers.base.BaseSashRenderer;
import at.bestsolution.efxclipse.runtime.workbench.renderers.base.BaseStackRenderer;
import at.bestsolution.efxclipse.runtime.workbench.renderers.base.BaseToolBarRenderer;
import at.bestsolution.efxclipse.runtime.workbench.renderers.base.BaseToolBarSeparatorRenderer;
import at.bestsolution.efxclipse.runtime.workbench.renderers.base.BaseToolControlRenderer;
import at.bestsolution.efxclipse.runtime.workbench.renderers.base.BaseToolItemRenderer;
import at.bestsolution.efxclipse.runtime.workbench.renderers.base.BaseTrimBarRenderer;
import at.bestsolution.efxclipse.runtime.workbench.renderers.base.BaseWindowRenderer;
import at.bestsolution.efxclipse.runtime.workbench.renderers.base.BaseWorkbenchRendererFactory;

@SuppressWarnings("restriction")
public class DefWorkbenchRendererFactory extends BaseWorkbenchRendererFactory {

	@Inject
	public DefWorkbenchRendererFactory(IEclipseContext context) {
		super(context);
	}

	@Override
	protected Class<? extends BaseWindowRenderer<?>> getWindowRendererClass() {
		return DefWindowRenderer.class;
	}

	@Override
	protected Class<? extends BaseSashRenderer<?>> getSashRendererClass() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Class<? extends BaseMenuBarRenderer<?>> getMenuBarRendererClass() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Class<? extends BaseTrimBarRenderer<?>> getTrimBarRendererClass() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Class<? extends BaseToolBarRenderer<?>> getToolBarRendererClass() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Class<? extends BaseToolItemRenderer<?>> getToolItemRendererClass() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Class<? extends BaseStackRenderer<?, ?, ?>> getStackRendererClass() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Class<? extends BasePartRenderer<?, ?, ?>> getPartRendererClass() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Class<? extends BaseMenuRenderer<?>> getMenuRendererClass() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Class<? extends BaseMenuItemRenderer<?>> getMenuItemRendererClass() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Class<? extends BaseMenuSeparatorRenderer<?>> getMenuSeparatorRendererClass() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Class<? extends BaseMenuRenderer<?>> getToolItemMenuRendererClass() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Class<? extends BasePerspectiveStackRenderer<?, ?, ?>> getPerspectiveStackRendererClass() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Class<? extends BasePerspectiveRenderer<?>> getPerspectiveRendererClass() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Class<? extends BasePlaceholderRenderer<?>> getPlaceholderRendererClass() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Class<? extends BaseToolControlRenderer<?>> getToolcontrolRendererClass() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Class<? extends BaseToolBarSeparatorRenderer<?>> getToolBarSeparatorRendererClass() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Class<? extends BaseAreaRenderer<?>> getAreaRendererClass() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Class<? extends BasePopupMenuRenderer<?>> getPopupMenuRendererClass() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Class<? extends BasePartMenuRenderer<?>> getPartMenuRenderer() {
		// TODO Auto-generated method stub
		return null;
	}

}
