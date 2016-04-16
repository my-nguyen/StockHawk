package com.sam_chordas.android.stockhawk.ui;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import com.sam_chordas.android.stockhawk.R;

/**
 * Created by My on 4/15/2016.
 */
public class StockWidgetProvider extends AppWidgetProvider {
   @Override
   public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
      Log.d("NGUYEN", "appWidgetIds size: " + appWidgetIds.length);
      // ComponentName component = new ComponentName(context, AppWidgetProvider.class);
      // int[] widgetIds = appWidgetManager.getAppWidgetIds(component);
      for (int widgetId : appWidgetIds) {
         // get the layout for the app widget
         RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.stock_widget);
         remoteViews.setTextViewText(R.id.stock_symbol, "NGUY");
         // create an Intent to launch StockWidgetProvider
         Intent intent = new Intent(context, StockWidgetProvider.class);
         intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
         intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
         PendingIntent pending = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
         // attach an on-click listener to the TextView
         remoteViews.setOnClickPendingIntent(R.id.stock_symbol, pending);
         // tell the AppWidgetManager to perform an update on the current app widget
         appWidgetManager.updateAppWidget(widgetId, remoteViews);
      }
   }
}
