package com.cloudeducate.redtick;

import android.app.Application;
import android.content.Context;

/**
 * Created by yogesh on 21/1/16.
 */
public class ApplicationWrapper extends Application {
    private static ApplicationWrapper applicationWrapper;

    @Override
    public void onCreate() {
        super.onCreate();
        applicationWrapper = this;
        super.onCreate();
        /*if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Log.d("MyApp", "No SDCARD");
        } else {
            File directory = new File(Environment.getExternalStorageDirectory() + File.separator + Constants.AppFolderName);
            directory.mkdirs();
        }*/
    }

    public static ApplicationWrapper getApplicationWrapper() {
        return applicationWrapper;
    }

    public static Context getAppContext() {
        return applicationWrapper.getApplicationContext();
    }
}
