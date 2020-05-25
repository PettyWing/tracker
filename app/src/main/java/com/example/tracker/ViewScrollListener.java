package com.example.tracker;

import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author youer
 * @date 2020/5/25
 */
public class ViewScrollListener {
    private static final String TAG = "ViewScrollListener";

    /**
     * 添加滑动监听
     *
     * @param view
     */
    public void setScrollListener(View view) {
        if (view instanceof RecyclerView) {
            ((RecyclerView) view).addOnScrollListener(new RecyclerView.OnScrollListener() {

                int scrollerState = 0;
                int scrollerY = 0;

                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    scrollerState = newState;
                    switch (newState) {
                        case RecyclerView.SCROLL_STATE_IDLE:
                            // 当页面滚动停止的时候进行数据上报，并且置零参数1
                            Log.d(TAG, "onScrollStateChanged: " + scrollerY);
                            scrollerY = 0;
                            break;
                        case RecyclerView.SCROLL_STATE_DRAGGING:
                        default:
                            break;
                    }
                }

                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    // 当滚动状态为1或者2的时候，移动距离进行叠加。
                    if (scrollerState == RecyclerView.SCROLL_STATE_DRAGGING || scrollerState == RecyclerView.SCROLL_STATE_SETTLING) {
                        scrollerY += dy;
                    }
                }
            });
        }
    }
}
