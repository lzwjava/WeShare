package com.avoscloud.demo.share.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVUser;
import com.avoscloud.demo.share.R;
import com.avoscloud.demo.share.StatusNetAsyncTask;
import com.avoscloud.demo.share.StatusUtils;

import java.io.ByteArrayOutputStream;

/**
 * Created by lzw on 15/1/1.
 */
public class PersonProfileLayout extends LinearLayout implements View.OnClickListener {
  private static final int IMAGE_PICK_REQUEST = 0;
  private static final int CROP_REQUEST = 1;
  Activity activity;
  AVUser user;

  public void init(Activity activity, AVUser user) {
    this.activity = activity;
    this.user = user;

    ButterKnife.inject(this, this);
    bindViewToOnClickListener();
    setPersonalInfoView();
  }

  @InjectView(R.id.profileLayout)
  View profileLayout;

  @InjectView(R.id.name)
  TextView nameView;

  @InjectView(R.id.avatar)
  ImageView avatarView;

  @InjectView(R.id.editHint)
  View editHint;

  public PersonProfileLayout(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  private void bindViewToOnClickListener() {
    avatarView.setOnClickListener(this);
    profileLayout.setOnClickListener(this);
  }

  boolean isCurrentUser() {
    AVUser currentUser = AVUser.getCurrentUser();
    return user.getObjectId().equals(currentUser.getObjectId());
  }

  private void setPersonalInfoView() {
    StatusUtils.displayAvatar(user, avatarView);
    nameView.setText(user.getUsername());
    if (isCurrentUser()) {
      editHint.setVisibility(View.VISIBLE);
    } else {
      editHint.setVisibility(View.GONE);
    }
  }

  @Override
  public void onClick(View v) {
    int id = v.getId();
    if (isCurrentUser() == false) {
      return;
    }
    if (id == R.id.avatar) {
      StatusUtils.pickImage(activity, IMAGE_PICK_REQUEST);
    }
  }

  public String getCachePath() {
    return getCacheDir() + "tmp";
  }

  public void handleActivityResult(int requestCode, int resultCode, Intent data) {
    if (resultCode == Activity.RESULT_OK) {
      if (requestCode == IMAGE_PICK_REQUEST) {
        Uri uri = data.getData();
        StatusUtils.startAvatarCrop(activity, uri, 200, 200, CROP_REQUEST, getCachePath());
      } else if (requestCode == CROP_REQUEST) {
        final Bitmap bitmap = data.getExtras().getParcelable("data");
        new StatusNetAsyncTask(getContext()) {
          @Override
          protected void doInBack() throws Exception {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
            byte[] bytes = stream.toByteArray();
            final AVFile file = new AVFile("file", bytes);
            file.save();
            user.put("avatar", file);
            user.save();
          }

          @Override
          protected void onPost(Exception e) {
            if (StatusUtils.filterException(activity, e)) {
              setPersonalInfoView();
            }
          }
        }.execute();

      }
    }
  }

  public String getCacheDir() {
    return getContext().getCacheDir().getAbsolutePath() + "/";
  }
}
