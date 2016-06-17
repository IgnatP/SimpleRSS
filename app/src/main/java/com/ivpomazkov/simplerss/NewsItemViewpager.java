package com.ivpomazkov.simplerss;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;
import java.util.UUID;

/**
 * Created by Ivpomazkov on 09.06.2016.
 */
public class NewsItemViewPager extends Fragment {
    public static final String TAG = "VIEWPAGER";
    private static final String NEWS_ID = "newsId";
    private ViewPager mViewPager;
    private List<NewsItem> mNewsItemList;

    public static NewsItemViewPager newInstance(UUID uuid){
        Bundle args = new Bundle();
        args.putSerializable(NEWS_ID, uuid);
        NewsItemViewPager fragment = new NewsItemViewPager();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceStat){
        super.onCreate(savedInstanceStat);

        View view = inflater.inflate(R.layout.news_viewpager, container, false);
        UUID uuid = (UUID) getArguments().getSerializable(NEWS_ID);
        Log.d(TAG, "onCreateView, id = " + uuid);
        mViewPager = (ViewPager) view.findViewById(R.id.news_item_detail_viewpager);
        mNewsItemList = NewsList.get(getContext()).getNews(false);

        FragmentManager fm = getActivity().getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
            @Override
            public Fragment getItem(int position) {
                NewsItem item = mNewsItemList.get(position);
                return NewsItemFragment.newInstance(item.getId());
            }

            @Override
            public int getCount() {
                return mNewsItemList.size();
            }
        });
        for (int i = 0; i < mNewsItemList.size(); i++)
            if (mNewsItemList.get(i).getId().equals(uuid)) {
                mViewPager.setCurrentItem(i);
            }
        return view;
    }
}
