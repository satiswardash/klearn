package com.kortain.klearn;

import android.app.Application;

import com.google.firebase.FirebaseApp;
import com.leocardz.link.preview.library.TextCrawler;

/**
 * Created by satiswardash on 20/01/18.
 */

public class Klearn extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
    }
}
