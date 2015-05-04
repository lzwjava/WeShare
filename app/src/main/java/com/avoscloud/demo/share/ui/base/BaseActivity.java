package com.avoscloud.demo.share.ui.base;

import android.app.Activity;
import android.widget.Toast;

/**
 * Created by lzw on 15/5/4.
 */
public class BaseActivity extends Activity {
  protected void toast(String s) {
    Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
  }

  protected boolean filterException(Exception e) {
    if (e != null) {
      toast(e.getMessage());
      return false;
    } else {
      return true;
    }
  }
}
