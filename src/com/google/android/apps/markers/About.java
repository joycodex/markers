/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.android.apps.markers;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.CharBuffer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.AssetFileDescriptor;
import android.webkit.WebView;

class About {
    static char buf[] = new char[1024];

    static String loadFileText(Context context, String filename) {
        try {
            StringBuffer fileData = new StringBuffer();
            InputStreamReader reader = new InputStreamReader(context.getAssets().open(filename));
            while ( reader.read(buf) > 0 ) {
                fileData.append(buf);
            }
            return fileData.toString();
        } catch (IOException e) {
            return null;
        }
    }

	static void show(final Activity activity) {
		final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setTitle(null);
		builder.setCancelable(true);
//		builder.setPositiveButton(R.string.about_dismiss_button,
//				new DialogInterface.OnClickListener() {
//					public void onClick(DialogInterface dialog, int which) {
//					}
//				});
//		builder.setMessage(R.string.about_body);
        
        String htmlString = loadFileText(activity, "about.html");
        if (htmlString != null) {
            String licenseString = loadFileText(activity, "license.html");
            String version = "";
            try {
                PackageInfo pi = activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0);
                if (pi != null) {
                    version = pi.versionName;
                }
            } catch (NameNotFoundException e) {
                //pass
            }
            htmlString = htmlString.replaceAll("__VERSION__", version);
            htmlString = htmlString.replaceAll("__LICENSE__", licenseString);

            WebView webview = new WebView(activity);
            webview.loadDataWithBaseURL("file:///android_asset/", htmlString, "text/html", "utf-8", null);
            builder.setView(webview);
        } else {
            builder.setMessage("Markers");
        }
		builder.create().show();
	}
}
