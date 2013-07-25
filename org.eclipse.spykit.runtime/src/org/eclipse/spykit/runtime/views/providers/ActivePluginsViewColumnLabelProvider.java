package org.eclipse.spykit.runtime.views.providers;

import java.sql.Date;
import java.text.SimpleDateFormat;

import org.eclipse.core.runtime.internal.stats.BundleStats;
import org.eclipse.core.runtime.internal.stats.ClassStats;
import org.eclipse.spykit.runtime.views.columns.ColumnName;


/**
 * @author Sandeesh
 */
public class ActivePluginsViewColumnLabelProvider extends AbstractColumnsLabelProvider {

  /**
   * @param plugin
   */
  public ActivePluginsViewColumnLabelProvider(final ColumnName plugin) {
    super(plugin);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getBundleText(final BundleStats stats) {
    switch (getColumnName()) {
      case PLUGIN:
        return stats.getSymbolicName() + (stats.isStartupBundle() ? "*" : "");

      case TIME_STAMP:
        return getTimeStampString(stats);

      case NUMBER_OF_CLASSES_LOADED:
        return Integer.toString(stats.getClassLoadCount());

      case START_UP_TIME:
        return Long.toString(stats.getStartupTime());

      case ACTIVATION_ORDER:
        return Integer.toString(stats.getActivationOrder());

      case TIME_TAKEN_TO_LOAD_CLASSES:
        return Integer.toString((int) stats.getClassLoadTime());

      case START_UP_METHOD_TIME:
        return Integer.toString((int) stats.getStartupMethodTime());

      case ACTIVATED_BY:
        return stats.getActivatedBy() == null ? "" : stats.getActivatedBy().getSymbolicName();

    }
    return "<Not Available>";
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int compare(final Object o1, final Object o2) {
    String text1 = getBundleText((BundleStats) o1);
    String text2 = getBundleText((BundleStats) o2);
    switch (getColumnName()) {
      case PLUGIN:
      case ACTIVATED_BY:
      case TIME_STAMP:
        return text1.compareTo(text2);

      case NUMBER_OF_CLASSES_LOADED:
      case START_UP_TIME:
      case ACTIVATION_ORDER:
      case TIME_TAKEN_TO_LOAD_CLASSES:
      case START_UP_METHOD_TIME:
        return new Integer(text1).compareTo(new Integer(text2));
    }

    return 0;
  }

  /**
   * @param plugin
   * @return
   */
  public String getTimeStampString(final BundleStats plugin) {
    long timestamp = plugin.getTimestamp();
    Date date = new Date(timestamp);
    SimpleDateFormat dateFormat = new SimpleDateFormat();
    String format = dateFormat.format(date);
    return format;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getToolTipText(final Object element) {
    switch (getColumnName()) {
      case PLUGIN:
        if ((element instanceof BundleStats) && ((BundleStats) element).isStartupBundle()) {
          return "is startup bundle";
        }

    }
    return super.getToolTipText(element);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  String getBundleText(final ClassStats stats) {
    return null;
  }

}
