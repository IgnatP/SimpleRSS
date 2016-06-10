package com.ivpomazkov.simplerss;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import java.util.List;

/**
 * Created by Ivpomazkov on 01.06.2016.
 */
public class RSSSettingsFragment extends Fragment {
    public final static int URLPICKER = 1;
    public final static String REQUESTURL = "url";
    public final static String REQUESTDESCRIPTION = "description";
    private RSSChannelList mRSSChannelList;
    private RecyclerView mRecyclerView;
    private ChannelRVAdapter mAdapter;

    public static RSSSettingsFragment newInstance(){
        return new RSSSettingsFragment();
    }
    @Override
    public void onCreate(Bundle onSavedInstanceState){
        super.onCreate(onSavedInstanceState);
        setHasOptionsMenu(true);
        mRSSChannelList = RSSChannelList.get(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle onSavedInstanceState){
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.settings,container,false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.url_list_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }

    public void updateUI() {
        if (mAdapter == null) {
            mAdapter = new ChannelRVAdapter(mRSSChannelList.getChannels());
            mRecyclerView.setAdapter(mAdapter);

        } else {
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.setChannels(mRSSChannelList.getChannels());
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        updateUI();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu,inflater);
        inflater.inflate(R.menu.menu_settings,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.menu_item_add_url:
                InputUrlDialog inputUrlDialog = new InputUrlDialog();
                inputUrlDialog.setTargetFragment(this, URLPICKER);
                inputUrlDialog.show(getFragmentManager(),"TAG");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        switch(requestCode){
            case URLPICKER:
                Bundle bundle = null;
                if (resultCode == Activity.RESULT_OK){
                    bundle = data.getExtras();
                    String temp = bundle.getString(REQUESTURL);
                    RSSChannel channel = new RSSChannel();
                    channel.setUrl(temp);
                    temp = bundle.getString(REQUESTDESCRIPTION);
                    channel.setDescription(temp);
                    channel.setActive(false);
                    mRSSChannelList.addChannel(channel);
                    updateUI();
                }
                break;
            default:
                break;
        }
    }

    private class ChannelViewHolder extends RecyclerView.ViewHolder
    implements View.OnClickListener{
        private RSSChannel mChannel;
        private CheckBox mUrlSelected;
        public ChannelViewHolder(View itemView) {
            super(itemView);
            mUrlSelected = (CheckBox) itemView.findViewById(R.id.url_selected);
            mUrlSelected.setOnClickListener(this);
        }

        public void bindChannel(RSSChannel channel){
            mChannel = channel;
            mUrlSelected.setText(mChannel.getDescription());
            mUrlSelected.setChecked(mChannel.isActive());
        }

        @Override
        public void onClick(View v) {
            mChannel.setActive(mUrlSelected.isChecked());
            mRSSChannelList.updateChannel(mChannel);
        }
    }

    private class ChannelRVAdapter extends RecyclerView.Adapter<ChannelViewHolder>{
        private List<RSSChannel> mChannels;

        public ChannelRVAdapter(List<RSSChannel> channels){ mChannels = channels; }

        public void setChannels(List<RSSChannel> channels) {mChannels = channels;}

        @Override
        public ChannelViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(getActivity()).inflate(R.layout.settings_item, parent, false);
            ChannelViewHolder uvh = new ChannelViewHolder(v);
            return uvh;
        }

        @Override
        public void onBindViewHolder(ChannelViewHolder holder, int position) {
            RSSChannel channel = new RSSChannel();
            channel = mChannels.get(position);
            holder.bindChannel(channel);
        }

        @Override
        public int getItemCount() {
            return mChannels.size();
        }
    }

}

