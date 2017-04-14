package com.tehvilla.apps.tehvilla.helpers;

import android.app.Application;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;

import com.onesignal.OneSignal;
import com.tehvilla.apps.tehvilla.R;
import com.bumptech.glide.Glide;
import com.crashlytics.android.Crashlytics;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.materialdrawer.util.AbstractDrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerUIUtils;
import com.pixplicity.easyprefs.library.Prefs;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import io.fabric.sdk.android.Fabric;
import io.realm.Realm;

/**
 * Created by Naufal on 26/12/2016.
 */
public class TehVillaApplication extends Application {
  private static TehVillaApplication instance;
  public static Context getContext()
  {
    return instance;
  }
  public TehVillaApplication() {
    instance = this;
  }
  @Override
  public void onCreate() {
    super.onCreate();

    OneSignal.startInit(this).setNotificationOpenedHandler(new MyNotificationOpenedHandler())
    .setNotificationReceivedHandler(new MyNotificationReceivedHandler()).init();
    Realm.init(this);
    if (android.os.Build.VERSION.SDK_INT >= 9) {
      try {
        // StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.LAX);
        Class<?> strictModeClass = Class.forName(
                "android.os.StrictMode", true, Thread.currentThread()
                        .getContextClassLoader());
        Class<?> threadPolicyClass = Class.forName(
                "android.os.StrictMode$ThreadPolicy", true, Thread
                        .currentThread().getContextClassLoader());
        Field laxField = threadPolicyClass.getField("LAX");
        Method setThreadPolicyMethod = strictModeClass.getMethod(
                "setThreadPolicy", threadPolicyClass);
        setThreadPolicyMethod.invoke(strictModeClass,
                laxField.get(null));
      } catch (Exception ignored) {
        //do nothing
      }
    }
    new Prefs.Builder()
            .setContext(this)
            .setMode(ContextWrapper.MODE_PRIVATE)
            .setPrefsName(getPackageName())
            .setUseDefaultSharedPreference(true)
            .build();
    DrawerImageLoader.init(new AbstractDrawerImageLoader() {
      @Override
      public void set(ImageView imageView, Uri uri, Drawable placeholder) {
        Glide.with(imageView.getContext()).load(uri).placeholder(placeholder).into(imageView);
      }
      @Override
      public void cancel(ImageView imageView) {
        Glide.clear(imageView);
      }
      @Override
      public Drawable placeholder(Context ctx, String tag) {
        //define different placeholders for different imageView targets
        //default tags are accessible via the DrawerImageLoader.Tags
        //custom ones can be checked via string. see the CustomUrlBasePrimaryDrawerItem LINE 111
        Log.d("TehVilla","Tag : " + tag);
        if (DrawerImageLoader.Tags.PROFILE.name().equals(tag)) {
          return DrawerUIUtils.getPlaceHolder(ctx);
        } else if (DrawerImageLoader.Tags.ACCOUNT_HEADER.name().equals(tag)) {
          return new IconicsDrawable(ctx).iconText(" ").backgroundColorRes(com.mikepenz.materialdrawer.R.color.primary).sizeDp(56);
        } else if ("customUrlItem".equals(tag)) {
          return new IconicsDrawable(ctx).iconText(" ").backgroundColorRes(R.color.colorPrimaryDark).sizeDp(56);
        }
        //we use the default one for
        //DrawerImageLoader.Tzxags.PROFILE_DRAWER_ITEM.name()
        return super.placeholder(ctx, tag);
      }
    });
    Fabric.with(this, new Crashlytics());
  }
  @Override
  protected void attachBaseContext(Context base) {
    super.attachBaseContext(base);
    MultiDex.install(this);
  }


}