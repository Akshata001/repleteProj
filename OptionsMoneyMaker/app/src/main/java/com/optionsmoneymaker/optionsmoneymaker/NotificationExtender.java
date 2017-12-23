package com.optionsmoneymaker.optionsmoneymaker;

import com.onesignal.NotificationExtenderService;
import com.onesignal.OSNotificationReceivedResult;
import com.optionsmoneymaker.optionsmoneymaker.utils.SessionManager;

/**
 * Created by Sagar on 26-10-2016.
 */
public class NotificationExtender extends NotificationExtenderService {

    @Override
    protected boolean onNotificationProcessing(OSNotificationReceivedResult osNotificationReceivedResult) {
        // Read properties from result.

        // Return true to stop the notification from displaying.
        SessionManager session = new SessionManager(NotificationExtender.this);
        if (session.isLoggedIn()) {
            return false;
        } else {
            return true;
        }
    }
}
