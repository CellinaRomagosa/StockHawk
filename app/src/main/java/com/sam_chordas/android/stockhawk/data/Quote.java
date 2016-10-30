package com.sam_chordas.android.stockhawk.data;

/**
 * Created by cars on 10/30/16.
 */

public class Quote {
  private String date;
  private int open;
  private int close;


  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public int getOpen() {
    return open;
  }

  public void setOpen(int open) {
    this.open = open;
  }

  public int getClose() {
    return close;
  }

  public void setClose(int close) {
    this.close = close;
  }
}
