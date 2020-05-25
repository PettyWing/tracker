package com.example.tracker;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.widget.ContentFrameLayout;
import androidx.fragment.app.Fragment;

/**
 * @author youer
 * @date 2020/5/6
 */
public class ViewClickedEventListener extends View.AccessibilityDelegate {

    public static final int FRAGMENT_TAG_KEY = 1;
    private static final String TAG = "ViewClickedEvent";

    /**
     * 设置Activity页面中View的事件监听
     *
     * @param activity
     */
    public void setActivityTracker(Activity activity) {
        View contentView = activity.findViewById(android.R.id.content);
        if (contentView != null) {
            setViewClickedTracker(contentView, null);
        }
    }

    /**
     * 设置viewHolder页面中View的事件监听
     *
     * @param view
     */
    public void setTracker(View view) {
        if (view != null) {
            setViewClickedTracker(view, null);
        }
    }

    /**
     * 对每个View添加埋点的监听
     *
     * @param view
     * @param fragment
     */
    private void setViewClickedTracker(View view, Fragment fragment) {
        if (needTracker(view)) {
            if (fragment != null) {
                view.setTag(FRAGMENT_TAG_KEY, fragment);
            }
            view.setAccessibilityDelegate(this);
        }
        if (view instanceof ViewGroup) {
            int childCount = ((ViewGroup) view).getChildCount();
            for (int i = 0; i < childCount; i++) {
                setViewClickedTracker(((ViewGroup) view).getChildAt(i), fragment);
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
            }
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
        getPath(host);

    }

    /**
     * 获取view的viewTree
     * 优化点：
     * 如果这个item有id，则返回id，如果没有，则返回他在ViewTree的第几个
     *
     * @param view
     */
    private void getPath(View view) {
        String path = "";
        do {
            //1. 构造ViewPath中于view对应的节点:ViewType[index]
            String viewType;
            try {
                viewType = view.getResources().getResourceEntryName(view.getId());
            } catch (Exception e) {
                viewType = view.getClass().getSimpleName();
                int index = indexOfChild((ViewGroup) view.getParent(), view);
                viewType = viewType + "[" + index + "]";
            }

            path = "/" + viewType + path;
        } while (!((view = (View) view.getParent()) instanceof ContentFrameLayout));//2. 将view指向上一级的节点
        Log.d(TAG, "getPath: " + "rootView" + path);
    }


    /**
     * 获取子view在viewTree的第几个
     * 优化点
     * 1：index从"兄弟节点的第几个”优化为:“相同类型兄弟节点的第几个
     *
     * @param parent
     * @param child
     * @return
     */
    private int indexOfChild(ViewGroup parent, View child) {
        final int count = parent.getChildCount();
        int j = 0;
        for (int i = 0; i < count; i++) {
            View view = parent.getChildAt(i);
            if (child.getClass().isInstance(view)) {
                if (view == child) {
                    return j;
                }
                j++;
            }
        }
        return -1;
    }
}