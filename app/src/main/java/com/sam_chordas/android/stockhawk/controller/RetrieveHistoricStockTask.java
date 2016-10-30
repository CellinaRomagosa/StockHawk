package com.sam_chordas.android.stockhawk.controller;

import android.os.AsyncTask;
import android.util.Log;

import com.sam_chordas.android.stockhawk.data.Quote;
import com.sam_chordas.android.stockhawk.ui.GraphStock;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by cars on 10/30/16.
 */

public class RetrieveHistoricStockTask extends AsyncTask<String, Boolean, Void> {


  private List<Quote> quoteList;
  WeakReference<GraphStock> activity;

  public RetrieveHistoricStockTask(WeakReference<GraphStock> activity){
    this.activity = activity;
  }

  private String initQuery(String stock){
    StringBuilder urlStringBuilder = new StringBuilder();
    try{
      // Base URL for the Yahoo query
      urlStringBuilder.append("https://query.yahooapis.com/v1/public/yql?q=");
      urlStringBuilder.append(URLEncoder.encode("select * from yahoo.finance.historicaldata where  symbol = "
          + "\"" + stock + "\" " + "and startDate= \"2016-01-01\" and endDate=\""
           , "UTF-8"));

      Date now = new Date();
      DateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd");

      urlStringBuilder.append(URLEncoder.encode(dateFormat.format(now) + "\"","UTF-8"));

      // finalize the URL for the API query.
      urlStringBuilder.append("&format=json&diagnostics=true&env=store%3A%2F%2Fdatatables."
          + "org%2Falltableswithkeys&callback=");
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    finally {
      return urlStringBuilder.toString();
    }

  }

  private List<Quote> fetchData(String url) {
    Log.d("tag", url);
    String bodyResponse = "";
    OkHttpClient client = new OkHttpClient();
    List<Quote> quoteList = null;

    Request request = new Request.Builder()
        .url(url)
        .build();

    Response response = null;
    try {
      response = client.newCall(request).execute();
      bodyResponse = response.body().string();
      quoteList = parseJson(new JSONObject(bodyResponse));

    } catch (IOException e) {
      e.printStackTrace();
    }
    finally {
      return quoteList;
    }
  }


  private ArrayList<Quote> parseJson(JSONObject json){
    ArrayList<Quote> quotes = null;
    try {
      JSONObject object1 = json.getJSONObject("query");
      int size = object1.getInt("count");
      quotes = new ArrayList<Quote>(size);
      JSONArray array =  object1.getJSONObject("results").getJSONArray("quote");
      for ( int i=0 ; i< size ; i++){
        Quote aux = new Quote();
        JSONObject object = array.getJSONObject(i);
        aux.setOpen(object.getInt("Open"));
        aux.setClose(object.getInt("Close"));
        aux.setDate(object.getString("Date"));
        quotes.add(aux);
      }


    } catch (JSONException e) {
      e.printStackTrace();
    }
    return  quotes;
  }


  @Override
  protected Void doInBackground(String... params) {
     quoteList = fetchData ( initQuery(params[0]));

    return null;
  }

  @Override
  protected void onPostExecute(Void aVoid) {
    super.onPostExecute(aVoid);

    activity.get().fillChart(quoteList);
    // update UI
  }
}
