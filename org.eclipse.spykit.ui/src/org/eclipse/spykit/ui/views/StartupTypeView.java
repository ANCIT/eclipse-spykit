package org.eclipse.spykit.ui.views;

import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.internal.stats.BundleStats;
import org.eclipse.core.runtime.internal.stats.StatsManager;
import org.eclipse.jface.action.Action;
import org.eclipse.spykit.ui.Activator;
import org.eclipse.spykit.ui.dialogs.StartupAnalysisDetailsDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.part.ViewPart;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.experimental.chart.swt.ChartComposite;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;

public class StartupTypeView extends ViewPart implements BundleListener {

	private final FormToolkit toolkit = new FormToolkit(Display.getCurrent());
	private ChartComposite chartComposite1;
	private ArrayList<BundleStats> startupBundles;
	private ArrayList<BundleStats> activatedBundles;
	private ArrayList<BundleStats> indirectActivatedBundles;
	private Action openStartupTypeDialog;
	public StartupTypeView() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void createPartControl(Composite parent) {
		ScrolledForm sForm = toolkit.createScrolledForm(parent);
		
		Composite container = sForm.getBody();
		toolkit.paintBordersFor(container);
		sForm.getBody().setLayout(new GridLayout(1, false));
		
		Section sctnSection_1 = toolkit.createSection(container, Section.TITLE_BAR);
		sctnSection_1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		toolkit.paintBordersFor(sctnSection_1);
		sctnSection_1.setText("Startup Analysis");
		sctnSection_1.setExpanded(true);
		
		Composite composite_1 = toolkit.createComposite(sctnSection_1, SWT.NONE);
		toolkit.paintBordersFor(composite_1);
		sctnSection_1.setClient(composite_1);
		composite_1.setLayout(new GridLayout(1, false));
		composite_1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
				
		JFreeChart chart1 = createStartupAnalysisChart(createStartupAnalysisDataset(), "Startup Analysis I");
		chartComposite1 = new ChartComposite(composite_1, SWT.NONE,
				chart1, true);
		chartComposite1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		toolkit.paintBordersFor(chartComposite1);
		
		Activator.getDefault().getBundle().getBundleContext().addBundleListener(this);
		
		makeActions();
		fillToolBar();

	}
	
	private void fillToolBar() {
		getViewSite().getActionBars().getToolBarManager().add(openStartupTypeDialog);
		
	}

	private void makeActions() {
		openStartupTypeDialog = new Action("Details", SWT.PUSH) {
			@Override
			public void run() {
				StartupAnalysisDetailsDialog dialog = new StartupAnalysisDetailsDialog(getViewSite().getShell(), startupBundles, activatedBundles, indirectActivatedBundles);
				dialog.open();
			}
		};
		openStartupTypeDialog.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));
		
	}

	private JFreeChart createStartupAnalysisChart(PieDataset dataset, String chartTitle) {

		JFreeChart chart = ChartFactory.createPieChart(chartTitle, // charttitle
				dataset, // data
				true, // include legend
				true, false);

		PiePlot plot = (PiePlot) chart.getPlot();
		plot.setSectionOutlinesVisible(false);
		plot.setLabelFont(new Font("ARIAL", Font.PLAIN, 12));
		plot.setNoDataMessage("No data available");
		plot.setCircular(false);
		plot.setLabelGap(0.02);
		return chart;

	}
	
	/** * Creates the Dataset for the Pie chart */

	private PieDataset createStartupAnalysisDataset() {
		DefaultPieDataset dataset = new DefaultPieDataset();
		
		int noOfStartupBundles = 0;
		int noOfActivatedBundles = 0;
		int noOfIndirectActivatedBundles = 0;
		
		startupBundles = new ArrayList<BundleStats>();
		activatedBundles = new ArrayList<BundleStats>();
		indirectActivatedBundles = new ArrayList<BundleStats>();
		
		BundleStats[] bundles = StatsManager.getDefault().getBundles();
		for (BundleStats bundle : bundles) {
			if (bundle.isStartupBundle()) {
				noOfStartupBundles +=1;
				startupBundles.add(bundle);
			} else {
				if (bundle.getActivatedBy() == null) {
					noOfIndirectActivatedBundles += 1;
					indirectActivatedBundles.add(bundle);
				} else {
					noOfActivatedBundles += 1;
					activatedBundles.add(bundle);
				}
			}
		}
				
		dataset.setValue("IStartup Contributions", noOfStartupBundles);
		dataset.setValue("Extension Contributions", noOfIndirectActivatedBundles);
		dataset.setValue("Dependant Activation", noOfActivatedBundles);
		

		
		return dataset;
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	@Override
	public void bundleChanged(BundleEvent event) {
		
			if(event.getType() == BundleEvent.STARTED){
			Display.getDefault().asyncExec(new Runnable() {
				public void run() {
					JFreeChart chart1 = createStartupAnalysisChart(createStartupAnalysisDataset(), "Startup Analysis I");
					chartComposite1.setChart(chart1);
					
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
