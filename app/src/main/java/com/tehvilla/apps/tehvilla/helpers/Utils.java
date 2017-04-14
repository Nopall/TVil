package com.tehvilla.apps.tehvilla.helpers;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;

import com.tehvilla.apps.tehvilla.R;

/**
 * Created by AkhmadNaufal on 2/17/17.
 */

public class Utils {
    public static void setBadgeCount(Context context, LayerDrawable icon, String count) {

        BadgesDrawable badge;

        // Reuse drawable if possible
        Drawable reuse = icon.findDrawableByLayerId(R.id.ic_badge);
        if (reuse != null && reuse instanceof BadgesDrawable) {
            badge = (BadgesDrawable) reuse;
        } else {
            badge = new BadgesDrawable(context);
        }

        badge.setCount(count);
        icon.mutate();
        icon.setDrawableByLayerId(R.id.ic_badge, badge);
    }
}
