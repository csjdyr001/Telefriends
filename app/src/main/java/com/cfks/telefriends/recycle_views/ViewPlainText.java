package com.cfks.telefriends.recycle_views;

import android.support.annotation.IdRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.cfks.telefriends.R;

/**
 * Created by Kyrylo Avramenko on 6/12/2018.
 */
public class ViewPlainText extends BasicViewHolder {

    private TextView title;

    public ViewPlainText(View itemView) {
        super(itemView);
        title = itemView.findViewById(R.id.txt_plain_text);
    }

    public TextView getTitle() {
        return title;
    }

    public void setTitle(TextView title) {
        this.title = title;
    }

}
