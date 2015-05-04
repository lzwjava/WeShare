package com.avoscloud.demo.share;

import android.app.Application;
import android.content.Context;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVUser;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lzw on 15/5/4.
 */
public class App extends Application {
  public static final String LIKES = "likes";
  public static final String STATUS_DETAIL = "StatusDetail";
  public static final String DETAIL_ID = "detailId";
  public static final String CREATED_AT = "createdAt";
  public static final String FOLLOWER = "follower";
  public static final String FOLLOWEE = "followee";

  @Override
  public void onCreate() {
    super.onCreate();
    AVOSCloud.initialize(this, "jk3o7cfw4zca1ymb1e2xbd0g1lv4kb4wf48igyvbwgkgppj5",
        "zuqhifufztkw9fb2wchrqc56mnjmxw4r363cm9xpoejj67rj");
    AVOSCloud.setDebugLogEnabled(true);
    initImageLoader(this);
  }

  public static void initImageLoader(Context context) {
    File cacheDir = StorageUtils.getOwnCacheDirectory(context, "");
    ImageLoaderConfiguration config = StatusUtils.getImageLoaderConfig(context, cacheDir);
    ImageLoader.getInstance().init(config);
  }

  public static Map<String, AVUser> userCache = new HashMap<>();

  public static void regiserUser(AVUser user) {
    userCache.put(user.getObjectId(), user);
  }

  public static void registerBatchUser(List<AVUser> users) {
    for (AVUser user : users) {
      regiserUser(user);
    }
  }

  public static AVUser lookupUser(String userId) {
    return userCache.get(userId);
  }
}
