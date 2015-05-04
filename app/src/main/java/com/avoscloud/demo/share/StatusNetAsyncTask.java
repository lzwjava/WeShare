package com.avoscloud.demo.share;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

public abstract class StatusNetAsyncTask extends AsyncTask<Void, Void, Void> {
  ProgressDialog dialog;
  protected Context ctx;
  boolean openDialog = true;
  Exception exception;

  protected StatusNetAsyncTask(Context ctx) {
    this.ctx = ctx;
  }

  protected StatusNetAsyncTask(Context ctx, boolean openDialog) {
    this.ctx = ctx;
    this.openDialog = openDialog;
  }

  public StatusNetAsyncTask setOpenDialog(boolean openDialog) {
    this.openDialog = openDialog;
    return this;
  }

  public ProgressDialog getDialog() {
    return dialog;
  }

  @Override
  protected void onPreExecute() {
    super.onPreExecute();
    if (openDialog) {
      dialog = StatusUtils.showSpinnerDialog((Activity) ctx);
    }
  }

  @Override
  protected Void doInBackground(Void... params) {
    try {
      doInBack();
    } catch (Exception e) {
      e.printStackTrace();
      exception = e;
    }
    return null;
  }

  @Override
  protected void onPostExecute(Void aVoid) {
    super.onPostExecute(aVoid);
    if (openDialog) {
      if (dialog.isShowing()) {
        dialog.dismiss();
      }
    }
    onPost(exception);
  }

  protected abstract void doInBack() throws Exception;

  protected abstract void onPost(Exception e);
}
