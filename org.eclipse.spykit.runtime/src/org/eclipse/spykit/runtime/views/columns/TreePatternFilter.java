package org.eclipse.spykit.runtime.views.columns;

import java.util.List;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.spykit.runtime.views.columns.Columns.Column;
import org.eclipse.ui.dialogs.PatternFilter;


/**
 * @author Sandeesh
 */
public class TreePatternFilter extends PatternFilter {


  private final String viewID;

  /**
   * 
   */
  public TreePatternFilter(final String viewId) {
    this.viewID = viewId;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected boolean isLeafMatch(final Viewer viewer, final Object element) {
    List<Column> columns = Columns.getColumns(this.viewID);
    for (Column column : columns) {
      String text = column.columnLableProvider.getText(element);
      if (text != null) {
        if (super.isLeafMatch(viewer, text)) {
          return true;
        }
      }
    }
    return false;
  }
}
