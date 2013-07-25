package org.eclipse.spykit.runtime.views.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.TreeViewer;


/**
 * The action to select all the rows in the view
 * 
 * @author Sandeesh
 */
public class SelectAllAction extends Action {

  private final TreeViewer viewer;

  /**
   * @param viewer
   */
  public SelectAllAction(final TreeViewer viewer) {
    this.viewer = viewer;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void run() {
    this.viewer.getTree().selectAll();
  }

}
