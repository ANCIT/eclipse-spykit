package org.eclipse.spykit.ui.perspectives;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class EclipseSpykitPerspective implements IPerspectiveFactory {

	/**
	 * Creates the initial layout for a page.
	 */
	public void createInitialLayout(IPageLayout layout) {
		String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(false);
		
		addFastViews(layout);
		addViewShortcuts(layout);
		addPerspectiveShortcuts(layout);
		layout.addView("org.eclipse.spykit.ui.views.StartupSummaryView", IPageLayout.LEFT, 0.31f, IPageLayout.ID_EDITOR_AREA);
		layout.addView("org.eclipse.spykit.ui.views.StartupTypeView", IPageLayout.BOTTOM, 0.18f, "org.eclipse.spykit.ui.views.StartupSummaryView");
		layout.addView("org.eclipse.spykit.ui.views.PluginLoadedByView", IPageLayout.BOTTOM, 0.48f, "org.eclipse.spykit.ui.views.StartupTypeView");
		
		
		layout.addView("org.eclipse.spykit.runtime.views.LoadedClassesView", IPageLayout.BOTTOM, 0.5f, IPageLayout.ID_EDITOR_AREA);
		layout.addView("org.eclipse.spykit.runtime.views.ActivePluginsView", IPageLayout.TOP, 0.5f, IPageLayout.ID_EDITOR_AREA);
		
	}

	/**
	 * Add fast views to the perspective.
	 */
	private void addFastViews(IPageLayout layout) {
	}

	/**
	 * Add view shortcuts to the perspective.
	 */
	private void addViewShortcuts(IPageLayout layout) {
	}

	/**
	 * Add perspective shortcuts to the perspective.
	 */
	private void addPerspectiveShortcuts(IPageLayout layout) {
	}

}
