package com.avoscloud.demo.share;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.Toast;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVUser;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by lzw on 15/5/4.
 */
public class StatusUtils {
  public static ProgressDialog showSpinnerDialog(Activity activity) {
    ProgressDialog dialog = new ProgressDialog(activity);
    dialog.setMessage(activity.getString(R.string.hardLoading));
    dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    dialog.setCancelable(true);
    dialog.show();
    return dialog;
  }

  public static void toast(Context context, String str) {
    Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
  }

  public static boolean filterException(Context ctx, Exception e) {
    if (e != null) {
      toast(ctx, e.getMessage());
      return false;
    } else {
      return true;
    }
  }

  public static void goActivity(Context cxt, Class<?> clz) {
    Intent intent = new Intent(cxt, clz);
    cxt.startActivity(intent);
  }


  public static Uri startAvatarCrop(Activity activity, Uri uri, int outputX, int outputY,
                                    int requestCode, String outputPath) {
    Intent intent = null;
    intent = new Intent("com.android.camera.action.CROP");
    intent.setDataAndType(uri, "image/*");
    intent.putExtra("crop", "true");
    intent.putExtra("aspectX", 1);
    intent.putExtra("aspectY", 1);
    intent.putExtra("outputX", outputX);
    intent.putExtra("outputY", outputY);
    intent.putExtra("scale", true);
//    Uri outputUri = Uri.fromFile(new File(outputPath));
//    intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
    intent.putExtra("return-data", true);
    intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
    intent.putExtra("noFaceDetection", false);
    activity.startActivityForResult(intent, requestCode);
    return null;
  }

  public static void pickImage(Activity activity, int requestCode) {
    Intent intent = new Intent(Intent.ACTION_PICK, null);
    intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
    activity.startActivityForResult(intent, requestCode);
  }

  public static void toast(Context context, int strId) {
    toast(context, context.getString(strId));
  }

  static Map<String, Integer> userId2randomAvatar = new HashMap<String, Integer>();

  public synchronized static void displayAvatar(AVUser user, ImageView avatarView) {
    AVFile file = user.getAVFile("avatar");
    if (file != null) {
      String url = file.getUrl();
      ImageLoader.getInstance().displayImage(url, avatarView, avatarImageOptions);
    } else {
      int avatarIds[] = new int[]{R.drawable.status_avatar0, R.drawable.status_avatar1,
          R.drawable.status_avatar3, R.drawable.status_avatar4,
          R.drawable.status_avatar5, R.drawable.status_avatar6};
      String id = user.getObjectId();
      if (userId2randomAvatar.get(id) == null) {
        userId2randomAvatar.put(id, new Random().nextInt(avatarIds.length));
      }
      int randomN = userId2randomAvatar.get(id);
      int avatarId = avatarIds[randomN];
      avatarView.setImageResource(avatarId);
    }
  }

  public static DisplayImageOptions avatarImageOptions = new DisplayImageOptions.Builder()
      .showImageOnLoading(R.drawable.default_user_avatar)
      .showImageForEmptyUri(R.drawable.default_user_avatar)
      .showImageOnFail(R.drawable.default_user_avatar)
      .cacheInMemory(true)
      .cacheOnDisc(true)
      .considerExifParams(true)
      .imageScaleType(ImageScaleType.EXACTLY)
      .bitmapConfig(Bitmap.Config.RGB_565)
      .resetViewBeforeLoading(true)// 设置图片在下载前是否重置，复位
          //.displayer(new RoundedBitmapDisplayer(20))
          //.displayer(new FadeInBitmapDisplayer(100))// 淡入
      .build();

  public static ImageLoaderConfiguration getImageLoaderConfig(Context context, File cacheDir) {
    return new ImageLoaderConfiguration.Builder(
        context)
        .threadPoolSize(3).threadPriority(Thread.NORM_PRIORITY - 2)
        .memoryCache(new WeakMemoryCache())
        .denyCacheImageMultipleSizesInMemory()
        .discCacheFileNameGenerator(new Md5FileNameGenerator())
        .tasksProcessingOrder(QueueProcessingType.LIFO)
        .discCache(new UnlimitedDiscCache(cacheDir))// 自定义缓存路径
            // .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
            //.writeDebugLogs() // Remove for release app
        .build();
  }


  public static DisplayImageOptions normalImageOptions = new DisplayImageOptions.Builder()
      .showImageOnLoading(R.drawable.empty_photo)
      .showImageForEmptyUri(R.drawable.empty_photo)
      .showImageOnFail(R.drawable.image_load_fail)
      .cacheInMemory(true)
      .cacheOnDisc(true)
      .considerExifParams(true)
      .imageScaleType(ImageScaleType.EXACTLY)
      .bitmapConfig(Bitmap.Config.RGB_565)
      .resetViewBeforeLoading(true)// 设置图片在下载前是否重置，复位
          //.displayer(new RoundedBitmapDisplayer(20))
          //.displayer(new FadeInBitmapDisplayer(100))// 淡入
      .build();

}
