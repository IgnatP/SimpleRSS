package com.ivpomazkov.simplerss;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.List;
import java.util.UUID;

/**
 * Created by Ivpomazkov on 09.06.2016.
 */
public class NewsItemViewPagerActivity extends AppCompatActivity {
    private ViewPager mViewPager;
    private List<NewsItem> mNewsItemList;

    public static NewsItemViewPagerActivity newInstance(){
        return new NewsItemViewPagerActivity();
    }


    @Override
    public void onCreate(Bundle savedInstanceStat){
        super.onCreate(savedInstanceStat);
        setContentView(R.layout.news_viewpager);
        UUID uuid = (UUID) getIntent().getSerializableExtra(RSSActivity.NEWS_ID);
        mViewPager = (ViewPager) findViewById(R.id.news_item_detail_viewpager);
        mNewsItemList = NewsList.get(this).getNews(true);

        FragmentManager fm = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
            @Override
            public Fragment getItem(int position) {
                Log.d("info", "viewPager->getItem, position " + position);
                NewsItem item = mNewsItemList.get(position);
                NewsItemFragment fragment = NewsItemFragment.newInstance(item.getId());
                return fragment;
            }

            @Override
            public int getCount() {
                return mNewsItemList.size();
            }
        });
        for (int i = 0; i < mNewsItemList.size(); i++)
            if (mNewsItemList.get(i).getId().equals(uuid))
                mViewPager.setCurrentItem(i);
    }
}
