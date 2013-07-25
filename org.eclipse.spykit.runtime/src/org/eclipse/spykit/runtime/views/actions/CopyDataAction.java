package org.eclipse.spykit.runtime.views.actions;

import java.util.Iterator;

import org.eclipse.core.runtime.internal.stats.BundleStats;
import org.eclipse.core.runtime.internal.stats.ClassStats;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.spykit.runtime.Activator;
import org.eclipse.spykit.runtime.views.ActivePluginsView;
import org.eclipse.spykit.runtime.views.LoadedClassesView;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.ui.part.ViewPart;


/**
 * The action to act upon ctrl + c
 * 
 * @author Sandeesh
 */
public class CopyDataAction extends Action {

  private final ViewPart view;

  /**
   * @param activePluginsView the view
   */
  public CopyDataAction(final ViewPart view) {

    super("Copy to clipboard");
    setId(Activator.PLUGIN_ID + "." + this.getClass().getSimpleName());
    this.view = view;

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void run() {
    Clipboard clipBoard = null;
    StringBuilder data = null;
    if (this.view instanceof ActivePluginsView) {
      ISelection selection = ((ActivePluginsView) this.view).getViewer().getSelection();
      IStructuredSelection structSel = (IStructuredSelection) selection;
      clipBoard = ((ActivePluginsView) this.view).getClipBoard();
      data = new StringBuilder();
      for (Iterator itr = structSel.iterator(); itr.hasNext();) {
        Object obj = itr.next();
        if (obj instanceof BundleStats) {
          data.append(((ActivePluginsView) this.view).getClipBoardData((BundleStats) obj));
          data.append("\n");
        }

      }

    }
    else if (this.view instanceof LoadedClassesView) {
      ISelection selection = ((LoadedClassesView) this.view).getViewer().getSelection();
      clipBoard = ((LoadedClassesView) this.view).getClipBoard();
      IStructuredSelection structSel = (IStructuredSelection) selection;
      clipBoard = ((LoadedClassesView) this.view).getClipBoard();
      data = new StringBuilder();
      for (Iterator itr = structSel.iterator(); itr.hasNext();) {
        Object obj = itr.next();
        if (obj instanceof ClassStats) {
          data.append(((LoadedClassesView) this.view).getClipBoardData((ClassStats) obj));
          data.append("\n");
        }
      }
    }
    if (data != null) {
      copyToClipBoard(clipBoard, data);
    }
  }

  /**
   * @param view the view
   * @param data the data to copy to the clipboard
   */
  private void copyToClipBoard(final Clipboard clipBoard, final StringBuilder data) {

    TextTransfer textTransfer = TextTransfer.getInstance();
    Transfer[] transfers = new Transfer[] { textTransfer };
    Object[] dataArray = new Object[] { data.toString() };
    clipBoard.setContents(dataArray, transfers);
  }

}
