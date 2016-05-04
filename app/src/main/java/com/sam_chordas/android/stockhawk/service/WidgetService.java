package com.sam_chordas.android.stockhawk.service;

import android.content.Intent;
import android.widget.RemoteViewsService;

import com.sam_chordas.android.stockhawk.rest.WidgetDataProvider;

/**
 * Created by My on 5/2/2016.
 */
public class WidgetService extends RemoteViewsService {
   @Override
   public RemoteViewsFactory onGetViewFactory(Intent intent) {
      // return remote view factory
      return new WidgetDataProvider(this, intent);
   }
}
