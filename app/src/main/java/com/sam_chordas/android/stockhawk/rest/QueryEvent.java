package com.sam_chordas.android.stockhawk.rest;

/**
 * Created by My on 4/13/2016.
 */
// this class is an Event published by EventBus, to communicate from StockTaskService to MyStocksActivity
public class QueryEvent {
   public String mMessage;

   public QueryEvent(String message) {
      mMessage = message;
   }
}
