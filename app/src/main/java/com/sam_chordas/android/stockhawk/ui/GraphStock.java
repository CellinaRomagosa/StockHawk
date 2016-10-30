package com.sam_chordas.android.stockhawk.ui;

import android.app.Activity;
import android.os.Bundle;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.controller.RetrieveHistoricStockTask;
import com.sam_chordas.android.stockhawk.data.Quote;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class GraphStock extends Activity {

  private LineChart chart;
  public static final String STOCK_KEY = "STOCK_KEY";
  private List<Quote> quotes;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_graph_stock);
    chart = (LineChart) findViewById(R.id.chart);
    initializeChart();

  }

  private void initializeChart(){
    // todo retrieve data and put it in the chart

    String stock = getIntent().getExtras().getString(STOCK_KEY,"");
    RetrieveHistoricStockTask task = new RetrieveHistoricStockTask( new WeakReference<GraphStock>(this));
    task.execute(stock);


  }

  public void fillChart(List<Quote> quotes){
    this.quotes = quotes;
    List<Entry> entries = new ArrayList<Entry>();

    int i=0;
    for (Quote data : quotes) {
      entries.add(new Entry(i, data.getClose()));
      i++;
    }
    LineDataSet dataSet = new LineDataSet(entries, "Days");
    dataSet.setColor(R.color.material_blue_500);
    dataSet.setValueTextColor(R.color.material_blue_600);

    LineData lineData = new LineData(dataSet);
    chart.setData(lineData);
    chart.invalidate(); // refresh
  }

}
