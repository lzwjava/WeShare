package com.avoscloud.demo.share.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.avos.avoscloud.AVStatus;
import com.avos.avoscloud.AVUser;
import com.avoscloud.demo.share.*;
import com.avoscloud.demo.share.ui.base.BaseActivity;
import com.avoscloud.demo.share.ui.base.BaseListView;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by lzw on 15/1/2.
 */
public class StatusListActivity extends BaseActivity {
  @InjectView(R.id.status_List)
  BaseListView<Status> statusList;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.status_list_layout);
    ButterKnife.inject(this);
    App.regiserUser(AVUser.getCurrentUser());
    initList();
    statusList.setToastIfEmpty(false);
    statusList.onRefresh();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return super.onCreateOptionsMenu(menu);
  }

  @Override
  public boolean onMenuItemSelected(int featureId, MenuItem item) {
    if (item.getItemId() == R.id.action_people) {
      Intent intent = new Intent(this, UserListActivity.class);
      startActivity(intent);
    } else if (item.getItemId() == R.id.logout) {
      AVUser.logOut();
      finish();
    }
    return super.onMenuItemSelected(featureId, item);
  }

  private void initList() {
    statusList.init(new BaseListView.DataInterface<Status>() {
      @Override
      public List<Status> getDatas(int skip, int limit, List<Status> currentDatas) throws Exception {
        long maxId;
        maxId = getMaxId(skip, currentDatas);
        if (maxId == 0) {
          return new ArrayList<>();
        } else {
          return StatusService.getStatusDatas(maxId, limit);
        }
      }

      @Override
      public void onItemLongPressed(final Status item) {
        AVStatus innerStatus = item.getInnerStatus();
        AVUser source = innerStatus.getSource();
        if (source.getObjectId().equals(AVUser.getCurrentUser().getObjectId())) {
          AlertDialog.Builder builder = new AlertDialog.Builder(StatusListActivity.this);
          builder.setMessage(R.string.status_deleteStatusTips).setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
              new StatusNetAsyncTask(StatusListActivity.this) {
                @Override
                protected void doInBack() throws Exception {
                  StatusService.deleteStatus(item);
                }

                @Override
                protected void onPost(Exception e) {
                  if (e != null) {
                    StatusUtils.toast(StatusListActivity.this, e.getMessage());
                  } else {
                    statusList.onRefresh();
                  }
                }
              }.execute();
            }
          }).setNegativeButton(R.string.cancel, null);
          builder.show();
        }
      }
    }, new StatusListAdapter(StatusListActivity.this));
  }

  public static long getMaxId(int skip, List<Status> currentDatas) {
    long maxId;
    if (skip == 0) {
      maxId = Long.MAX_VALUE;
    } else {
      AVStatus lastStatus = currentDatas.get(currentDatas.size() - 1).getInnerStatus();
      maxId = lastStatus.getMessageId() - 1;
    }
    return maxId;
  }

  @OnClick(R.id.followers)
  void goFollowers() {
    AVUser currentUser = AVUser.getCurrentUser();
    FollowListActivity.go(StatusListActivity.this,
        FollowListActivity.TYPE_FOLLOWER,
        currentUser);
  }

  @OnClick(R.id.following)
  void goFollowing() {
    FollowListActivity.go(StatusListActivity.this,
        FollowListActivity.TYPE_FOLLOWING,
        AVUser.getCurrentUser());
  }

  @OnClick(R.id.send)
  void goSend() {
    StatusUtils.goActivity(StatusListActivity.this, StatusSendActivity.class);
  }
}
