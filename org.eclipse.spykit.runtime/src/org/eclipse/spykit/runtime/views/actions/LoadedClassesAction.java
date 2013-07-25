package org.eclipse.spykit.runtime.views.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.spykit.runtime.views.LoadedClassesView;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;


/**
 * @author Sandeesh
 */
public class LoadedClassesAction implements IViewActionDelegate {

  @Override
  public void run(final IAction action) {
    try {
      PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(LoadedClassesView.VIEW_ID);
    }
    catch (PartInitException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

  }

  @Override
  public void selectionChanged(final IAction action, final ISelection selection) {
    // TODO Auto-generated method stub

  }

  @Override
  public void init(final IViewPart view) {
    // TODO Auto-generated method stub

  }

}
