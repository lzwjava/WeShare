package com.avoscloud.demo.share.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.avos.avoscloud.*;
import com.avoscloud.demo.share.R;
import com.avoscloud.demo.share.ui.base.BaseActivity;

public class LoginActivity extends BaseActivity {
  @InjectView(com.avoscloud.demo.share.R.id.usernameEditText)
  EditText usernameEditText;
  @InjectView(R.id.passwordEditText)
  EditText passwordEditText;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.login_activity);
    ButterKnife.inject(this);
    AVAnalytics.trackAppOpened(getIntent());
    usernameEditText.setText("lzwjava");
    passwordEditText.setText("lzwjava");

    if (AVUser.getCurrentUser() != null) {
      onSucceed();
    }
  }

  void onSucceed() {
    Intent intent = new Intent(LoginActivity.this, StatusListActivity.class);
    startActivity(intent);
    finish();
  }

  @OnClick(R.id.login)
  void login() {
    String username = usernameEditText.getText().toString();
    String password = passwordEditText.getText().toString();
    if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {
      AVUser.logInInBackground(username, password, new LogInCallback<AVUser>() {
        @Override
        public void done(AVUser avUser, AVException e) {
          if (filterException(e)) {
            onSucceed();
          }
        }
      });
    }
  }

  @OnClick(R.id.register)
  void register() {
    String username = usernameEditText.getText().toString();
    String password = passwordEditText.getText().toString();
    if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {
      AVUser user = new AVUser();
      user.setUsername(username);
      user.setPassword(password);
      user.saveInBackground(new SaveCallback() {
        @Override
        public void done(AVException e) {
          if (filterException(e)) {
            onSucceed();
          }
        }
      });
    }
  }
}