/*
 * Copyright 2013 Dmitriy Tarasov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.iamadonar.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.Contacts;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;

import java.io.File;
import java.net.URL;
import java.util.List;

public class IntentUtils {

    /**
     * Open app page at Google Play. If Play Store application isn't available on the device
     * then web browser will be opened
     *
     * @param context Application context
     */
    public static Intent openPlayStore(Context context) {
        return openPlayStore(context, true);
    }

    /**
     * Open app page at Google Play
     *
     * @param context       Application context
     * @param openInBrowser Should we try to open application page in web browser
     *                      if Play Store app not found on device
     */
    public static Intent openPlayStore(Context context, boolean openInBrowser) {
        String appPackageName = context.getPackageName();
        Intent marketIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName));
        if (isIntentAvailable(context, marketIntent)) {
            return marketIntent;
        }
        if (openInBrowser) {
            return openLink("https://play.google.com/store/apps/details?id=" + appPackageName);
        }
        return marketIntent;
    }

    /**
     * Send email message
     *
     * @param to      Receiver email
     * @param subject Message subject
     * @param text    Message body
     * @see #sendEmail(String[], String, String)
     */
    public static Intent sendEmail(String to, String subject, String text) {
        return sendEmail(new String[]{to}, subject, text);
    }

    /**
     * @see #sendEmail(String, String, String)
     */
    public static Intent sendEmail(String[] to, String subject, String text) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc822");
        intent.putExtra(Intent.EXTRA_EMAIL, to);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, text);
        return intent;
    }

    /**
     * Share text via thirdparty app like twitter, facebook, email, sms etc.
     *
     * @param subject Optional subject of the message
     * @param text    Text to share
     */
    public static Intent shareText(String subject, String text) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        if (!TextUtils.isEmpty(subject)) {
            intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        }
        intent.putExtra(Intent.EXTRA_TEXT, text);
        intent.setType("text/plain");
        return intent;
    }

    /**
     * Send SMS message using built-in app
     *
     * @param to      Receiver phone number
     * @param message Text to send
     */
    public static Intent sendSms(String to, String message) {
        Uri smsUri = Uri.parse("tel:" + to);
        Intent intent = new Intent(Intent.ACTION_VIEW, smsUri);
        intent.putExtra("address", to);
        intent.putExtra("sms_body", message);
        intent.setType("vnd.android-dir/mms-sms");
        return intent;
    }

   
    /**
     * Opens the Maps application to the given query.
     *
     * @param query Query string
     * @see #showLocation(float, float, Integer)
     */
    public static Intent findLocation(String query) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        String data = String.format("geo:0,0?q=%s", query);
        intent.setData(Uri.parse(data));
        return intent;
    }

    /**
     * Open system settings location services screen for turning on/off GPS
     */
    public static Intent showLocationServices() {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        return intent;
    }

    /**
     * Open a browser window to the URL specified.
     *
     * @param url Target url
     */
    public static Intent openLink(String url) {
        // if protocol isn't defined use http by default
        if (!TextUtils.isEmpty(url) && !url.contains("://")) {
            url = "http://" + url;
        }

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        return intent;
    }

    /**
     * @see #openLink(String)
     */
    public static Intent openLink(URL url) {
        return openLink(url.toString());
    }

    

    /**
     * Calls the entered phone number. Valid telephone numbers as defined in the IETF RFC 3966 are accepted.
     * Valid examples include the following:
     * tel:2125551212
     * tel: (212) 555 1212
     * <p/>
     * Note: This requires your application to request the following permission in your manifest:
     * <code>&lt;uses-permission android:name="android.permission.CALL_PHONE"/&gt;</code>
     *
     * @param phoneNumber Phone number
     */
    public static Intent callPhone(String phoneNumber) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        return intent;
    }

   

    /**
     * Dials (but does not actually initiate the call) the number given.
     * Telephone number normalization described for {@link #callPhone(String)} applies to dial as well.
     *
     * @param phoneNumber Phone number
     */
    public static Intent dialPhone(String phoneNumber) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        return intent;
    }

    

    /**
     * Check that in the system exists application which can handle this intent
     *
     * @param context Application context
     * @param intent  Checked intent
     * @return true if intent consumer exists, false otherwise
     */
    public static boolean isIntentAvailable(Context context, Intent intent) {
        PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

   
}
