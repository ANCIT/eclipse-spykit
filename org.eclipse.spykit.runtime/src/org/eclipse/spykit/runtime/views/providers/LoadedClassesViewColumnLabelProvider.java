package org.eclipse.spykit.runtime.views.providers;

import org.eclipse.core.runtime.internal.stats.BundleStats;
import org.eclipse.core.runtime.internal.stats.ClassStats;
import org.eclipse.spykit.runtime.views.columns.ColumnName;


/**
 * @author Sandeesh
 */
public class LoadedClassesViewColumnLabelProvider extends AbstractColumnsLabelProvider {

  /**
   * @param plugin
   */
  public LoadedClassesViewColumnLabelProvider(final ColumnName plugin) {
    super(plugin);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int compare(final Object o1, final Object o2) {
    String text1 = getBundleText((ClassStats) o1);
    String text2 = getBundleText((ClassStats) o2);
    switch (getColumnName()) {
      case CLASSES:
      case CLASS_IS_LOADED_DURING_START_UP:
      case CLASS_LOADED_BY:
        return text1.compareTo(text2);
      case CLASS_LOAD_ORDER:
      case CLASS_LOAD_TIME:
        return new Integer(text1).compareTo(new Integer(text2));
    }
    return 0;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  String getBundleText(final BundleStats stats) {
    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  String getBundleText(final ClassStats stats) {
    switch (getColumnName()) {
      case CLASSES:
        return stats.getClassName();
      case CLASS_LOAD_ORDER:
        return Integer.toString(stats.getLoadOrder());
      case CLASS_LOAD_TIME:
        return Long.toString(stats.getTimeLoading());
      case CLASS_IS_LOADED_DURING_START_UP:
        return stats.isStartupClass() ? "YES" : "NO";
      case CLASS_LOADED_BY:
        return stats.getLoadedBy() == null ? "" : stats.getLoadedBy().getClassName();
    }
    return null;
  }

}
