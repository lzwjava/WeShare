package com.avoscloud.demo.share.ui;

import android.app.Activity;
import android.os.Bundle;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.avos.avoscloud.AVUser;
import com.avoscloud.demo.share.App;
import com.avoscloud.demo.share.R;
import com.avoscloud.demo.share.ui.base.BaseListView;
import com.avoscloud.demo.share.ui.base.UserAdapter;

import java.util.List;

/**
 * Created by lzw on 15/1/2.
 */
public abstract class BaseUserListActivity extends Activity {

  @InjectView(R.id.userList)
  protected BaseListView<AVUser> userList;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.status_users_layout);
    ButterKnife.inject(this);
    initList();
  }

  protected void refresh() {
    userList.onRefresh();
  }

  private void initList() {
    userList.init(new BaseListView.DataInterface<AVUser>() {

      @Override
      public List<AVUser> getDatas(int skip, int limit, List<AVUser> currentDatas) throws Exception {
        List<AVUser> users = getUserList(skip, limit);
        App.registerBatchUser(users);
        return users;
      }

      @Override
      public void onItemSelected(AVUser item) {
        PersonActivity.go(BaseUserListActivity.this, item);
      }
    }, new UserAdapter(this));
  }

  protected abstract List<AVUser> getUserList(int skip, int limit) throws Exception;
}
