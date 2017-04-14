package com.tehvilla.apps.tehvilla.helpers;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.Toast;

import com.onesignal.OSNotificationAction;
import com.onesignal.OSNotificationOpenResult;
import com.onesignal.OneSignal;
import com.tehvilla.apps.tehvilla.HomeActivity;
import com.tehvilla.apps.tehvilla.IntroKeduaActivity;
import com.tehvilla.apps.tehvilla.IntroKetigaActivity;
import com.tehvilla.apps.tehvilla.KeranjangActivity;
import com.tehvilla.apps.tehvilla.KonfirmasiFragment;
import com.tehvilla.apps.tehvilla.R;

import org.json.JSONObject;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by AkhmadNaufal on 2/11/17.
 */

public class MyNotificationOpenedHandler implements OneSignal.NotificationOpenedHandler {
    // This fires when a notification is opened by tapping on it.
    @Override
    public void notificationOpened(OSNotificationOpenResult result ) {
        OSNotificationAction.ActionType actionType = result.action.type;
        JSONObject data = result.notification.payload.additionalData;
        String activityToBeOpened;

        //While sending a Push notification from OneSignal dashboard
        // you can send an addtional data named "activityToBeOpened" and retrieve the value of it and do necessary operation
        //If key is "activityToBeOpened" and value is "AnotherActivity", then when a user clicks
        //on the notification, AnotherActivity will be opened.
        //Else, if we have not set any additional data MainActivity is opened.
        if (data != null) {
            Fragment fragment = null;
            activityToBeOpened = data.optString("kategori_notifikasi", null);
            if (activityToBeOpened != null && activityToBeOpened.equalsIgnoreCase("1")) {
                Log.v("Topp", "customkey set with value: " + activityToBeOpened);
                Intent intent = new Intent(TehVillaApplication.getContext(), KeranjangActivity.class);
                Bundle b = new Bundle();
                b.putString("judul", "22");
                intent.putExtras(b);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
                TehVillaApplication.getContext().startActivity(intent);
            } else if (activityToBeOpened != null && activityToBeOpened.equalsIgnoreCase("2")) {
                Log.v("Topp", "customkey set with value: " + activityToBeOpened);
                Intent intent = new Intent(TehVillaApplication.getContext(), KeranjangActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
                Bundle b = new Bundle();
                b.putString("judul", "33");
                intent.putExtras(b);
                TehVillaApplication.getContext().startActivity(intent);
            }

        }

        //If we send notification with action buttons we need to specidy the button id's and retrieve it to
        //do the necessary operation.
        if (actionType == OSNotificationAction.ActionType.ActionTaken) {
            Log.i("OneSignalExample", "Button pressed with id: " + result.action.actionID);
            if (result.action.actionID.equals("ActionOne")) {
                Toast.makeText(TehVillaApplication.getContext(), "ActionOne Button was pressed", Toast.LENGTH_LONG).show();
            } else if (result.action.actionID.equals("ActionTwo")) {
                Toast.makeText(TehVillaApplication.getContext(), "ActionTwo Button was pressed", Toast.LENGTH_LONG).show();
            }
        }
    }
}
