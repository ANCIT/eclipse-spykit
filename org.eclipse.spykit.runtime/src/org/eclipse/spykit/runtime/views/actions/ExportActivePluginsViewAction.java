package org.eclipse.spykit.runtime.views.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;


/**
 * @author Sandeesh
 */
public class ExportActivePluginsViewAction implements IViewActionDelegate {

  private IViewPart viewPart;

  @Override
  public void run(final IAction action) {
    MessageDialog
        .openInformation(
            this.viewPart.getSite().getShell(),
            "Export Disabled",
            "The export functionality is not yet implemented. Please select all rows and copy 'CTRL + c' the data in tab separated format.");
  }

  @Override
  public void selectionChanged(final IAction action, final ISelection selection) {
    //

  }

  @Override
  public void init(final IViewPart view) {
    this.viewPart = view;

  }


}
