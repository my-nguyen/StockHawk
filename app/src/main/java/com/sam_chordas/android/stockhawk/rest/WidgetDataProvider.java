package com.sam_chordas.android.stockhawk.rest;

import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Build;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.data.QuoteColumns;
import com.sam_chordas.android.stockhawk.data.QuoteProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by My on 5/2/2016.
 */
// this class acts like an Adapter for the ListView in the widget
public class WidgetDataProvider implements RemoteViewsService.RemoteViewsFactory, Loader.OnLoadCompleteListener<Cursor> {
   List<String> mCollection = new ArrayList<>();
   Context mContext;
   Intent mIntent;
   CursorLoader mCursorLoader;
   Cursor mCursor;
   static final int CURSOR_LOADER_ID = 1;

   private void initialize() {
      mCollection.clear();
      for (int i = 1; i < 10; i++)
         mCollection.add("ListView Item " + i);
      // create, register, and start loading CursorLoader
      mCursorLoader = new CursorLoader(mContext, QuoteProvider.Quotes.CONTENT_URI,
            new String[]{ QuoteColumns._ID, QuoteColumns.SYMBOL, QuoteColumns.BIDPRICE,
                  QuoteColumns.PERCENT_CHANGE, QuoteColumns.CHANGE, QuoteColumns.ISUP},
            QuoteColumns.ISCURRENT + " = ?",
            new String[]{"1"},
            null);
      mCursorLoader.registerListener(CURSOR_LOADER_ID, this);
      mCursorLoader.startLoading();
   }

   public WidgetDataProvider(Context context, Intent intent) {
      mContext = context;
      mIntent = intent;
   }

   @Override
   public void onCreate() {
      initialize();
   }

   @Override
   public void onDataSetChanged() {
      initialize();
   }

   @Override
   public void onDestroy() {
      // clean up CursorLoader when this service is destroyed
      if (mCursorLoader != null) {
         mCursorLoader.unregisterListener(this);
         mCursorLoader.cancelLoad();
         mCursorLoader.stopLoading();
      }
   }

   @Override
   public int getCount() {
      return mCollection.size();
   }

   @Override
   public RemoteViews getViewAt(int position) {
      /*
      RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), android.R.layout.simple_list_item_1);
      remoteViews.setTextViewText(android.R.id.text1, mCollection.get(position));
      // remoteViews.setTextColor(android.R.id.text1, Color.BLACK);
      */
      RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.stock_widget_item);
      if (mCursor != null && mCursor.moveToPosition(position)) {
         remoteViews.setTextViewText(R.id.stock_symbol, mCursor.getString(mCursor.getColumnIndex("symbol")));
         remoteViews.setTextViewText(R.id.bid_price, mCursor.getString(mCursor.getColumnIndex("bid_price")));
         remoteViews.setTextViewText(R.id.change, mCursor.getString(mCursor.getColumnIndex("change")));
         /*
         int sdk = Build.VERSION.SDK_INT;
         if (cursor.getInt(cursor.getColumnIndex("is_up")) == 1){
            if (sdk < Build.VERSION_CODES.JELLY_BEAN){
               viewHolder.change.setBackgroundDrawable(
                     mContext.getResources().getDrawable(R.drawable.percent_change_pill_green));
            }else {
               viewHolder.change.setBackground(
                     mContext.getResources().getDrawable(R.drawable.percent_change_pill_green));
            }
         } else{
            if (sdk < Build.VERSION_CODES.JELLY_BEAN) {
               viewHolder.change.setBackgroundDrawable(
                     mContext.getResources().getDrawable(R.drawable.percent_change_pill_red));
            } else{
               viewHolder.change.setBackground(
                     mContext.getResources().getDrawable(R.drawable.percent_change_pill_red));
            }
         }
         if (Utils.showPercent){
            viewHolder.change.setText(cursor.getString(cursor.getColumnIndex("percent_change")));
         } else{
            viewHolder.change.setText(cursor.getString(cursor.getColumnIndex("change")));
         }
         */
      }
      /*
      RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.stock_widget_item);
      remoteViews.setTextViewText(R.id.stock_symbol, "NGUY");
      remoteViews.setTextViewText(R.id.bid_price, "220.00");
      remoteViews.setTextViewText(R.id.change, "-5%");
      */
      return remoteViews;
   }

   @Override
   public RemoteViews getLoadingView() {
      return null;
   }

   @Override
   public int getViewTypeCount() {
      return 1;
   }

   @Override
   public long getItemId(int position) {
      return position;
   }

   @Override
   public boolean hasStableIds() {
      return true;
   }

   @Override
   public void onLoadComplete(Loader<Cursor> loader, Cursor data) {
      // save the cursor upon load completion
      mCursor = data;
   }
}
