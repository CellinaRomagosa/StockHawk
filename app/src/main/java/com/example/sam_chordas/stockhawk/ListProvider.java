package com.example.sam_chordas.stockhawk;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.data.QuoteColumns;
import com.sam_chordas.android.stockhawk.data.QuoteProvider;

import static com.sam_chordas.android.stockhawk.data.QuoteColumns.BIDPRICE;
import static com.sam_chordas.android.stockhawk.data.QuoteColumns.SYMBOL;

/**
 * Created by cars on 1/14/17.
 */

public class ListProvider implements RemoteViewsService.RemoteViewsFactory {
  private Context context = null;
  private int appWidgetId;
  private Cursor cursor;

  public ListProvider(Context context, Intent intent) {
    this.context = context;
    Log.d(ListProvider.class.getName(), "constructor list provider ");
  }



  @Override
  public int getCount() {
    return cursor.getCount();
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  /*
   *Similar to getView of Adapter where instead of View
   *we return RemoteViews
   *
   */
  @Override
  public RemoteViews getViewAt(int position) {
    Log.d(ListProvider.class.getName(), "get view at " + position);

    final RemoteViews remoteView = new RemoteViews(
        context.getPackageName(), R.layout.list_item_quote);

    if  (cursor.moveToPosition(position)){
      remoteView.setTextViewText(R.id.bid_price,cursor.getString((cursor.getColumnIndex(BIDPRICE))));
      remoteView.setTextViewText(R.id.stock_symbol,cursor.getString((cursor.getColumnIndex(SYMBOL))));
    }


    return remoteView;
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
  public boolean hasStableIds() {
    return true;
  }

  @Override
  public void onCreate() {
  }

  @Override
  public void onDataSetChanged() {
    if (cursor != null) {
      cursor.close();
    }
    cursor = context.getContentResolver().query(QuoteProvider.Quotes.CONTENT_URI,
        new String[]{QuoteColumns._ID, SYMBOL, QuoteColumns.BIDPRICE,
            QuoteColumns.PERCENT_CHANGE, QuoteColumns.CHANGE, QuoteColumns.ISUP},
        QuoteColumns.ISCURRENT + " = ?",
        new String[]{"1"},
        null);
  }

  @Override
  public void onDestroy() {
    if ( cursor != null){
      cursor.close();
    }
  }

}
