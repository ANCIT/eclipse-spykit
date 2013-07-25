package org.eclipse.spykit.runtime.views.providers;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.internal.stats.ClassStats;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.spykit.runtime.views.LoadedClassesView;


/**
 * @author Sandeesh
 */
public class LoadedClassesContentProvider implements ITreeContentProvider {

  private final LoadedClassesView view;
  private final List<ClassStats> classes = new ArrayList<ClassStats>();

  /**
   * @param loadedClassesView
   */
  public LoadedClassesContentProvider(final LoadedClassesView view) {
    this.view = view;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void dispose() {
    //
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput) {

    if (oldInput != newInput) {
      if (newInput instanceof List) {
        this.classes.clear();
        this.classes.addAll((List<ClassStats>) newInput);
      }
    }

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object[] getElements(final Object inputElement) {

    return this.classes.toArray();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object[] getChildren(final Object parentElement) {
    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object getParent(final Object element) {
    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasChildren(final Object element) {
    return false;
  }

}
