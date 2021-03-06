/*
 * This file is part of LoLin1.
 *
 * LoLin1 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * LoLin1 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with LoLin1. If not, see <http://www.gnu.org/licenses/>.
 *
 * Created by Jorge Antonio Diaz-Benito Soriano.
 */

package org.jorge.cmp.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.jorge.cmp.R;
import org.jorge.cmp.datamodel.FeedArticle;
import org.jorge.cmp.io.database.SQLiteDAO;
import org.jorge.cmp.util.Interface;
import org.jorge.cmp.util.PicassoUtils;

import java.util.ArrayList;
import java.util.List;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.ViewHolder> {

    private final List<FeedArticle> items = new ArrayList<>();
    private final Context mContext;
    private int mDefaultImageId;
    private final Interface.IOnItemInteractionListener mCallback;
    private final Object mTag;
    private static final Object ADAPTER_RELOAD_LOCK = new Object();
    private final String mTableName;

    public FeedAdapter(Context context, Interface.IOnItemInteractionListener
            onItemSelectedListener, Integer defaultImageId, Object _tag, String tableName,
                       View initiallyLoadResponsibleView) {
        this.mContext = context;
        this.mDefaultImageId = defaultImageId;
        this.mCallback = onItemSelectedListener;
        mTag = _tag;
        mTableName = tableName;
        requestDataLoad(initiallyLoadResponsibleView);
    }

    public void requestDataLoad(View loadResponsibleView) {
        //Workaround for RecyclerView bug (https://code.google.com/p/android/issues/detail?id=77232)
        loadResponsibleView.postDelayed(new Runnable() {
            @Override
            public void run() {
                synchronized (ADAPTER_RELOAD_LOCK) {
                    items.clear();
                    List<FeedArticle> allArticles = SQLiteDAO.getInstance().getFeedArticlesFromTable
                            (mTableName);
                    for (FeedArticle thisArticle : allArticles) {
                        if (!items.contains(thisArticle)) {
                            items.add(thisArticle);
                        }
                    }
                    notifyDataSetChanged();
                }
            }
        }, 100);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout
                .list_item_feed_article, viewGroup, Boolean.FALSE);
        return new ViewHolder(v, this);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int i) {
        viewHolder.imageView.setImageDrawable(null);
        FeedArticle item = getItem(i);
        if (item != null) {
            if (!item.isRead()) {
                viewHolder.titleView.setTextAppearance(mContext,
                        R.style.FeedArticleOnListTitleUnread);
            } else {
                viewHolder.titleView.setTextAppearance(mContext,
                        R.style.FeedArticleOnListTitleRead);
            }
            final String title = item.getTitle();
            viewHolder.titleView.setText(title);
            if (viewHolder.imageView.getDrawable() == null) {
                PicassoUtils.loadInto(mContext, item.getImageUrl(), mDefaultImageId,
                        viewHolder.imageView, mTag);
                viewHolder.imageView.setContentDescription(title);
            }
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public FeedArticle getItem(Integer i) {
        if (i >= items.size())
            return null;
        return items.get(i);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ImageView imageView;
        private final TextView titleView;
        private final FeedAdapter mAdapter;

        public ViewHolder(View itemView, FeedAdapter adapter) {
            super(itemView);
            itemView.setOnClickListener(this);
            mAdapter = adapter;
            titleView = (TextView) itemView.findViewById(android.R.id.title);
            imageView = (ImageView) itemView.findViewById(android.R.id.icon);
        }

        @Override
        public void onClick(View v) {
            FeedArticle item = mAdapter.getItem(getPosition());
            if (item != null)
                mAdapter.mCallback.onItemClick(item);
        }
    }
}