package com.github.katelee.widget.app;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.*;
import android.view.*;
import android.widget.TextView;
import com.github.katelee.widget.RecyclerViewLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kate on 2015/5/8
 */
public class RecyclerViewLayoutActivity extends ActionBarActivity {
    RecyclerViewLayout recyclerViewLayout;

    LinearLayoutManager linearLayoutManager;
    StaggeredGridLayoutManager staggeredGridLayoutManager;
    GridLayoutManager gridLayoutManager;
    DataAdapter mAdapter;
    View header;

    List<String> strings = new ArrayList<String>();
    Handler handler = new Handler();
    boolean isAdjustHeader = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_recycler_view_layout);

        recyclerViewLayout = (RecyclerViewLayout) findViewById(R.id.recyclerViewLayout);

        linearLayoutManager = new LinearLayoutManager(this);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        gridLayoutManager = new GridLayoutManager(this, 2);

        strings.add("0");
        strings.add("1");
        strings.add("2");
        strings.add("3");
        strings.add("4");
        strings.add("5");

        recyclerViewLayout.setLayoutManager(linearLayoutManager);
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
                        tmp.add("5");

                        strings.addAll(tmp);
                        mAdapter.notifyDataSetChanged();

                        recyclerViewLayout.setRefreshing(false);
                        mAdapter.enableLoadMore();
                    }
                }, 500);
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
                        tmp.add("6");
                        tmp.add("7");
                        tmp.add("8");
                        tmp.add("9");
                        tmp.add("10");
                        tmp.add("11");

                        strings.addAll(tmp);
                        mAdapter.notifyAdapterItemRangeInserted(strings.size() - tmp.size(), tmp.size());
                        mAdapter.setLoadingMore(false);

                        mAdapter.disableLoadMore();
                    }
                }, 500);
            }
        });
        mAdapter.enableLoadMore();

        if (isAdjustHeader) {
            recyclerViewLayout.setAdjustHeaderView(header = LayoutInflater.from(this).inflate(R.layout.view_header, null));
            header.findViewById(R.id.linearlayout).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recyclerViewLayout.setLayoutManager(linearLayoutManager);
                }
            });
            header.findViewById(R.id.gridlayout).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recyclerViewLayout.setLayoutManager(gridLayoutManager);
                }
            });
            header.findViewById(R.id.staggeredgridlayout).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recyclerViewLayout.setLayoutManager(staggeredGridLayoutManager);
                }
            });
        }
    }

    private class DataAdapter extends RecyclerViewLayout.Adapter<DataHolder> {
        @Override
        protected View onHeaderCreateView(ViewGroup viewGroup) {
            if (!isAdjustHeader) {
                return LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_header, viewGroup, false);
            }
            return super.onHeaderCreateView(viewGroup);
        }

        @Override
        protected View onLoadMoreCreateView(ViewGroup viewGroup) {
            return LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_loadmore, viewGroup, false);
        }

        @Override
        protected View onFooterCreateView(ViewGroup viewGroup) {
            return LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_footer, viewGroup, false);
        }

        @Override
        protected DataHolder onAdapterCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_item, viewGroup, false);
            return new DataHolder(view);
        }

        @Override
        protected void onAdapterBindViewHolder(DataHolder viewHolder, int position) {
            viewHolder.label.setText("position: " + strings.get(position));
            ViewGroup.LayoutParams layoutParams = viewHolder.itemView.getLayoutParams();
            layoutParams.height = 150 + (position + 1) * 20;
            viewHolder.itemView.setLayoutParams(layoutParams);
        }

        @Override
        public int getAdapterItemCount() {
            return strings.size();
        }

        @Override
        protected void onHeaderBindViewHolder(RecyclerView.ViewHolder viewHolder) {
            super.onHeaderBindViewHolder(viewHolder);

            if (!isAdjustHeader) {
                viewHolder.itemView.findViewById(R.id.linearlayout).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        recyclerViewLayout.setLayoutManager(linearLayoutManager);
                    }
                });
                viewHolder.itemView.findViewById(R.id.gridlayout).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        recyclerViewLayout.setLayoutManager(gridLayoutManager);
                    }
                });
                viewHolder.itemView.findViewById(R.id.staggeredgridlayout).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        recyclerViewLayout.setLayoutManager(staggeredGridLayoutManager);
                    }
                });
            }
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
