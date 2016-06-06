package com.ivpomazkov.simplerss;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class RSSActivity extends AppCompatActivity
implements RSSListFragment.ButtonSettingsPressed{
    private final static String TAG = "RSS";
    private FragmentManager mFragmentManager;
    public static final String UPDATE_ACTION = "update_action";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rss);
        mFragmentManager = getSupportFragmentManager();
        RSSListFragment listFragment = RSSListFragment.newInstance();
        mFragmentManager.beginTransaction().add(R.id.fragment_container, listFragment).commit();

    }

    @Override
    public void onButtonSettingsPressed() {
        RSSSettingsFragment settingsFragment = RSSSettingsFragment.newInstance();
        mFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, settingsFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Intent intent = new Intent(this, RSSGetter.class);
        stopService(intent);
    }

}
