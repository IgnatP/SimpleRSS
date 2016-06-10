package com.ivpomazkov.simplerss;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.List;
import java.util.UUID;

public class RSSActivity extends AppCompatActivity
implements RSSListFragment.ButtonSettingsPressed,
            RSSListFragment.OpenNewsItem{
    private final static String TAG = "RSS";
    private FragmentManager mFragmentManager;
    public static final String UPDATE_ACTION = "update_action";
    public static  NewsList mNewsList;
    public static List<NewsItem> mNewsItemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rss);
        mFragmentManager = getSupportFragmentManager();
        RSSListFragment listFragment = RSSListFragment.newInstance();
        mFragmentManager.beginTransaction().add(R.id.fragment_container, listFragment).commit();
        RSSActivity.mNewsList = NewsList.get(this);
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
    public void onStop(){
        super.onStop();
        Log.d("info", "MAIN_ACTIVITY" + " -> onStop()");
        mNewsItemList = mNewsList.getNews(false);
        mNewsList.addNews(mNewsItemList, true);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Intent intent = new Intent(this, RSSGetter.class);
        stopService(intent);
    }

    @Override
    public void onNewsItemPressed(UUID uuid) {
        NewsItemViewPager newsItemFragment = NewsItemViewPager.newInstance(uuid);
        mFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, newsItemFragment)
                .addToBackStack(null)
                .commit();
    }
}
