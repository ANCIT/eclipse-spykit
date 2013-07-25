package org.eclipse.spykit.ui.views.providers;

import java.sql.Date;
import java.text.SimpleDateFormat;

import org.eclipse.core.runtime.internal.stats.BundleStats;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

/**
 * @author Sandeesh
 */
public class BundleStatColumnLabelProvider extends LabelProvider implements
		ITableLabelProvider {

	private static final int PLUGIN = 0;
	private static final int TIME_STAMP = 1;
	private static final int NUMBER_OF_CLASSES_LOADED = 2;
	private static final int START_UP_TIME = 3;
	private static final int ACTIVATION_ORDER = 4;
	private static final int ACTIVATED_BY = 5;

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("restriction")
	@Override
	public String getColumnText(Object object, int columnIndex) {
		if (object instanceof String) {
			if (columnIndex == 0) {
				String groupName = (String) object;
				return groupName;
			}
			return "";
		}
		BundleStats stats = (BundleStats) object;
		switch (columnIndex) {
		case PLUGIN:
			return stats.getSymbolicName()
					+ (stats.isStartupBundle() ? "*" : "");

		case TIME_STAMP:
			return getTimeStampString(stats);

		case NUMBER_OF_CLASSES_LOADED:
			return Integer.toString(stats.getClassLoadCount());

		case START_UP_TIME:
			return Long.toString(stats.getStartupTime());

		case ACTIVATION_ORDER:
			return Integer.toString(stats.getActivationOrder());

		case ACTIVATED_BY:
			return stats.getActivatedBy() == null ? "" : stats.getActivatedBy()
					.getSymbolicName();

		}
		return "<Not Available>";
	}

	/**
	 * @param plugin
	 * @return
	 */
	public String getTimeStampString(final BundleStats plugin) {
		long timestamp = plugin.getTimestamp();
		Date date = new Date(timestamp);
		SimpleDateFormat dateFormat = new SimpleDateFormat();
		String format = dateFormat.format(date);
		return format;
	}

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		// TODO Auto-generated method stub
		return null;
	}

}
