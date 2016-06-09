package com.ivpomazkov.simplerss;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.TreeSet;
import java.util.UUID;

/**
 * Created by Ivpomazkov on 31.05.2016.
 */
public class RSSListFragment extends Fragment {

    private final static String TAG = "RSS";
    private ButtonSettingsPressed mButtonSettingsListener;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RSSListRecyclerViewAdapter mAdapter;
    private NewsList mNewsList;
    private List<NewsItem> mNewsItemList;
    private BroadcastReceiver mBroadcastReceiver;


    public interface ButtonSettingsPressed{
       void onButtonSettingsPressed();
    }

    public interface OpenNewsItem{
        void onNewsItemPressed(UUID uuid);
    }

    public static RSSListFragment newInstance(){
        return new RSSListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Log.d("info","list->onCreate()");
        setHasOptionsMenu(true);
        mNewsList = NewsList.get(getActivity());
        mNewsItemList = mNewsList.getNews(true);
    }

    @Override
    public void onStart(){
        super.onStart();
        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d("info", "intent UPDATE_ACTION received");
                boolean readyToUpdate = intent.getBooleanExtra(RSSGetter.READY_TO_UPDATE,false);
                if (readyToUpdate)
                    updateUI();
            }
        };
        IntentFilter intentFilter = new IntentFilter(RSSActivity.UPDATE_ACTION);
        getActivity().registerReceiver(mBroadcastReceiver, intentFilter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        super.onCreateView(inflater, container, savedInstanceState);
        Log.d("info","list->onCreateView()");
        View view = inflater.inflate(R.layout.news_list,container, false);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.news_list_swipe_refresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Intent intent = new Intent(getActivity(), RSSGetter.class);
                getActivity().startService(intent);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
        mRecyclerView = (RecyclerView) view.findViewById(R.id.news_list_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
        updateUI();
    }

    @Override
    public void onStop(){
        super.onStop();
        getActivity().unregisterReceiver(mBroadcastReceiver);
        mNewsItemList = mNewsList.getNews(false);
        mNewsList.addNews(mNewsItemList, true);
    }

    private void updateUI(){
        mNewsItemList = mNewsList.getNews(false);
        if (mAdapter == null) {
            mAdapter = new RSSListRecyclerViewAdapter(mNewsItemList);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.setNewsList(mNewsItemList);
            Log.d("info", "size of newsList" + mNewsItemList.size());
            mAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_news_list,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.menu_item_settings:

                try{
                    mButtonSettingsListener = (ButtonSettingsPressed) getActivity();
                    mButtonSettingsListener.onButtonSettingsPressed();
                } catch (ClassCastException e) {
                    Log.d("info", "Activity " + mButtonSettingsListener.toString() + "isn't host ButtonSettingsPressed");
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class RSSListRecyclerViewHolder extends RecyclerView.ViewHolder
    implements View.OnClickListener{
        private NewsItem mNewsItem;
        private TextView mTitle;
        private TextView mDescription;
        private TextView mPubDate;

        public RSSListRecyclerViewHolder(View itemView) {
            super(itemView);
            mTitle = (TextView) itemView.findViewById(R.id.news_item_title);
            itemView.setOnClickListener(this);
            //mDescription = (TextView) itemView.findViewById(R.id.news_item_description);
            //mPubDate = (TextView) itemView.findViewById(R.id.news_item_pubDate);
        }

        public void bind(NewsItem newsItem){
            mNewsItem = newsItem;
            mTitle.setText(newsItem.getTitle());
        }

        @Override
        public void onClick(View v) {
            OpenNewsItem opener;
            try{
                opener = (OpenNewsItem) getActivity();
                opener.onNewsItemPressed(mNewsItem.getId());
            } catch (ClassCastException e){
                Log.d("info",e.toString());
            }
        }
    }

    private class RSSListRecyclerViewAdapter extends  RecyclerView.Adapter<RSSListRecyclerViewHolder>{

        private List<NewsItem> mNewsList;

        public RSSListRecyclerViewAdapter(List<NewsItem> newsList){
            mNewsList = newsList;
        }
        public void setNewsList(List<NewsItem> newsList) {mNewsList = newsList; }

        @Override
        public RSSListRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(getContext()).inflate(R.layout.news_item,parent, false);
            RSSListRecyclerViewHolder vh = new RSSListRecyclerViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(RSSListRecyclerViewHolder holder, int position) {
            NewsItem nItem = mNewsList.get(position);
            holder.bind(nItem);
            //holder.mDescription.setText(nItem.getDescription());
            //holder.mPubDate.setText(nItem.getPubDate().toString());
        }

        @Override
        public int getItemCount() {
            return mNewsList.size();
        }
    }
}
