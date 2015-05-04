package com.avoscloud.demo.share.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.avos.avoscloud.AVUser;
import com.avoscloud.demo.share.App;
import com.avoscloud.demo.share.R;
import com.avoscloud.demo.share.StatusService;

import java.util.List;

/**
 * Created by lzw on 15/1/2.
 */
public class FollowListActivity extends BaseUserListActivity {
  public static final int TYPE_FOLLOWER = 0;
  public static final int TYPE_FOLLOWING = 1;
  public static final String TYPE = "type";
  public static final String USER_ID = "userId";

  int type;
  AVUser user;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    init();
    refresh();
  }

  private void init() {
    int type = getIntent().getIntExtra(TYPE, TYPE_FOLLOWER);
    String userId = getIntent().getStringExtra(USER_ID);
    this.type = type;
    this.user = App.lookupUser(userId);
    if (type == TYPE_FOLLOWER) {
      setTitle(R.string.status_followers);
    } else {
      setTitle(R.string.status_following);
    }
  }

  @Override
  protected List<AVUser> getUserList(int skip, int limit) throws Exception {
    if (type == TYPE_FOLLOWER) {
      return StatusService.getFollowers(user, skip, limit);
    } else {
      return StatusService.getFollowings(user, skip, limit);
    }
  }

  public static void go(Context context, int type, AVUser user) {
    Intent intent = new Intent(context, FollowListActivity.class);
    intent.putExtra(TYPE, type);
    intent.putExtra(USER_ID, user.getObjectId());
    context.startActivity(intent);
  }
}
