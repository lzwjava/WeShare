package com.avoscloud.demo.share.ui;

import android.os.Bundle;
import com.avos.avoscloud.AVUser;
import com.avoscloud.demo.share.StatusService;

import java.util.List;

/**
 * Created by lzw on 15/5/4.
 */
public class UserListActivity extends BaseUserListActivity {
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    userList.onRefresh();
  }

  @Override
  protected List<AVUser> getUserList(int skip, int limit) throws Exception {
    return StatusService.findUsers(skip, limit);
  }

}
