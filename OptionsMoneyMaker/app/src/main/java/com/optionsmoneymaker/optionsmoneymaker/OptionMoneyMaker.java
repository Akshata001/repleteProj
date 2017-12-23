package com.optionsmoneymaker.optionsmoneymaker;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.gson.Gson;
import com.onesignal.OSNotification;
import com.onesignal.OSNotificationAction;
import com.onesignal.OSNotificationOpenResult;
import com.onesignal.OneSignal;
import com.onesignal.shortcutbadger.ShortcutBadger;
import com.optionsmoneymaker.optionsmoneymaker.fragment.HomeFragment;
import com.optionsmoneymaker.optionsmoneymaker.model.MessageData;
import com.optionsmoneymaker.optionsmoneymaker.model.NotificationResult;
import com.optionsmoneymaker.optionsmoneymaker.rest.RestClient;
import com.optionsmoneymaker.optionsmoneymaker.utils.ConnectionDetector;
import com.optionsmoneymaker.optionsmoneymaker.utils.Constants;
import com.optionsmoneymaker.optionsmoneymaker.utils.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;

/**
 * Created by Sagar on 07-10-2016.
 */
public class OptionMoneyMaker extends Application {

    private ConnectionDetector cd;
    private SessionManager session;
    private int badgeCount = 0;

    @Override
    public void onCreate() {
        super.onCreate();

        cd = new ConnectionDetector(getApplicationContext());
        session = new SessionManager(getApplicationContext());

        OneSignal.startInit(this)
                .setNotificationOpenedHandler(new ExampleNotificationOpenedHandler())
                .setNotificationReceivedHandler(new CustomNotificationReceivedHandler())
                .inFocusDisplaying(null)
                .init();
//        OneSignal.setInFocusDisplaying(OneSignal.OSInFocusDisplayOption.InAppAlert);

        /*OneSignal.idsAvailable(new OneSignal.IdsAvailableHandler() {
            @Override
            public void idsAvailable(String userId, String registrationId) {
                Log.e("debug", "User:" + userId);
                if (registrationId != null)
                    Log.e("debug", "registrationId:" + registrationId);

                SessionManager session = new SessionManager(getApplicationContext());
                session.setRegisterID(userId);
            }
        });*/
    }

    private class ExampleNotificationOpenedHandler implements OneSignal.NotificationOpenedHandler {
        // This fires when a notification is opened by tapping on it.
        @Override
        public void notificationOpened(OSNotificationOpenResult result) {

            if (cd.isConnectingToInternet()) {

                OSNotificationAction.ActionType actionType = result.action.type;
                JSONObject data = result.notification.payload.additionalData;
                String msgID = "";

                if (data != null) {
                    msgID = data.optString("message_id", null);
                }

                if (actionType == OSNotificationAction.ActionType.ActionTaken)
                    Log.i("OneSignalExample", "Button pressed with id: " + result.action.actionID);

                SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.US);
                String strTime = dateFormatter.format(new Date());

                RestClient.getMoneyMaker().messageRead(msgID, session.getUserID(),
                        strTime, new Callback<NotificationResult>() {
                            @Override
                            public void success(NotificationResult result, Response response) {
                                if ((int) result.getStatus() == 1) {
                                } else if ((int) result.getStatus() == 0) {
                                }
                            }

                            @Override
                            public void failure(RetrofitError error) {
                                Log.e("OptionMoneyMaker", "API failure " + error);
                            }
                        });

                try {
                    ArrayList<MessageData> list = new ArrayList<MessageData>();
                    list = session.getLatestMessage().getData();
                    if (list != null && list.size() > 0) {
                        for (int i = 0; i < list.size(); i++) {
                            if (msgID.equalsIgnoreCase(list.get(i).getId())) {
                                list.get(i).setIsRead("1");
                                // create a new Gson instance
                                Gson gson = new Gson();
                                // convert your list to json
                                String jsonMsgList = gson.toJson(list);
                                JSONArray jsonArray = new JSONArray(jsonMsgList);
                                JSONObject jsonObject = new JSONObject();
                                jsonObject.put("status", 1);
                                jsonObject.put("data", jsonArray);

                                session.setLatestMessage(jsonObject.toString());
                                break;
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // The following can be used to open an Activity of your choice.
                Intent intent = new Intent(getApplicationContext(), MessageDetailActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(Constants.ID, msgID);
                intent.putExtra(Constants.TYPE, "notification");
                startActivity(intent);
            } else {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class CustomNotificationReceivedHandler implements OneSignal.NotificationReceivedHandler {

        @Override
        public void notificationReceived(OSNotification notification) {
            Log.d(OptionMoneyMaker.class.getSimpleName(), "notificationReceived: " + notification.toString());
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (!isAppIsInBackground(getApplicationContext())) {
                            showNewMessageArrived();
                            reload();
                        } else {
                            showBadge();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    private void showBadge() {
        badgeCount++;
        ShortcutBadger.applyCount(getApplicationContext(), badgeCount);
    }


    private void reload() {
        if (cd.isConnectingToInternet()) {
            RestClient.getMoneyMaker().lastMessage(session.getUserID(), new Callback<Response>() {
                @Override
                public void success(Response result, Response response) {
                    try {
                        JSONObject main = new JSONObject(new String(((TypedByteArray) response.getBody()).getBytes()));
                        if ((int) main.getInt("status") == 1) {
                            session.setLatestMessage(main.toString());
                            if (HomeFragment.active) {
                                HomeFragment.refreshView(getCurrentActivity());
                            }
                        } else if ((int) main.getInt("status") == 0) {
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.e("Application", "API failure " + error);
                }
            });
        }
    }

    public void showNewMessageArrived() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
        builder.setMessage(R.string.arrived_msg)
                .setPositiveButton(R.string.ok, new
                        DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // FIRE ZE MISSILES!
                                dialog.dismiss();
                            }
                        }).setTitle("Message");
        // Create the AlertDialog object and return it
        AlertDialog dialog = builder.create();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        }
        dialog.show();
    }

    public static boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }
        return isInBackground;
    }

    private Activity mCurrentActivity = null;

    public Activity getCurrentActivity() {
        return mCurrentActivity;
    }

    public void setCurrentActivity(Activity mCurrentActivity) {
        this.mCurrentActivity = mCurrentActivity;
    }

}
