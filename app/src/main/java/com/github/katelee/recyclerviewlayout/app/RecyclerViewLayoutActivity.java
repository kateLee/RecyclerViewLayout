package com.github.katelee.recyclerviewlayout.app;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.*;
import android.widget.TextView;
import com.github.katelee.recyclerviewlayout.RecyclerViewLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kate on 2015/5/8
 */
public class RecyclerViewLayoutActivity extends ActionBarActivity {
    Toolbar toolbar;
    RecyclerViewLayout recyclerViewLayout;

    LinearLayoutManager linearLayoutManager;
    StaggeredGridLayoutManager staggeredGridLayoutManager =
            new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
    DataAdapter mAdapter;

    List<String> strings = new ArrayList<String>();
    android.os.Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_recycler_view_layout);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        recyclerViewLayout = (RecyclerViewLayout) findViewById(R.id.recyclerViewLayout);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        strings.add("0");
        strings.add("1");
        strings.add("2");
        strings.add("3");
        strings.add("4");

        recyclerViewLayout.setLayoutManager(linearLayoutManager = new LinearLayoutManager(this));
        recyclerViewLayout.setAdapter(mAdapter = new DataAdapter());

        recyclerViewLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //simulate request, cause recyclerview is measuring size.
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        strings.clear();

                        List<String> tmp = new ArrayList<String>();
                        tmp.add("0");
                        tmp.add("1");
                        tmp.add("2");
                        tmp.add("3");
                        tmp.add("4");

                        strings.addAll(tmp);
                        mAdapter.notifyDataSetChanged();

                        recyclerViewLayout.setRefreshing(false);
                        mAdapter.enableLoadMore();
                    }
                }, 100);
            }
        });
        mAdapter.setOnLoadMoreListener(new DataAdapter.OnLoadMoreListener() {
            @Override
            public void loadMore() {
                //simulate request, cause recyclerview is measuring size.
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        List<String> tmp = new ArrayList<String>();
                        tmp.add("5");
                        tmp.add("6");
                        tmp.add("7");
                        tmp.add("8");

                        strings.addAll(tmp);
                        mAdapter.notifyItemRangeInserted(strings.size() - tmp.size(), tmp.size());

                        recyclerViewLayout.disableLoadMore();
                    }
                }, 100);
            }
        });
        mAdapter.setLoadMoreView(LayoutInflater.from(this).inflate(R.layout.view_loadmore, null));
        mAdapter.setHeaderView(LayoutInflater.from(this).inflate(R.layout.view_header, null));
        mAdapter.setFooterView(LayoutInflater.from(this).inflate(R.layout.view_footer, null));
//        recyclerViewLayout.setAdjustHeaderView(LayoutInflater.from(this).inflate(R.layout.view_header, null));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.switch_mode, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.option_switch_mode:
                if (recyclerViewLayout.getLayoutManager() instanceof LinearLayoutManager) {
                    recyclerViewLayout.setLayoutManager(staggeredGridLayoutManager);
                }
                else {
                    recyclerViewLayout.setLayoutManager(linearLayoutManager);
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class DataAdapter extends RecyclerViewLayout.Adapter<DataHolder> {
    @Override
        protected DataHolder onAdapterCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_item, viewGroup, false);
            return new DataHolder(view);
        }

        @Override
        protected void onAdapterBindViewHolder(DataHolder viewHolder, int position) {
            viewHolder.label.setText("position: " + strings.get(position));
            ViewGroup.LayoutParams layoutParams = viewHolder.itemView.getLayoutParams();
            layoutParams.height = position * 100;
            viewHolder.itemView.setLayoutParams(layoutParams);
        }

        @Override
        public int getAdapterItemCount() {
            return strings.size();
        }
    }

    private class DataHolder extends RecyclerView.ViewHolder {
        TextView label;

        public DataHolder(View itemView) {
            super(itemView);

            label = (TextView) itemView.findViewById(R.id.label);
        }
    }
}
