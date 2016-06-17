package com.ivpomazkov.simplerss;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Switch;

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
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        return view;
    }

    public void updateUI() {
        if (mAdapter == null) {
            mAdapter = new ChannelRVAdapter(mRSSChannelList.getChannels());
            mRecyclerView.setAdapter(mAdapter);
            ItemTouchHelper.Callback callback = new ItemTouchHelperCallback(mAdapter, getContext());
            ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
            touchHelper.attachToRecyclerView(mRecyclerView);
        } else {
            Log.d("info", "SETTINGS: updateUI");
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.setChannels(mRSSChannelList.getChannels());
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
                //InputUrlDialog inputUrlDialog = new InputUrlDialog();
                //inputUrlDialog.setTargetFragment(this, URLPICKER);
                InputUrlDialog inputUrlDialog = initUrlDialog("", "");
                inputUrlDialog.show(getFragmentManager(),"TAG");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public InputUrlDialog initUrlDialog(String url, String description){
        InputUrlDialog inputUrlDialog = InputUrlDialog.newInstance(url, description);
        inputUrlDialog.setTargetFragment(this, URLPICKER);
        Log.d("info", "target fragment = " + this);
        return inputUrlDialog;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        switch(requestCode){
            case URLPICKER:
                Bundle bundle;
                if (resultCode == Activity.RESULT_OK){
                    bundle = data.getExtras();
                    String temp = bundle.getString(REQUESTURL);
                    RSSChannel channel = new RSSChannel();
                    channel.setUrl(temp);
                    temp = bundle.getString(REQUESTDESCRIPTION);
                    channel.setDescription(temp);
                    channel.setActive(false);
                    mAdapter.addItem(channel);
                   // mRSSChannelList.addChannel(channel);
                   // mAdapter.notifyItemInserted(mRSSChannelList.getSize());
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
        private Switch mSwitch;
        public ChannelViewHolder(View itemView) {
            super(itemView);
            mSwitch = (Switch) itemView.findViewById(R.id.switch1);
            mSwitch.setOnClickListener(this);
        }

        public void bindChannel(RSSChannel channel){
            mChannel = channel;
            mSwitch.setText(mChannel.getDescription());
            mSwitch.setChecked(mChannel.isActive());
        }

        @Override
        public void onClick(View v) {
            mChannel.setActive(mSwitch.isChecked());
            mRSSChannelList.updateChannel(mChannel);
        }
    }

    private class ChannelRVAdapter extends RecyclerView.Adapter<ChannelViewHolder>
    implements ItemTouchHelperAdapter{
        private List<RSSChannel> mChannels;

        public ChannelRVAdapter(List<RSSChannel> channels){ mChannels = channels; }

        public void setChannels(List<RSSChannel> channels) {mChannels = channels;}

        @Override
        public ChannelViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(getActivity()).inflate(R.layout.settings_item, parent, false);
            return new ChannelViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ChannelViewHolder holder, int position) {
            holder.bindChannel(mChannels.get(position));
        }

        @Override
        public int getItemCount() {
            return mChannels.size();
        }

        @Override
        public void onItemMove(int fromPosition, int toPosition) {

        }

        @Override
        public void onItemDismiss(int position) {
            mRSSChannelList.deleteChannel(mChannels.get(position));
            mChannels.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, mChannels.size());
            Log.d("info", "SETTINGS: item removed, position " + position);
        }
        public void addItem(RSSChannel channel){
            mRSSChannelList.addChannel(channel);
            mAdapter.notifyItemInserted(mRSSChannelList.getSize());
        }

    }

    class ItemTouchHelperCallback extends ItemTouchHelper.Callback {
        private final ItemTouchHelperAdapter mAdapter;
        private Context mContext;

        public ItemTouchHelperCallback(ItemTouchHelperAdapter adapter, Context context) {
            mAdapter = adapter;
            mContext = context;
        }

        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            int swipeFlags = ItemTouchHelper.LEFT;//ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
            return makeMovementFlags(dragFlags, swipeFlags);
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            // mAdapter.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            Log.d("info", "ItemTouchHelper -> onSwiped");
            switch (direction) {
                case ItemTouchHelper.LEFT:
                    mAdapter.onItemDismiss(viewHolder.getAdapterPosition());
                    break;
                case ItemTouchHelper.RIGHT:
                    int position = viewHolder.getAdapterPosition();
                    Log.d("info", mRSSChannelList.getChannels().get(position).getUrl());
                    InputUrlDialog inputUrlDialog = initUrlDialog(mRSSChannelList.getChannels().get(position).getUrl(),mRSSChannelList.getChannels().get(position).getDescription());
                    inputUrlDialog.show(getFragmentManager(), "TAG");
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive){
            Bitmap icon;
            Paint p = new Paint();
            if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE){
                View itemView = viewHolder.itemView;
                float height = (float) itemView.getBottom() - (float) itemView.getTop();
                float width = height / 3;
                if (dX > 0){
                    p.setColor(Color.parseColor("#388E3C"));
                    RectF background = new RectF((float)itemView.getLeft(), (float)itemView.getTop(), dX, (float)itemView.getBottom());
                    c.drawRect(background, p);
                    icon = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_swipe_edit);
                    RectF icon_dest = new RectF((float) itemView.getLeft() + width ,(float) itemView.getTop() + width,(float) itemView.getLeft()+ 2*width,(float)itemView.getBottom() - width);
                    c.drawBitmap(icon, null, icon_dest, p);
                } else {
                    p.setColor(Color.parseColor("#D32F2F"));
                    RectF background = new RectF((float)itemView.getRight() + dX, (float)itemView.getTop(), (float)itemView.getRight(), (float)itemView.getBottom());
                    c.drawRect(background, p);
                    icon = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_swipe_delete);
                    RectF icon_dest = new RectF((float) itemView.getRight() - 2*width ,(float) itemView.getTop() + width,(float) itemView.getRight() - width,(float)itemView.getBottom() - width);
                    c.drawBitmap(icon,null,icon_dest,p);
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        }
    }

}

