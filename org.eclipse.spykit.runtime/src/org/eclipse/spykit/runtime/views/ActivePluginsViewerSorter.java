package org.eclipse.spykit.runtime.views;

import org.eclipse.core.runtime.internal.stats.BundleStats;
import org.eclipse.core.runtime.internal.stats.ClassStats;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.spykit.runtime.views.columns.Columns.Column;


/**
 * The sorter to sort the columns
 * 
 * @author Sandeesh
 */
public class ActivePluginsViewerSorter extends ViewerComparator {

  private ActivePluginsView view;
  private Column column;
  private boolean isAscendingOrder;

  /**
   * {@inheritDoc}
   */
  @Override
  public int compare(final Viewer viewer, final Object e1, final Object e2) {
    if (this.column != null) {
      int compareResult;
      if ((e1 instanceof BundleStats) && (e2 instanceof BundleStats)) {
        compareResult = this.column.columnComparator.compare(e1, e2);
        if (!this.isAscendingOrder) {
          return compareResult *= -1;
        }
        return compareResult;
      }
      else if ((e1 instanceof ClassStats) && (e2 instanceof ClassStats)) {
        compareResult = this.column.columnComparator.compare(e1, e2);
        if (!this.isAscendingOrder) {
          return compareResult *= -1;
        }
        return compareResult;
      }
    }

    return super.compare(viewer, e1, e2);
  }

  /**
   * @param ascending
   */
  public void setAscendingOrder(final boolean ascending) {
    this.isAscendingOrder = ascending;

  }

  /**
   * @param column
   */
  public void setColumnToSortBy(final Column column) {
    this.column = column;

  }

}
