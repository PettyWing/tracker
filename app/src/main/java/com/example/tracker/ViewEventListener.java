package com.example.tracker;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

/**
 * @author youer
 * @date 2020/5/6
 */
public class ViewEventListener extends View.AccessibilityDelegate {

    public static final int FRAGMENT_TAG_KEY = 1;
    private static final String TAG = "ViewClickedEvent";
    private ViewScrollListener viewScrollListener = new ViewScrollListener();

    /**
     * 设置Activity页面中View的事件监听
     *
     * @param activity
     */
    public void setTracker(Activity activity) {
        // 找到根路径的View
        View contentView = activity.findViewById(android.R.id.content);
        if (contentView != null) {
            setViewTracker(contentView, null);
        }
    }

    /**
     * 设置Fragment页面中View的事件监听
     *
     * @param fragment
     */
    public void setTracker(Fragment fragment) {
        View contentView = fragment.getView();
        if (contentView != null) {
            setViewTracker(contentView, fragment);
        }
    }


    /**
     * 设置View上的事件监听
     *
     * @param view
     */
    public void setTracker(View view) {
        if (view != null) {
            setViewTracker(view, null);
        }
    }

    /**
     * 对每个View添加埋点的监听
     *
     * @param view
     * @param fragment
     */
    private void setViewTracker(View view, Fragment fragment) {
        if (needTracker(view)) {
            if (fragment != null) {
                view.setTag(FRAGMENT_TAG_KEY, fragment);
            }
            view.setAccessibilityDelegate(this);
        }
        if (view instanceof ViewGroup) {
            int childCount = ((ViewGroup) view).getChildCount();
            for (int i = 0; i < childCount; i++) {
                setViewTracker(((ViewGroup) view).getChildAt(i), fragment);
            }
            ((ViewGroup) view).setOnHierarchyChangeListener(new ViewGroup.OnHierarchyChangeListener() {
                @Override
                public void onChildViewAdded(View parent, View child) {
                    setTracker(parent);
                }

                @Override
                public void onChildViewRemoved(View parent, View child) {
                    setTracker(parent);
                }
            });
            if (needTracker(view)) {
                view.setAccessibilityDelegate(this);
                viewScrollListener.setScrollListener(view);
            }
            // TODO: 2020/5/25 添加对listView，RecycleView，ViewPager的监听
        }
    }

    /**
     * 判断view是否需要埋点，目前默认只要可以点击的都是true
     *
     * @param view
     * @return
     */
    private boolean needTracker(View view) {
        return true;
    }

    /**
     * 1. 需要clickable为true
     * 2. 事件的传递按照事件传递机制
     * 3.   Checkbox 完成
     * EditText 如何判断输入结束
     * Button 完成
     * viewGroup完成
     * 自定义View
     *
     * @param host
     * @param eventType
     */
    @Override
    public void sendAccessibilityEvent(View host, int eventType) {
        super.sendAccessibilityEvent(host, eventType);
//        AccessibilityEvent.TYPE_VIEW_CLICKED
        if (host instanceof CheckBox) {
            // 添加事件到数据库
            Log.d(TAG, "sendAccessibilityEvent: " + eventType + "--CheckBox:" + ((CheckBox) host).isChecked());
        } else if (host instanceof EditText) {
            Log.d(TAG, "sendAccessibilityEvent: " + eventType + "--EditText:" + ((EditText) host).getText());
        } else if (host instanceof TextView) {
            Log.d(TAG, "sendAccessibilityEvent: " + eventType + "--Button:" + ((TextView) host).getText());
        } else if (host instanceof ViewGroup) {
            Log.d(TAG, "sendAccessibilityEvent: " + eventType + "--LinearLayout:");
        }
        Log.d(TAG, "sendAccessibilityEvent: " + PathCreater.getPath(host));
    }

}