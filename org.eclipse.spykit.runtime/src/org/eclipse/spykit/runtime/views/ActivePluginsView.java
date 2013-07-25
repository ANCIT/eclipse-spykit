package org.eclipse.spykit.runtime.views;


import java.util.List;

import org.eclipse.core.runtime.internal.stats.BundleStats;
import org.eclipse.core.runtime.internal.stats.StatsManager;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnPixelData;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.spykit.runtime.Activator;
import org.eclipse.spykit.runtime.views.actions.CopyDataAction;
import org.eclipse.spykit.runtime.views.actions.SelectAllAction;
import org.eclipse.spykit.runtime.views.columns.Columns;
import org.eclipse.spykit.runtime.views.columns.Columns.Column;
import org.eclipse.spykit.runtime.views.columns.TreePatternFilter;
import org.eclipse.spykit.runtime.views.providers.ActivePluginsViewContentProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.dialogs.FilteredTree;
import org.eclipse.ui.part.ViewPart;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;


/**
 * @author Sandeesh
 */
public class ActivePluginsView extends ViewPart implements BundleListener {
	public ActivePluginsView() {
	}

  /**
   * 
   */
  public static String VIEW_ID = "org.eclipse.spykit.runtime.views.ActivePluginsView";
  private TreeViewer viewer;
  private Clipboard clipboard;
  private final ActivePluginsViewContentProvider contentProvider = new ActivePluginsViewContentProvider();
  private ActivePluginsViewerSorter sorter;
private Label lbl;

  /**
   * {@inheritDoc}
   */
  @Override
  public void init(final IViewSite site) throws PartInitException {
    super.init(site);
    this.sorter = new ActivePluginsViewerSorter();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void createPartControl(final Composite parent) {
	  Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		composite.setLayout(new GridLayout(1, false));
    
	if (!StatsManager.MONITOR_ACTIVATION) {
      Text text = new Text(composite, 0);
      text.setText("Plugin monitoring is not enabled. The monitoring on org.eclipse.osgi/monitor/activation should be set to true either in tracing or .options file");
      return;
    }

    
	lbl = new Label(composite, SWT.None);
	lbl.setText("Total Number of Plugins Loaded :: "+ StatsManager.getDefault().getBundles().length + "");

    FilteredTree filteredTree =
        new FilteredTree(composite, SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.MULTI, new TreePatternFilter(
            VIEW_ID), true);
    this.viewer = filteredTree.getViewer();
	
    getSite().setSelectionProvider(this.viewer);
    this.viewer.setSelection(StructuredSelection.EMPTY);

    this.viewer.setUseHashlookup(true);
    this.viewer.setContentProvider(getContentProvider());

    Tree tree = this.viewer.getTree();
    tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
    tree.setLinesVisible(true);
    tree.setHeaderVisible(true);

    this.viewer.setComparator(getSorter());
    createColumns();

    this.viewer.setInput(BundleStats.class);

    // register global copy action
    getViewSite().getActionBars().setGlobalActionHandler(ActionFactory.COPY.getId(), new CopyDataAction(this));
    getViewSite().getActionBars().setGlobalActionHandler(ActionFactory.SELECT_ALL.getId(),
        new SelectAllAction(this.viewer));
    getViewSite().getActionBars().updateActionBars();
    
    Activator.getDefault().getBundle().getBundleContext().addBundleListener(this);
  }

  /**
   * @return
   */
  private ActivePluginsViewerSorter getSorter() {
    return this.sorter;
  }

  /**
   * @return the content provider
   */
  public ActivePluginsViewContentProvider getContentProvider() {
    return this.contentProvider;
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public void setFocus() {
    if (getViewer() != null) {
      getViewer().getControl().setFocus();
    }
  }

  private void createColumns() {
    TableLayout tableLayout = new TableLayout();
    Tree tree = getViewer().getTree();
    tree.setLayout(tableLayout);

    HeaderListener headerListener = new HeaderListener();
    List<Column> columns = Columns.getColumns(VIEW_ID);
    for (Column column : columns) {
      tableLayout.addColumnData(new ColumnPixelData(column.preferredWidth));
      TreeColumn treeColumn = new TreeColumn(tree, SWT.LEFT);
      treeColumn.setText(column.name);
      treeColumn.setToolTipText(column.description);
      treeColumn.setData(column);
      treeColumn.setResizable(true);
      treeColumn.setMoveable(true);
      treeColumn.addSelectionListener(headerListener);
      TreeViewerColumn viewerColumn = new TreeViewerColumn(getViewer(), treeColumn);
      viewerColumn.setLabelProvider(column.columnLableProvider);
    }
    tree.layout(true);

  }

  /**
   * Listens for table header selection to start sorting of table.
   * 
   * @author Lothar Wendehals
   */
  private class HeaderListener extends SelectionAdapter {

    /**
     * Handles the case of user selecting the header area.
     */
    @SuppressWarnings("synthetic-access")
    @Override
    public void widgetSelected(final SelectionEvent event) {
      final TreeColumn treeColumn = (TreeColumn) event.widget;
      final Column column = (Column) treeColumn.getData();

      int currentOrder = getViewer().getTree().getSortDirection();
      final boolean ascending = (currentOrder == SWT.DOWN) || (currentOrder == SWT.NONE);

      // getSorter().setAscendingOrder(ascending);
      // getSorter().setColumnToSortBy(column);
      getSorter().setAscendingOrder(ascending);
      getSorter().setColumnToSortBy(column);
      getViewer().setComparator(null);
      getViewer().setComparator(getSorter());
      updateDirectionIndicator(treeColumn, ascending);
    }
  }

  /**
   * Update the direction indicator as column is now the primary column.
   * 
   * @param column the tree columns to update the direction indicator for
   * @param ascending whether the indicator shall represent ascending or descending order
   */
  private void updateDirectionIndicator(final TreeColumn column, final boolean ascending) {
    getViewer().getTree().setSortColumn(column);
    if (ascending) {
      getViewer().getTree().setSortDirection(SWT.UP);
    }
    else {
      getViewer().getTree().setSortDirection(SWT.DOWN);
    }
  }

  /**
   * @return ClipBoard instance
   */
  public Clipboard getClipBoard() {
    if (this.clipboard == null) {
      this.clipboard = new Clipboard(Display.getCurrent());
    }
    return this.clipboard;
  }


  /**
   * @return the viewer
   */
  public TreeViewer getViewer() {
    return this.viewer;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void dispose() {
    super.dispose();
    Activator.getDefault().getBundle().getBundleContext().removeBundleListener(this);
  }

  /**
   * @param firstElement
   * @return the tab separated data
   */
  public StringBuilder getClipBoardData(final BundleStats firstElement) {
    StringBuilder builder = new StringBuilder();
    int[] columnOrder = getViewer().getTree().getColumnOrder();
    for (int index : columnOrder) {
      ColumnLabelProvider lbl = (ColumnLabelProvider) getViewer().getLabelProvider(index);
      String text = lbl.getText(firstElement);
      builder.append(text);
      // separator is not needed for after last column
      if (index < (columnOrder.length - 1)) {
        builder.append("\t");
      }
    }
    return builder;
  }

public void refresh() {
		getViewer().refresh();
		lbl.setText("Total Number of Plugins Loaded :: "+ StatsManager.getDefault().getBundles().length + "");
		lbl.pack();
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

}