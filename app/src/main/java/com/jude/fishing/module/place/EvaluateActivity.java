package com.jude.fishing.module.place;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.ViewGroup;

import com.github.clans.fab.FloatingActionButton;
import com.jude.beam.bijection.RequiresPresenter;
import com.jude.beam.expansion.list.BeamListActivity;
import com.jude.beam.expansion.list.ListConfig;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.fishing.R;
import com.jude.fishing.model.AccountModel;
import com.jude.fishing.model.entities.Evaluate;
import com.jude.fishing.module.user.LoginActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by zhuchenxi on 15/10/3.
 */
@RequiresPresenter(EvaluatePresenter.class)
public class EvaluateActivity extends BeamListActivity<EvaluatePresenter, Evaluate> {
    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.recycler)
    EasyRecyclerView recycler;
    @InjectView(R.id.write)
    FloatingActionButton write;

    @Override
    protected BaseViewHolder getViewHolder(ViewGroup viewGroup, int i) {
        return new EvaluateViewHolder(viewGroup);
    }

    @Override
    public int getLayout() {
        return R.layout.place_activity_evaluate;
    }

    @Override
    protected ListConfig getConfig() {
        return super.getConfig().setLoadmoreAble(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.inject(this);
        write.setOnClickListener(v->{
            if (AccountModel.getInstance().getAccount()==null){
                startActivity(new Intent(this, LoginActivity.class));
                return;
            }
            Intent i = new Intent(this,WriteEvaluateActivity.class);
            i.putExtra("id",getIntent().getIntExtra("id",0));
            startActivityForResult(i, 10086);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10086 && resultCode == Activity.RESULT_OK){
            getListView().getSwipeToRefresh().setRefreshing(true);
            getPresenter().onRefresh();
        }
    }
}
