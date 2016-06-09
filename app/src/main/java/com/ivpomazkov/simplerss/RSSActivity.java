package com.ivpomazkov.simplerss;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.Serializable;
import java.util.UUID;

public class RSSActivity extends AppCompatActivity
implements RSSListFragment.ButtonSettingsPressed,
            RSSListFragment.OpenNewsItem{
    public static final String NEWS_ID = "news_id";
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

    @Override
    public void onNewsItemPressed(UUID uuid) {
        Intent intent = new Intent(getApplicationContext(),NewsItemViewPagerActivity.class);
        intent.putExtra(NEWS_ID, uuid);
        startActivity(intent);
    }
}
