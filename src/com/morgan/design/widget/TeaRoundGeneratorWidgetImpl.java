package com.morgan.design.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.morgan.design.R;
import com.morgan.design.activity.TeaRoundGeneratorDashboardActivity;
import com.morgan.design.helpers.Constants;

/**
 * @author James Edward Morgan
 */
public class TeaRoundGeneratorWidgetImpl extends AppWidgetProvider {

	@Override
	public void onUpdate(final Context ctxt, final AppWidgetManager mgr, final int[] appWidgetIds) {
		final ComponentName me = new ComponentName(ctxt, TeaRoundGeneratorWidgetImpl.class);
		mgr.updateAppWidget(me, buildUpdate(ctxt, appWidgetIds));
	}

	private RemoteViews buildUpdate(final Context ctxt, final int[] appWidgetIds) {
		final RemoteViews updateViews = new RemoteViews(ctxt.getPackageName(), R.layout.tearound_widget_layout);

		final Intent i = new Intent(ctxt, TeaRoundGeneratorDashboardActivity.class);

		i.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
		i.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
		i.putExtra(Constants.TEA_ROUND_WIDGET, true);

		final PendingIntent pi = PendingIntent.getActivity(ctxt, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);

		updateViews.setImageViewResource(R.id.tearound_widget_icon, R.drawable.teapot_widget_icon);
		updateViews.setOnClickPendingIntent(R.id.tearound_widget_icon, pi);

		return (updateViews);
	}
}
