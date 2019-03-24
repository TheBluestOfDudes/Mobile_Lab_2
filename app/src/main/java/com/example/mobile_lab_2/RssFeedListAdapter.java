package com.example.mobile_lab_2;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class RssFeedListAdapter extends RecyclerView.Adapter<RssFeedListAdapter.FeedModelViewHolder> {

    private List<RssFeedModel> mRssFeedModels;
    private OnNoteListener mOnNoteListener;

    public static class FeedModelViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private View rssFeedView;
        OnNoteListener OnNoteListener;

        public FeedModelViewHolder(View v, OnNoteListener OnNoteListener) {
            super(v);
            rssFeedView = v;
            this.OnNoteListener = OnNoteListener;
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            OnNoteListener.onNoteClick(getAdapterPosition());
        }
    }

    public RssFeedListAdapter(List<RssFeedModel> rssFeedModels, OnNoteListener onNoteListener) {
        mRssFeedModels = rssFeedModels;
        this.mOnNoteListener = onNoteListener;

    }


    @Override
    public FeedModelViewHolder onCreateViewHolder(ViewGroup parent, int type) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_rss_feed, parent, false);
        FeedModelViewHolder holder = new FeedModelViewHolder(v, mOnNoteListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(FeedModelViewHolder holder, int position) {
        final RssFeedModel rssFeedModel = mRssFeedModels.get(position);
        ((TextView)holder.rssFeedView.findViewById(R.id.titleText)).setText(rssFeedModel.title);
        ((TextView)holder.rssFeedView.findViewById(R.id.descriptionText)).setText(rssFeedModel.description);
    }

    @Override
    public int getItemCount() {
        return mRssFeedModels.size();
    }

    public interface OnNoteListener {
        void onNoteClick(int position);
    }
}