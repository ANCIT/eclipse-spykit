package org.eclipse.spykit.runtime.snapshot.views;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.internal.stats.BundleStats;
import org.eclipse.core.runtime.internal.stats.StatsManager;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.spykit.runtime.snapshot.Activator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.XYItemLabelGenerator;
import org.jfree.chart.labels.XYToolTipGenerator;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataItem;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.experimental.chart.swt.ChartComposite;

public class SnapshotView extends ViewPart {

	public static final String ID = "org.eclipse.spykit.runtime.snapshot.views.SnapshotView"; //$NON-NLS-1$
	private Action collectAction;
	private Action clearAction;
	private XYSeries series1;
	private List<Double> countOfPlugins = new ArrayList<Double>();
	
	private JFreeChart chart1;
	private ChartComposite chartComposite1;
	private Action saveAction;
	private Action loadAction;
	private XYSeriesCollection dataset;

	public SnapshotView() {
	}

	/**
	 * Create contents of the view part.
	 * @param parent
	 */
	@Override
	public void createPartControl(Composite parent) {
		countOfPlugins.add(0, new Double(StatsManager.getDefault().getBundles().length));	
		parent.setLayout(new FillLayout());
		JFreeChart chart1 = createChart(createDataset());
		chartComposite1 = new ChartComposite(parent, SWT.NONE,
				chart1, true);
//		toolkit.paintBordersFor(chartComposite1);

		createActions();
		initializeToolBar();
		initializeMenu();
	}

	/**
	 * Create the actions.
	 */
	private void createActions() {
		// Create the actions
		{
			ImageDescriptor img = Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "icons/images.jpeg");
			collectAction = new Action("Collect", img) {
				@Override
				public void run() {
					BundleStats[] bundles = StatsManager.getDefault().getBundles();
					double noOfBundles = bundles.length;
					countOfPlugins.add(noOfBundles);
					redrawChart();
				}
			};
			collectAction.setToolTipText("Collect Plugin Count !!");
		}
		{
			ImageDescriptor img = Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "icons/clear.jpeg");
			clearAction = new Action("Clear", img) {
				@Override
				public void run() {
					countOfPlugins.clear();
					countOfPlugins.add((double)StatsManager.getDefault().getBundles().length);
					redrawChart();
				}
			};
		}
		{
			ImageDescriptor img =  PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_ETOOL_SAVE_EDIT);
			saveAction = new Action("Save", img) {
				@Override
				public void run() {
					FileDialog fDialog = new FileDialog(Display.getDefault()
							.getActiveShell(), SWT.SAVE);
					String filepath = fDialog.open();
					if (filepath != null && filepath.trim().length() != 0) {
						try {
							BufferedWriter bw = new BufferedWriter(
									new FileWriter(new File(filepath), true));
							XYSeries seriesCurrent = (XYSeries) dataset.getSeries().get(0);
							List items = seriesCurrent.getItems();
							for (Object object : items) {
								XYDataItem dataItem = (XYDataItem)object;
								String result = Double.toString(dataItem.getXValue());
								result +=","+Double.toString(dataItem.getYValue());
								bw.write(result);
								bw.newLine();
							}
							
							bw.close();
						} catch (Exception e) {
						}

					}

				}

			};
			saveAction.setToolTipText("Click to Save Current Snapshot Details");
		}
		{
			ImageDescriptor img = PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_ETOOL_HOME_NAV);
			loadAction = new Action("Load", img) {
				@Override
				public void run() {
					FileDialog fDialog = new FileDialog(Display.getDefault().getActiveShell(), SWT.OPEN);
					String filepath = fDialog.open();
					if(filepath.trim().length()!=0) {
						JFreeChart chart1 = createChart(createDataset(filepath));
						chartComposite1.setChart(chart1);
						chartComposite1.forceRedraw();
					}
				}
			};
			loadAction.setToolTipText("Click to Load Previous Snapshot Details");
		}
	}
	
	private XYDataset createDataset(String pathName) {
	
		createDataset();
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File(pathName)));
			String line;
			series1 = new XYSeries("Previous Plugin Set");
			int counter = 1;
			while((line = br.readLine()) != null) {
			     Double count = Double.valueOf((line.substring(line.indexOf(",")+1)));
			     series1.add(counter, count);
			     counter++;
			}

			dataset = getDataset();
			dataset.addSeries(series1);
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
  		
		return getDataset();
		
	}

	protected void redrawChart() {
		JFreeChart chart1 = createChart(createDataset());
		chartComposite1.setChart(chart1);
		chartComposite1.forceRedraw();
	}

	/**
	 * Initialize the toolbar.
	 */
	private void initializeToolBar() {
		IToolBarManager toolbarManager = getViewSite().getActionBars()
				.getToolBarManager();
		toolbarManager.add(collectAction);
		toolbarManager.add(clearAction);
		toolbarManager.add(saveAction);
		toolbarManager.add(loadAction);
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
	
	/**
     * Creates a sample dataset.
     * 
     * @return a sample dataset.
     */
    private XYDataset createDataset() {
        
        series1 = new XYSeries("Current Plugin Set");
        
        int counter = 1;
        for (Double count : countOfPlugins) {
        	series1.add(counter, count);
        	counter++;
		}
   
        dataset = new XYSeriesCollection();
        dataset.addSeries(series1);
  
        return dataset;
        
    }
    
    public XYSeriesCollection getDataset() {
    	if(dataset==null) {
    		dataset = new XYSeriesCollection();
    	}
		return dataset;
	}

	/**
     * Creates a chart.
     * 
     * @param dataset  the data for the chart.
     * 
     * @return a chart.
     */
    private JFreeChart createChart(final XYDataset dataset) {
        
        // create the chart...
        final JFreeChart chart = ChartFactory.createXYLineChart(
            "Plugin Load Snapshot",      // chart title
            "X",                      // x axis label
            "Y",                      // y axis label
            dataset,                  // data
            PlotOrientation.VERTICAL,
            false,                     // include legend
            true,                     // tooltips
            false                     // urls
        );

        // NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...
        chart.setBackgroundPaint(Color.white);
 
//        final StandardLegend legend = (StandardLegend) chart.getLegend();
  //      legend.setDisplaySeriesShapes(true);
        
        // get a reference to the plot for further customisation...
        final XYPlot plot = chart.getXYPlot();
        plot.setBackgroundPaint(Color.lightGray);
    //    plot.setAxisOffset(new Spacer(Spacer.ABSOLUTE, 5.0, 5.0, 5.0, 5.0));
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);
                
        final XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setBaseShapesVisible(true);
        renderer.setBaseShapesFilled(true);
        renderer.setToolTipGenerator(new XYToolTipGenerator() {
			
			@Override
			public String generateToolTip(XYDataset arg0, int arg1, int arg2) {
				// TODO Auto-generated method stub
				return new String(arg0.getXValue(arg1, arg2)+","+arg0.getYValue(arg1, arg2) + " Plugins Loaded.");
			}
		});
        renderer.setBaseItemLabelGenerator(new XYItemLabelGenerator() {
			
			@Override
			public String generateLabel(XYDataset arg0, int arg1, int arg2) {
				// TODO Auto-generated method stub
				return new String(arg0.getXValue(arg1, arg2)+","+arg0.getYValue(arg1, arg2));
			}
		});
        renderer.setSeriesPaint(0, Color.BLUE);
        plot.setRenderer(renderer);
        


        // change the auto tick unit selection to integer units only...
       /* final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createStandardTickUnits());*/
        // OPTIONAL CUSTOMISATION COMPLETED.
        
                
        return chart;
        
    }
}
