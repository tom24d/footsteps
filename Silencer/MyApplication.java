package jp.gr.java_conf.nstommo_silenter;

import android.app.Application;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

/**
 * Created by 智哉 on 2015/09/04.
 */
public class MyApplication extends Application {

    private Tracker mTracker;

    @Override
    public void onCreate() {
        super.onCreate();
        GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
        mTracker = analytics.newTracker(R.xml.global_tracker);
        mTracker.enableAdvertisingIdCollection(true);

    }

    /**
     * Gets the default {@link Tracker} for this {@link Application}.
     * @return tracker
     */

}

