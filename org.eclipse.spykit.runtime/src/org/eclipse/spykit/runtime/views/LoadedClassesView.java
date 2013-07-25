package org.eclipse.spykit.runtime.views;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.runtime.internal.stats.BundleStats;
import org.eclipse.core.runtime.internal.stats.ClassStats;
import org.eclipse.core.runtime.internal.stats.ClassloaderStats;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnPixelData;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.spykit.runtime.views.actions.CopyDataAction;
import org.eclipse.spykit.runtime.views.actions.SelectAllAction;
import org.eclipse.spykit.runtime.views.columns.Columns;
import org.eclipse.spykit.runtime.views.columns.Columns.Column;
import org.eclipse.spykit.runtime.views.columns.TreePatternFilter;
import org.eclipse.spykit.runtime.views.providers.LoadedClassesContentProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.dialogs.FilteredTree;
import org.eclipse.ui.part.ViewPart;


/**
 * @author Sandeeesh
 */
public class LoadedClassesView extends ViewPart implements ISelectionListener {

  /**
   * the view id
   */
  public static String VIEW_ID = "org.eclipse.spykit.runtime.views.LoadedClassesView";
  private TreeViewer viewer;
  private ActivePluginsViewerSorter sorter;
  private Clipboard clipboard;

  /**
   * {@inheritDoc}
   */
  @Override
  public void init(final IViewSite site) throws PartInitException {
    super.init(site);
    this.sorter = new ActivePluginsViewerSorter();
  }

  @Override
  public void createPartControl(final Composite parent) {
    parent.setLayout(new FillLayout());
    FilteredTree filteredTree =
        new FilteredTree(parent, SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.MULTI, new TreePatternFilter(
            VIEW_ID), true);
    this.viewer = filteredTree.getViewer();

    getSite().setSelectionProvider(this.viewer);
    this.viewer.setSelection(StructuredSelection.EMPTY);

    this.viewer.setUseHashlookup(true);

    this.viewer.setContentProvider(new LoadedClassesContentProvider(this));

    Tree tree = this.viewer.getTree();
    tree.setLinesVisible(true);
    tree.setHeaderVisible(true);

    this.viewer.setComparator(getSorter());
    createColumns();


    this.viewer.setInput(ClassStats.class);

    getSite().getPage().addPostSelectionListener(ActivePluginsView.VIEW_ID, this);

    // register global copy action
    getViewSite().getActionBars().setGlobalActionHandler(ActionFactory.COPY.getId(), new CopyDataAction(this));
    getViewSite().getActionBars().setGlobalActionHandler(ActionFactory.SELECT_ALL.getId(),
        new SelectAllAction(this.viewer));
    getViewSite().getActionBars().updateActionBars();

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
   * @return the viewer
   */
  public TreeViewer getViewer() {
    return this.viewer;
  }

  /**
   * @return the sorter
   */
  private ActivePluginsViewerSorter getSorter() {
    return this.sorter;
  }

  @Override
  public void setFocus() {
    this.viewer.getControl().setFocus();

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void selectionChanged(final IWorkbenchPart part, final ISelection selection) {

    if (selection instanceof IStructuredSelection) {
      IStructuredSelection sel = (IStructuredSelection) selection;
      Object firstElement = sel.getFirstElement();
      if (firstElement instanceof BundleStats) {
        this.viewer.setInput(getClasses(((BundleStats) firstElement).getSymbolicName()));
        return;
      }
    }
    this.viewer.setInput(null);
  }

  /**
   * @param string
   * @return
   */
  private List<ClassStats> getClasses(final String id) {
    List<String> list = new ArrayList<String>();
    ClassloaderStats loader = ClassloaderStats.getLoader(id);
    if (loader != null) {
      ClassStats[] classes = loader.getClasses();
      if (classes != null) {
        return Arrays.asList(classes);
      }
    }
    return Collections.EMPTY_LIST;
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
   * {@inheritDoc}
   */
  @Override
  public void dispose() {
    getSite().getPage().removePostSelectionListener(ActivePluginsView.VIEW_ID, this);
    super.dispose();
  }

  /**
   * @param firstElement
   * @return
   */
  public StringBuilder getClipBoardData(final ClassStats firstElement) {

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
}
