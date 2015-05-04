package com.avoscloud.demo.share.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import com.avoscloud.demo.share.R;
import com.avoscloud.demo.share.StatusUtils;
import com.avoscloud.demo.share.ui.base.BaseActivity;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by lzw on 14-9-21.
 */
public class ImageBrowserActivity extends BaseActivity {
  String url;
  ImageView imageView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.image_brower_layout);
    imageView = (ImageView) findViewById(R.id.imageView);
    Intent intent = getIntent();
    url = intent.getStringExtra("url");
    ImageLoader.getInstance().displayImage(url, imageView, StatusUtils.normalImageOptions);
  }
}
