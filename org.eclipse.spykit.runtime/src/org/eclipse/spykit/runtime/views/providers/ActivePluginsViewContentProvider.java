// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) radix(10) lradix(10)
// Source File Name: ActivePluginsViewContentProvider.java

package org.eclipse.spykit.runtime.views.providers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.runtime.internal.stats.BundleStats;
import org.eclipse.core.runtime.internal.stats.StatsManager;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * @author Sandeesh
 */
public class ActivePluginsViewContentProvider implements ITreeContentProvider {


  /**
   * @param mode true to show all activated bundles. If false then, shows only bundles activated by default.
   */
  public void setFlat(final boolean mode) {
    this.flat = mode;
  }


  @Override
  public Object[] getChildren(final Object element) {

    return null;
  }

  @Override
  public Object getParent(final Object element) {

    return null;
  }

  @Override
  public boolean hasChildren(final Object element) {

    return false;
  }

  @Override
  public Object[] getElements(final Object inputElement) {
    if (!StatsManager.MONITOR_ACTIVATION || (inputElement != BundleStats.class)) {
      return null;
    }

    if (this.activatedBundlesStatsList.isEmpty()) {
      StatsManager statsManager = StatsManager.getDefault();
      if (statsManager != null) {
        this.activatedBundlesStatsList.addAll(Arrays.asList(statsManager.getBundles()));
      }
    }

    List<BundleStats> result = new ArrayList<BundleStats>(this.activatedBundlesStatsList.size());
    for (BundleStats activePlugin : this.activatedBundlesStatsList) {
      if (this.flat || (activePlugin.getActivatedBy() == null)) {
        result.add(activePlugin);
      }
    }

    return result.toArray(new BundleStats[result.size()]);
  }

  private boolean flat;
  private final List<BundleStats> activatedBundlesStatsList = new ArrayList<BundleStats>();

  /**
   * {@inheritDoc}
   */
  @Override
  public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput) {
    // do nothing
  }

  /**
   * Clear the local bundle stats model
   */
  public void clearModel() {
    this.activatedBundlesStatsList.clear();
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public void dispose() {
    // do nothing

  }
}
