package com.avoscloud.demo.share.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.SaveCallback;
import com.avoscloud.demo.share.R;
import com.avoscloud.demo.share.StatusService;
import com.avoscloud.demo.share.StatusUtils;

import java.io.IOException;

/**
 * Created by lzw on 15/1/2.
 */
public class StatusSendActivity extends Activity {
  private static final int IMAGE_PICK_REQUEST = 0;
  @InjectView(R.id.editText)
  EditText editText;
  Context context;
  @InjectView(R.id.image)
  ImageView imageView;

  @InjectView(R.id.imageAction)
  Button imageAction;
  boolean haveImage = false;
  Bitmap bitmap;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    context = this;
    setContentView(R.layout.status_send_layout);
    ButterKnife.inject(this);
    setButtonAndImage();
  }

  void setButtonAndImage() {
    imageView.setImageBitmap(bitmap);
    if (haveImage) {
      imageAction.setText(R.string.status_cancelImage);
      imageView.setVisibility(View.VISIBLE);
    } else {
      imageAction.setText(R.string.status_addImage);
      imageView.setVisibility(View.INVISIBLE);
    }
  }

  @OnClick(R.id.send)
  void send() {
    String text = editText.getText().toString();
    if (TextUtils.isEmpty(text) == false || bitmap != null) {
      final ProgressDialog dialog = StatusUtils.showSpinnerDialog(this);
      StatusService.sendStatus(text, bitmap, new SaveCallback() {
        @Override
        public void done(AVException e) {
          dialog.dismiss();
          if (StatusUtils.filterException(context, e)) {
            finish();
          }
        }
      });
    }
  }

  public static void pickImage(Activity activity, int requestCode) {
    Intent intent = new Intent(Intent.ACTION_PICK, null);
    intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
    activity.startActivityForResult(intent, requestCode);
  }

  @OnClick(R.id.imageAction)
  void imageAction() {
    if (haveImage == false) {
      pickImage(this, IMAGE_PICK_REQUEST);
    } else {
      bitmap = null;
      haveImage = false;
      setButtonAndImage();
    }
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == Activity.RESULT_OK) {
      if (requestCode == IMAGE_PICK_REQUEST) {
        Uri uri = data.getData();
        try {
          bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
          haveImage = true;
          setButtonAndImage();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }
}
