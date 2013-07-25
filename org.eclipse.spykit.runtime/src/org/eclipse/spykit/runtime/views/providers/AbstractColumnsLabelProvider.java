package org.eclipse.spykit.runtime.views.providers;

import java.util.Comparator;

import org.eclipse.core.runtime.internal.stats.BundleStats;
import org.eclipse.core.runtime.internal.stats.ClassStats;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.spykit.runtime.views.columns.ColumnName;


/**
 * The column label provider
 * 
 * @author Sandeesh
 */
public abstract class AbstractColumnsLabelProvider extends ColumnLabelProvider implements Comparator<Object> {


  private final ColumnName columnName;

  /**
   * 
   */
  public AbstractColumnsLabelProvider(final ColumnName plugin) {
    this.columnName = plugin;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final String getText(final Object element) {
    if (element instanceof BundleStats) {
      return getBundleText((BundleStats) element);
    }
    else if (element instanceof ClassStats) {
      return getBundleText((ClassStats) element);
    }
    return "Not a bundle";
  }

  /**
   * @param stats
   * @return
   */
  abstract String getBundleText(BundleStats stats);

  abstract String getBundleText(ClassStats stats);


  /**
   * @return the columnName
   */
  public ColumnName getColumnName() {
    return this.columnName;
  }

}
