package com.avoscloud.demo.share.ui.base;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.avos.avoscloud.AVUser;
import com.avoscloud.demo.share.R;
import com.avoscloud.demo.share.StatusUtils;

/**
 * Created by lzw on 15/5/4.
 */
public class UserAdapter extends BaseListAdapter<AVUser> {

  public UserAdapter(Context ctx) {
    super(ctx);
  }

  @Override
  public View getView(int position, View conView, ViewGroup parent) {
    if (conView == null) {
      LayoutInflater inflater = LayoutInflater.from(ctx);
      conView = inflater.inflate(R.layout.user_row, null, false);
    }
    ImageView avatarView = ViewHolder.findViewById(conView, R.id.avatarView);
    TextView nameView = ViewHolder.findViewById(conView, R.id.nameView);

    AVUser user = datas.get(position);
    StatusUtils.displayAvatar(user, avatarView);
    nameView.setText(user.getUsername());
    return conView;
  }
}
