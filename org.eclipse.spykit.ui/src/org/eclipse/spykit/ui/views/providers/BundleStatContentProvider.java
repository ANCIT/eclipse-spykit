// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) radix(10) lradix(10)
// Source File Name: ActivePluginsViewContentProvider.java

package org.eclipse.spykit.ui.views.providers;

import java.util.List;

import org.eclipse.core.runtime.internal.stats.BundleStats;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * @author Annamalai
 */
public class BundleStatContentProvider implements ITreeContentProvider {


  
  
  private static final String DEPENDANT_ACTIVATION = "Dependant Activation";
private static final String EXTENSION_CONTRIBUTIONS = "Extension Contributions";
private static final String I_STARTUP_CONTRIBUTIONS = "IStartup Contributions";
private final List<BundleStats> startupBundles;
private final List<BundleStats> activatedBundles;
private final List<BundleStats> indirectActivatedBundles;

  
  public BundleStatContentProvider(List<BundleStats> startupBundles, List<BundleStats> activatedBundles, List<BundleStats> indirectActivatedBundles) {
	this.startupBundles = startupBundles;
	// TODO Auto-generated constructor stub
	this.activatedBundles = activatedBundles;
	this.indirectActivatedBundles = indirectActivatedBundles;
}
  
@Override
  public Object[] getChildren(final Object element) {
	  if(element.toString().equals(I_STARTUP_CONTRIBUTIONS)) {
		  return startupBundles.toArray();
	  } else if (element.toString().equals(EXTENSION_CONTRIBUTIONS)) {
		  return indirectActivatedBundles.toArray();
	  } else {
		  return activatedBundles.toArray();
	  }
  }

  @Override
  public Object getParent(final Object element) {

    return null;
  }

  @Override
  public boolean hasChildren(final Object element) {
	  if(element instanceof String) {
		  return true;
	  }
    return false;
  }

  @Override
  public Object[] getElements(final Object inputElement) {
   return new String[]{I_STARTUP_CONTRIBUTIONS,EXTENSION_CONTRIBUTIONS,DEPENDANT_ACTIVATION};
  }

@Override
public void dispose() {
	// TODO Auto-generated method stub
	
}

@Override
public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	// TODO Auto-generated method stub
	
}

  
}
