package org.eclipse.spykit.runtime.views.columns;


/**
 * The colums to show in the view
 * 
 * @author Sandeesh
 */
public enum ColumnName {


  /**
   * 
   */
  PLUGIN("Plugin"),

  /**
   * 
   */
  START_UP_TIME("StartUp Time(msec)"),

  /**
   * 
   */
  ACTIVATION_ORDER("Activation Order"),

  /**
   * 
   */
  TIME_STAMP("TimeStamp"),

  /**
   * 
   */
  NUMBER_OF_CLASSES_LOADED("Number of Classes Loaded"),

  /**
   * 
   */
  TIME_TAKEN_TO_LOAD_CLASSES("TimeTaken To Load Clases"),

  /**
   * 
   */
  ACTIVATED_BY("Activated By"),

  /**
   * 
   */
  START_UP_METHOD_TIME("StartUp Method Time"),

  // columns for loaded class

  /**
   * 
   */
  CLASSES("Classes"),

  /**
   * 
   */
  CLASS_LOAD_ORDER("ClassLoadOrder"),

  /**
   * 
   */
  CLASS_LOAD_TIME("Load Time"),

  /**
   * 
   */
  CLASS_LOADED_BY("Loaded By"),

  /**
   * 
   */
  CLASS_IS_LOADED_DURING_START_UP("Loaded During StartUp")


  ;


  private String str;

  private ColumnName(final String str1) {
    this.str = str1;
  }

  /**
   * @return the columnName
   */
  public String getColumnName() {
    return this.str;
  }
}
