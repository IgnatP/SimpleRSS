package com.ivpomazkov.simplerss;

/**
 * Created by Ivpomazkov on 16.06.2016.
 */
public interface ItemTouchHelperAdapter {
    void onItemMove(int fromPosition, int toPosition);
    void onItemDismiss(int position);
}
