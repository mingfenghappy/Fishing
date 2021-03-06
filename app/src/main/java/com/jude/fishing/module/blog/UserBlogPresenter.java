package com.jude.fishing.module.blog;

import android.os.Bundle;

import com.jude.beam.expansion.list.BeamListActivityPresenter;
import com.jude.fishing.model.BlogModel;
import com.jude.fishing.model.entities.Seed;

/**
 * Created by Mr.Jude on 2015/9/19.
 */
public class UserBlogPresenter extends BeamListActivityPresenter<UserBlogActivity,Seed> {
    int id;

    @Override
    protected void onCreate(UserBlogActivity view, Bundle savedState) {
        super.onCreate(view, savedState);
        id = getView().getIntent().getIntExtra("id",0);
        onRefresh();
    }

    @Override
    public void onRefresh() {
        BlogModel.getInstance().getUserBlog(id,0).unsafeSubscribe(getRefreshSubscriber());
    }

    @Override
    public void onLoadMore() {
        BlogModel.getInstance().getUserBlog(id,getCurPage()).unsafeSubscribe(getMoreSubscriber());
    }
}
