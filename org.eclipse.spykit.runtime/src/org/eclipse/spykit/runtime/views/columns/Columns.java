package org.eclipse.spykit.runtime.views.columns;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.spykit.runtime.views.ActivePluginsView;
import org.eclipse.spykit.runtime.views.LoadedClassesView;
import org.eclipse.spykit.runtime.views.providers.AbstractColumnsLabelProvider;
import org.eclipse.spykit.runtime.views.providers.ActivePluginsViewColumnLabelProvider;
import org.eclipse.spykit.runtime.views.providers.LoadedClassesViewColumnLabelProvider;


/**
 * The columns to show in the views
 * 
 * @author Sandeesh
 */
public class Columns {


  /**
   * @author Sandeesh
   */
  public static final class Column {

    /**
     * 
     */
    public final String name;
    /**
     * 
     */
    public final AbstractColumnsLabelProvider columnLableProvider;
    /**
     * 
     */
    public final Comparator<Object> columnComparator;
    /**
     * 
     */
    public final String description;
    /**
     * 
     */
    public final int preferredWidth;


    /**
     * @param name the column name
     * @param columnLabelProvider the {@link AbstractColumnsLabelProvider}
     * @param columnComparator the column comparator
     * @param description the description
     * @param preferredWidth the preferred width
     */
    public Column(final String name, final AbstractColumnsLabelProvider columnLabelProvider,
        final Comparator<Object> columnComparator, final String description, final int preferredWidth) {
      this.name = name;
      this.columnLableProvider = columnLabelProvider;
      this.columnComparator = columnComparator;
      this.description = description;
      this.preferredWidth = preferredWidth;
    }

  }

  private static Map<String, List<Column>> columnsToViewMap;

  /**
   * @param viewId the view to fetch the columns for
   * @return The list of columns
   */
  public static List<Column> getColumns(final String viewId) {
    if (columnsToViewMap == null) {
      columnsToViewMap = new HashMap<String, List<Column>>();
      createColumns();
    }

    return Collections.unmodifiableList(columnsToViewMap.get(viewId));
  }

  /**
   * Adds the columns to the list
   */
  private static void createColumns() {
    List<Column> activePluginsViewColumns = new ArrayList<Column>();
    List<Column> loadedClassesViewColumns = new ArrayList<Column>();
    // column 1
    String description = "Shows activated plugins.(* indicates that the plugin is a start up plugin)";
    activePluginsViewColumns.add(createColumn(ColumnName.PLUGIN, description, 150, true));
    // column 2
    description = "Shows the startup time for the plugin in milli seconds.";
    activePluginsViewColumns.add(createColumn(ColumnName.START_UP_TIME, description, 100, true));
    // column 3
    description = "Shows the activation order.";
    activePluginsViewColumns.add(createColumn(ColumnName.ACTIVATION_ORDER, description, 100, true));
    // column 4
    description = "Shows the plugin that activated it";
    activePluginsViewColumns.add(createColumn(ColumnName.ACTIVATED_BY, description, 100, true));
    // column 5
    description = "Shows the time stamp i.e when the plugin was started";
    activePluginsViewColumns.add(createColumn(ColumnName.TIME_STAMP, description, 100, true));
    // column 6
    description = "Shows the number of classes loaded by this plugin.";
    activePluginsViewColumns.add(createColumn(ColumnName.NUMBER_OF_CLASSES_LOADED, description, 100, true));
    // column 7
    description = "Shows the time taken to load classes.";
    activePluginsViewColumns.add(createColumn(ColumnName.TIME_TAKEN_TO_LOAD_CLASSES, description, 100, true));
    // column 8
    description = "Shows the start up method time of the plugin.";
    activePluginsViewColumns.add(createColumn(ColumnName.START_UP_METHOD_TIME, description, 100, true));


    columnsToViewMap.put(ActivePluginsView.VIEW_ID, activePluginsViewColumns);

    // column 1
    description = "Shows the classes loaded by the selected plugin.";
    loadedClassesViewColumns.add(createColumn(ColumnName.CLASSES, description, 100, false));
    // column 2
    description = "Shows the order in which the classes were loaded";
    loadedClassesViewColumns.add(createColumn(ColumnName.CLASS_LOAD_ORDER, description, 100, false));
    // column 3
    description = "Shows the time taken to load the class.";
    loadedClassesViewColumns.add(createColumn(ColumnName.CLASS_LOAD_TIME, description, 100, false));
    // column 4
    description = "Shows who loaded this class.";
    loadedClassesViewColumns.add(createColumn(ColumnName.CLASS_LOADED_BY, description, 100, false));
    // column 5
    description = "Shows whether the class was loaded during startUp.";
    loadedClassesViewColumns.add(createColumn(ColumnName.CLASS_IS_LOADED_DURING_START_UP, description, 100, false));


    columnsToViewMap.put(LoadedClassesView.VIEW_ID, loadedClassesViewColumns);
  }

  /**
   * @param plugin
   * @param description
   * @param preferredWidth
   * @return
   */
  private static Column createColumn(final ColumnName plugin, final String description, final int preferredWidth,
      final boolean isActivePluginsView) {
    AbstractColumnsLabelProvider pluginColumnLabelProvider = createLabelProvider(plugin, isActivePluginsView);
    return new Column(plugin.getColumnName(), pluginColumnLabelProvider, pluginColumnLabelProvider, description,
        preferredWidth);
  }

  /**
   * @param columnNameEnum
   * @return
   */
  private static AbstractColumnsLabelProvider createLabelProvider(final ColumnName columnNameEnum,
      final boolean isActivePluginsView) {
    AbstractColumnsLabelProvider pluginColumnLabelProvider;
    if (isActivePluginsView) {
      pluginColumnLabelProvider = new ActivePluginsViewColumnLabelProvider(columnNameEnum);
    }
    else {
      pluginColumnLabelProvider = new LoadedClassesViewColumnLabelProvider(columnNameEnum);
    }
    return pluginColumnLabelProvider;
  }


}
