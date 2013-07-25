package org.eclipse.spykit.ui.views;

import java.awt.Font;
import java.util.HashMap;
import java.util.Iterator;

import org.eclipse.core.runtime.internal.stats.BundleStats;
import org.eclipse.core.runtime.internal.stats.StatsManager;
import org.eclipse.spykit.ui.Activator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
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

public class PluginLoadedByView extends ViewPart {

	private final FormToolkit toolkit = new FormToolkit(Display.getCurrent());
	private ChartComposite chartComposite1;

	public PluginLoadedByView() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void createPartControl(Composite parent) {
		ScrolledForm sForm = toolkit.createScrolledForm(parent);

		Composite container = sForm.getBody();
		toolkit.paintBordersFor(container);
		sForm.getBody().setLayout(new GridLayout(1, false));

		Section sctnSection_1 = toolkit.createSection(container,
				Section.TITLE_BAR);
		sctnSection_1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true, 1, 1));
		toolkit.paintBordersFor(sctnSection_1);
		sctnSection_1.setText("Startup Analysis");
		sctnSection_1.setExpanded(true);

		Composite composite_1 = toolkit
				.createComposite(sctnSection_1, SWT.NONE);
		toolkit.paintBordersFor(composite_1);
		sctnSection_1.setClient(composite_1);
		composite_1.setLayout(new GridLayout(1, false));
		composite_1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true,
				1, 1));

		JFreeChart chart1 = createStartupAnalysisChart(
				createActivatedByDataset(), "Startup Analysis II");
		chartComposite1 = new ChartComposite(composite_1, SWT.NONE, chart1,
				true);
		chartComposite1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true, 1, 1));
		toolkit.paintBordersFor(chartComposite1);

		Activator.getDefault().getBundle().getBundleContext()
				.addBundleListener(new BundleListener() {

					@Override
					public void bundleChanged(BundleEvent event) {
						Display.getDefault().asyncExec(new Runnable() {
							public void run() {
								JFreeChart chart1 = createStartupAnalysisChart(
										createActivatedByDataset(),
										"Startup Analysis I");
								chartComposite1.setChart(chart1);

							}
						});

					}
				});

	}

	/*
	 * Creates the Chart based on a dataset
	 */

	private PieDataset createActivatedByDataset() {
		DefaultPieDataset dataset = new DefaultPieDataset();

		BundleStats[] bundles = StatsManager.getDefault().getBundles();
		HashMap<String, Integer> bundleMap = new HashMap<String, Integer>();
		BundleStats parentBundle = null;
		for (BundleStats bundle : bundles) {
			if (bundle.getActivatedBy() != null) {
				parentBundle = fetchParentBundle(bundle);
			} else {
				parentBundle = bundle;
			}

			Integer bundleCount = 1;
			String keyName;

			if (parentBundle.equals(bundle)) {
				keyName = "others";
				if (bundleMap.containsKey(keyName)) {
					bundleCount = bundleMap.get("others");
					bundleCount += 1;
				}
				bundleMap.put(keyName, bundleCount);
				continue;
			}

			if (parentBundle != null
					&& bundleMap.containsKey(parentBundle.getSymbolicName())) {
				bundleCount = bundleMap.get(parentBundle.getSymbolicName());
				bundleCount += 1;
			}
			bundleMap.put(parentBundle.getSymbolicName(), bundleCount);
		}

		for (Iterator iterator = bundleMap.keySet().iterator(); iterator
				.hasNext();) {
			String type = (String) iterator.next();
			Integer count = bundleMap.get(type);
			if (count != 1) {
				dataset.setValue(type, count);
			}
		}

		return dataset;
	}

	private BundleStats fetchParentBundle(BundleStats bundle) {
		BundleStats parentBundle = bundle.getActivatedBy();
		if (parentBundle != null) {
			parentBundle = fetchParentBundle(parentBundle);
		} else {
			parentBundle = bundle;
		}
		return parentBundle;
	}

	private JFreeChart createStartupAnalysisChart(PieDataset dataset,
			String chartTitle) {

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

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

}
