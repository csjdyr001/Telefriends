package com.cfks.telefriends.recycle_views;

import android.support.annotation.IdRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.cfks.telefriends.R;

/**
 * Created by Kyrylo Avramenko on 6/12/2018.
 */
public class ViewHolderSubTitle extends BasicViewHolder {

    private final TextView title, subTitle;

    public ViewHolderSubTitle(View itemView) {
        super(itemView);

        title = itemView.findViewById(R.id.text_info);
        subTitle = itemView.findViewById(R.id.label_about);
    }

    public TextView getTitle() {
        return title;
    }

    public TextView getSubTitle() {
        return subTitle;
    }


}
