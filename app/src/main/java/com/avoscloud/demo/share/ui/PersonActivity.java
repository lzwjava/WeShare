package com.avoscloud.demo.share.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FollowCallback;
import com.avoscloud.demo.share.*;
import com.avoscloud.demo.share.ui.base.BaseListView;

import java.util.List;

public class PersonActivity extends Activity {
  public static final String USER_ID = "userId";
  public static final int CANCEL_FOLLOW = 0;
  public static final int FOLLOW = 1;

  @InjectView(R.id.profileLayout)
  PersonProfileLayout personProfileLayout;

  @InjectView(R.id.status_List)
  BaseListView<Status> statusList;

  @InjectView(R.id.followAction)
  Button followActionBtn;

  @InjectView(R.id.followStatus)
  TextView followStatusView;

  @InjectView(R.id.followLayout)
  View followLayout;

  int followStatus;

  AVUser user;

  boolean myself;

  int actionType = -1;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    // TODO Auto-generated method stub
    super.onCreate(savedInstanceState);
    setContentView(R.layout.status_person_layout);
    init();
    ButterKnife.inject(this);
    personProfileLayout.init(this, user);
    initList();
    statusList.onRefresh();
    refresh();
  }

  private void refresh() {
    new StatusNetAsyncTask(this) {
      @Override
      protected void doInBack() throws Exception {
        if (!myself) {
          followStatus = StatusService.followStatus(user);
        }
      }

      @Override
      protected void onPost(Exception e) {
        if (StatusUtils.filterException(PersonActivity.this, e)) {
          if (myself) {
            followStatusView.setVisibility(View.GONE);
            followLayout.setVisibility(View.GONE);
            followActionBtn.setVisibility(View.GONE);
            return;
          }
          followStatusView.setVisibility(View.VISIBLE);
          followActionBtn.setVisibility(View.VISIBLE);

          int followStatusDescId = R.string.status_none_follow_desc;
          switch (followStatus) {
            case StatusService.MUTUAL_FOLLOW:
              followStatusDescId = R.string.status_mutual_follow;
              break;
            case StatusService.FOLLOWER:
              followStatusDescId = R.string.status_follower_desc;
              break;
            case StatusService.FOLLOWING:
              followStatusDescId = R.string.status_following_desc;
              break;
            case StatusService.NONE_FOLLOW:
              followStatusDescId = R.string.status_none_follow_desc;
              break;
          }
          String followStatusDesc = getString(followStatusDescId);
          followStatusView.setText(followStatusDesc);

          int followButtonResId;
          if (followStatus == StatusService.MUTUAL_FOLLOW ||
              followStatus == StatusService.FOLLOWING) {
            actionType = CANCEL_FOLLOW;
            followButtonResId = R.string.status_cancelFollow;
          } else {
            actionType = FOLLOW;
            followButtonResId = R.string.status_follow;
          }

          followActionBtn.setText(getString(followButtonResId));
        }
      }
    }.execute();
  }

  private void initList() {
    statusList.init(new BaseListView.DataInterface<Status>() {
      public List<Status> getDatas(int skip, int limit, List<Status> currentDatas) throws Exception {
        return StatusService.getUserStatusList(user, skip, limit);
      }
    }, new StatusListAdapter(this));
    statusList.setToastIfEmpty(false);
  }

  private void init() {
    Intent intent = getIntent();
    String userId = intent.getStringExtra(USER_ID);
    user = App.lookupUser(userId);
    AVUser currentUser = AVUser.getCurrentUser();
    myself = user.getObjectId().equals(currentUser.getObjectId());
  }


  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    personProfileLayout.handleActivityResult(requestCode, resultCode, data);
  }

  public static void go(Context context, AVUser item) {
    Intent intent = new Intent(context, PersonActivity.class);
    intent.putExtra(PersonActivity.USER_ID, item.getObjectId());
    context.startActivity(intent);
  }

  @OnClick(R.id.followAction)
  void followAction() {
    if (actionType != -1) {
      if (myself) {
        return;
      }
      boolean follow;
      if (actionType == FOLLOW) {
        follow = true;
      } else {
        follow = false;
      }
      StatusService.followAction(user, follow, new FollowCallback() {

        @Override
        public void done(AVObject object, AVException e) {
          if (StatusUtils.filterException(PersonActivity.this, e)) {
            refresh();
          }
        }
      });
    }
  }
}
