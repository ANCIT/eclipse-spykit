package org.eclipse.spykit.ui.dialogs;

import org.eclipse.core.runtime.internal.stats.BundleStats;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Table;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.spykit.ui.views.providers.BundleStatContentProvider;
import java.util.List;
import org.eclipse.spykit.ui.views.providers.BundleStatColumnLabelProvider;

public class StartupAnalysisDetailsDialog extends TitleAreaDialog {

	private final List<BundleStats> startupBundles;
	private final List<BundleStats> activatedBundles;
	private final List<BundleStats> indirectActivatedBundles;

	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public StartupAnalysisDetailsDialog(Shell parentShell, List<BundleStats> startupBundles, List<BundleStats> activatedBundles, List<BundleStats> indirectActivatedBundles) {
		super(parentShell);
		this.startupBundles = startupBundles;
		this.activatedBundles = activatedBundles;
		this.indirectActivatedBundles = indirectActivatedBundles;
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		setTitle("Startup Analysis");
		setMessage("Details on Startup Analysis");
		Composite area = (Composite) super.createDialogArea(parent);
		area.setLayout(new GridLayout(1, false));
		
		TreeViewer treeViewer = new TreeViewer(area, SWT.BORDER);
		Tree tree = treeViewer.getTree();
		tree.setLinesVisible(true);
		tree.setHeaderVisible(true);
		tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		TreeColumn trclmnPluginName = new TreeColumn(tree, SWT.NONE);
		trclmnPluginName.setWidth(100);
		trclmnPluginName.setText("Plugin Name");
		
		TreeColumn trclmnTimeStamp = new TreeColumn(tree, SWT.NONE);
		trclmnTimeStamp.setWidth(100);
		trclmnTimeStamp.setText("Time Stamp");
		
		TreeColumn trclmnNoOfClassLoad = new TreeColumn(tree, SWT.NONE);
		trclmnNoOfClassLoad.setWidth(100);
		trclmnNoOfClassLoad.setText("No of Classes Loaded");
		
		TreeColumn trclmnStartupTime = new TreeColumn(tree, SWT.NONE);
		trclmnStartupTime.setWidth(100);
		trclmnStartupTime.setText("Startup Time");
		
		TreeColumn trclmnActivationOrder = new TreeColumn(tree, SWT.NONE);
		trclmnActivationOrder.setWidth(100);
		trclmnActivationOrder.setText("Activation Order");
		
		TreeColumn trclmnActivatedBy = new TreeColumn(tree, SWT.NONE);
		trclmnActivatedBy.setWidth(100);
		trclmnActivatedBy.setText("Activated By");
		treeViewer.setLabelProvider(new BundleStatColumnLabelProvider());
		treeViewer.setContentProvider(new BundleStatContentProvider(startupBundles,activatedBundles, indirectActivatedBundles));
		treeViewer.setInput(new Object());

		return area;
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				true);
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(638, 449);
	}

}
