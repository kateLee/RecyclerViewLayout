package com.github.katelee.widget.recyclerviewlayout;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kate on 2015/6/1
 */
public class CustomRecyclerView extends RecyclerView {
    private List<OnChangeLayoutManagerListener> mOnChangeLayoutManagerListenerList;

    public CustomRecyclerView(Context context) {
        super(context);

        initialize();
    }

    public CustomRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);

        initialize();
    }

    public CustomRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        initialize();
    }

    private void initialize() {
        mOnChangeLayoutManagerListenerList = new ArrayList<OnChangeLayoutManagerListener>();
    }

    /**
     * @deprecated use {@link #setAdapter(CustomAdapter)} instead
     * @param adapter the adapter for view
     */
    @Override
    public void setAdapter(Adapter adapter) {

    }

    public void setAdapter(CustomAdapter adapter) {
        super.setAdapter(adapter);
    }

    public CustomAdapter getAdapter() {
        return (CustomAdapter) super.getAdapter();
    }

    @Override
    public void setLayoutManager(LayoutManager layout) {
        super.setLayoutManager(layout);

        for (OnChangeLayoutManagerListener onChangeLayoutManagerListener : mOnChangeLayoutManagerListenerList) {

            if (onChangeLayoutManagerListener != null) {
                onChangeLayoutManagerListener.onChange(layout);
            }
        }
    }

    public void setOnChangeLayoutManagerListener(OnChangeLayoutManagerListener listener) {
        mOnChangeLayoutManagerListenerList.set(0, listener);
    }

    public void addOnChangeLayoutManagerListener(OnChangeLayoutManagerListener listener) {
        mOnChangeLayoutManagerListenerList.add(listener);
    }

    public void removeOnChangeLayoutManagerListener(OnChangeLayoutManagerListener listener) {
        removeOnChangeLayoutManagerListener(mOnChangeLayoutManagerListenerList.indexOf(listener));
    }

    private void removeOnChangeLayoutManagerListener(int index) {
        if (index < 0 || index >= mOnChangeLayoutManagerListenerList.size()) {
            return;
        }
        if (index == 0) {
            setOnChangeLayoutManagerListener(null);
            return;
        }
        mOnChangeLayoutManagerListenerList.remove(index);
    }

    public interface OnChangeLayoutManagerListener {
        void onChange(LayoutManager layout);
    }
}
