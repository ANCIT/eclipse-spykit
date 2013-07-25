package org.eclipse.spykit.ui.views;

import org.eclipse.core.runtime.internal.stats.BundleStats;
import org.eclipse.core.runtime.internal.stats.StatsManager;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.spykit.ui.Activator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.part.ViewPart;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;

public class StartupSummaryView extends ViewPart implements BundleListener {

	public static final String ID = "org.eclipse.spykit.ui.views.StartupSummaryView"; //$NON-NLS-1$
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private Label lblNumberOfPlugins;
	private Label lblTotalTimeTaken;
	private Label lblMaxTimeIs;

	public StartupSummaryView() {
	}

	/**
	 * Create contents of the view part.
	 * @param parent
	 */
	@Override
	public void createPartControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new FillLayout(SWT.HORIZONTAL));
		{
			Section sctnSummary = formToolkit.createSection(container, Section.TITLE_BAR);
			formToolkit.paintBordersFor(sctnSummary);
			sctnSummary.setText("Startup Summary");
			{
				Composite composite = formToolkit.createComposite(sctnSummary, SWT.NONE);
				formToolkit.paintBordersFor(composite);
				sctnSummary.setClient(composite);
				composite.setLayout(new RowLayout(SWT.VERTICAL));
				{
					lblNumberOfPlugins = new Label(composite, SWT.NONE);
					formToolkit.adapt(lblNumberOfPlugins, true, true);
					BundleStats[] bundles = StatsManager.getDefault().getBundles();
					lblNumberOfPlugins.setText("Number of Plugins Loaded : "+bundles.length);
				}
				{
					lblTotalTimeTaken = new Label(composite, SWT.NONE);
					formToolkit.adapt(lblTotalTimeTaken, true, true);
					lblTotalTimeTaken.setText("Total Time taken for Startup :");
				}
				{
					lblMaxTimeIs = new Label(composite, SWT.NONE);
					formToolkit.adapt(lblMaxTimeIs, true, true);
					lblMaxTimeIs.setText("Max Time is taken by ");
				}
			}
		}
		
		 Activator.getDefault().getBundle().getBundleContext().addBundleListener(this);
		
		

		createActions();
		initializeToolBar();
		initializeMenu();
	}
	
	private void refresh() {
		BundleStats[] bundles = StatsManager.getDefault().getBundles();
		lblNumberOfPlugins.setText("Number of Plugins Loaded : "+bundles.length);
		
		long totalTimeTaken = 0;
		BundleStats maxBundleStats = bundles[0];
		for (BundleStats bundleStats : bundles) {
			if(bundleStats.getStartupTime() > maxBundleStats.getStartupTime()) {
				maxBundleStats = bundleStats;
			}
			totalTimeTaken += bundleStats.getStartupTime();
		}
		
		lblTotalTimeTaken.setText("Total Time taken for Startup :"+totalTimeTaken);
		lblMaxTimeIs.setText("Max Time of "+maxBundleStats.getStartupTime()+" is taken by "+maxBundleStats.getSymbolicName());
		lblNumberOfPlugins.pack();
		lblTotalTimeTaken.pack();
		lblMaxTimeIs.pack();
		
	}

	/**
	 * Create the actions.
	 */
	private void createActions() {
		// Create the actions
	}

	/**
	 * Initialize the toolbar.
	 */
	private void initializeToolBar() {
		IToolBarManager toolbarManager = getViewSite().getActionBars()
				.getToolBarManager();
	}

	/**
	 * Initialize the menu.
	 */
	private void initializeMenu() {
		IMenuManager menuManager = getViewSite().getActionBars()
				.getMenuManager();
	}

	@Override
	public void setFocus() {
		// Set the focus
	}
	
	@Override
	public void bundleChanged(BundleEvent event) {
		if(event.getType() == BundleEvent.STARTED){
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				refresh();
			}

			
		});
		}
				
	}
	
	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		super.dispose();
		 Activator.getDefault().getBundle().getBundleContext().removeBundleListener(this);
	}

}
