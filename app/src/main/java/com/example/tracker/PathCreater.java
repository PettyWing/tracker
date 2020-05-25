package com.example.tracker;

import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.ContentFrameLayout;

/**
 * @author youer
 * @date 2020/5/25
 */
public class PathCreater {

    /**
     * 获取view的viewTree
     * 优化点：
     * 如果这个item有id，则返回id，如果没有，则返回他在ViewTree的第几个
     *
     * @param view
     */
    public static String getPath(View view) {
        String totalPath = "";
        do {
            //1. 构造ViewPath中于view对应的节点:ViewType[index]
            String viewPath;
            try {
                viewPath = getPathById(view);
            } catch (Exception e) {
                viewPath = getPathByViewTree(view);
            }
            viewPath = viewPath + "[" + indexOfChild((ViewGroup) view.getParent(), view) + "]";
            totalPath = "/" + viewPath + totalPath;
        } while (!((view = (View) view.getParent()) instanceof ContentFrameLayout));//2. 将view指向上一级的节点
        return "rootView" + totalPath;
    }

    /**
     * 通过id来生成Path
     *
     * @param view
     * @return
     */
    private static String getPathById(View view) {
        return view.getResources().getResourceEntryName(view.getId());
    }

    /**
     * 通过ViewTree来生成Path
     *
     * @param view
     * @return
     */
    private static String getPathByViewTree(View view) {
        return view.getClass().getSimpleName();
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
    private static int indexOfChild(ViewGroup parent, View child) {
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
