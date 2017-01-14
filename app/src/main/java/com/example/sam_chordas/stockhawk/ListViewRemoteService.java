package com.example.sam_chordas.stockhawk;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by cars on 1/14/17.
 */

public class ListViewRemoteService extends RemoteViewsService {


  @Override
  public RemoteViewsFactory onGetViewFactory(Intent intent) {
    return (new ListProvider(this.getApplicationContext(), intent));
  }
}
