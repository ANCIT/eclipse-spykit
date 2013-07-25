package org.eclipse.spykit.runtime.actionSets;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.spykit.runtime.views.ActivePluginsView;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;


/**
 * Our sample action implements workbench action delegate. The action proxy will be created by the workbench and shown
 * in the UI. When the user tries to use the action, this delegate will be created and execution will be delegated to
 * it.
 * 
 * @see IWorkbenchWindowActionDelegate
 * @author Sandeesh
 */
public class ShowActivePluginsView implements IWorkbenchWindowActionDelegate {

  private IWorkbenchWindow window;

  /**
   * The constructor.
   */
  public ShowActivePluginsView() {}

  /**
   * The action has been activated. The argument of the method represents the 'real' action sitting in the workbench UI.
   * 
   * @see IWorkbenchWindowActionDelegate#run
   */
  public void run(final IAction action) {

    try {
      PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(ActivePluginsView.VIEW_ID);
    }
    catch (PartInitException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }


  }

  /**
   * Selection in the workbench has been changed. We can change the state of the 'real' action here if we want, but this
   * can only happen after the delegate has been created.
   * 
   * @see IWorkbenchWindowActionDelegate#selectionChanged
   */
  public void selectionChanged(final IAction action, final ISelection selection) {}

  /**
   * We can use this method to dispose of any system resources we previously allocated.
   * 
   * @see IWorkbenchWindowActionDelegate#dispose
   */
  public void dispose() {}

  /**
   * We will cache window object in order to be able to provide parent shell for the message dialog.
   * 
   * @see IWorkbenchWindowActionDelegate#init
   */
  public void init(final IWorkbenchWindow window) {
    this.window = window;
  }
}