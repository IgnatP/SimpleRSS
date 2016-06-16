package com.ivpomazkov.simplerss;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import com.ivpomazkov.simplerss.R;

import java.util.UUID;

/**
 * Created by Ivpomazkov on 09.06.2016.
 */
public class NewsItemFragment extends Fragment {
    private TextView mTitle;

    private WebView mWebView;
    private static String ARG_NEWS_ID = "newsItemUUID";

    public static NewsItemFragment newInstance(UUID uuid){
        Bundle args = new Bundle();
        args.putSerializable(ARG_NEWS_ID, uuid);
        NewsItemFragment fragment =  new NewsItemFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup root, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.news_item_detail,root,false);
        UUID uuid = (UUID) getArguments().getSerializable(ARG_NEWS_ID);
        NewsItem item = NewsList.get(getContext()).getNewsItem(uuid, false);
        mTitle = (TextView) view.findViewById(R.id.news_detail_title);
        mWebView = (WebView) view.findViewById(R.id.news_detail_webview);
        mTitle.setText(item.getTitle());
        mWebView.loadUrl(item.getLink());
        return view;
    }
}
