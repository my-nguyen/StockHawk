package com.sam_chordas.android.stockhawk.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.sam_chordas.android.stockhawk.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by My on 5/11/2016.
 */
public class StockChartActivity extends AppCompatActivity {

   @Override
   protected void onCreate(@Nullable Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_stock_chart);
      final LineChart lineChart = (LineChart)findViewById(R.id.chart);

      /// OkHttp usage is based on CodePath guide
      // instantiate OkHttpClient singleton
      OkHttpClient client = new OkHttpClient();
      // create Request object
      String base = "https://query.yahooapis.com/v1/public/yql";
      final String symbol = getIntent().getStringExtra("SYMBOL");
      /// Yahoo historical data stock query is based on https://github.com/swatiag1101/LineGraph
      String query = "select * from yahoo.finance.historicaldata where symbol ='" + symbol
            + "' and startDate = '" + lastMonth() + "' and endDate = '" + yesterday() + "'";
      Uri uri = Uri.parse(base).buildUpon()
            .appendQueryParameter("q", query)
            .appendQueryParameter("format", "json")
            // .appendQueryParameter("diagnostics", "true")
            .appendQueryParameter("env", "store://datatables.org/alltableswithkeys")
            // .appendQueryParameter("callback", "")
            .build();
      String url = uri.toString();
      Log.d("NGUYEN", "StockChartActivity, url: " + url);
      final Request request = new Request.Builder().url(url).build();
      // make asynchronous network call
      client.newCall(request).enqueue(new Callback() {
         @Override
         public void onFailure(Call call, IOException e) {
            e.printStackTrace();
         }

         @Override
         public void onResponse(Call call, Response response) throws IOException {
            // check for failure
            if (!response.isSuccessful())
               throw new IOException("Unexpected code " + response);
            // read data on the worker thread
            final String jsonString = response.body().string();
            // run view-related code back on the main thread
            StockChartActivity.this.runOnUiThread(new Runnable() {
               @Override
               public void run() {
                  try {
                     /// MPChart usage is based on
                     /// https://www.numetriclabz.com/android-line-chart-using-mpandroidchart-tutorial/
                     // list of y-values
                     List<Entry> entries = new ArrayList<>();
                     // x-axis labels
                     List<String> labels = new ArrayList<>();
                     JSONArray jsonArray = new JSONObject(jsonString).getJSONObject("query").getJSONObject("results").getJSONArray("quote");
                     // the jsonArray is in reverse chronological order
                     int j = 0;
                     for (int i = jsonArray.length()-1; i >= 0; i--) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        // JSON field "Adj_Close" (adjusted close) is chosen instead of field "Close"
                        float adjustedClose = Float.parseFloat(jsonObject.getString("Adj_Close"));
                        Entry entry = new Entry(adjustedClose, j);
                        entries.add(entry);
                        String date = jsonObject.getString("Date");
                        labels.add(date);
                        j++;
                     }
                     // the description at the bottom left of the chart
                     LineDataSet dataSet = new LineDataSet(entries, symbol + " values over the past thirty days");
                     LineData lineData = new LineData(labels, dataSet);
                     // the description at the bottom right of the chart, in smaller font size, and
                     // situated higher than dataSet description
                     lineChart.setDescription("Mother-Father!!!");
                     lineChart.setData(lineData);
                     // without animateY(), the chart won't show!
                     lineChart.animateY(1000);
                  } catch (JSONException e) {
                     e.printStackTrace();
                  }
               }
            });
         }
      });
   }

   public static Intent newIntent(Context context, String symbol) {
      Intent intent = new Intent(context, StockChartActivity.class);
      intent.putExtra("SYMBOL", symbol);
      return intent;
   }

   private String yesterday() {
      return getDate(Calendar.DATE);
   }

   private String lastMonth() {
      return getDate(Calendar.MONTH);
   }

   private String getDate(int field) {
      Calendar calendar = Calendar.getInstance();
      calendar.add(field, -1);
      DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
      return format.format(calendar.getTime());
   }
}
