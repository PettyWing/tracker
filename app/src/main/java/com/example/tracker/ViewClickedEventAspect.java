package com.example.tracker;

import android.util.Log;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;

/**
 * @author youer
 * @date 2020/5/13
 */
@Aspect
public class ViewClickedEventAspect {

    private static final String TAG = "ViewClickedEventAspect";

    @After("execution(* android.view.View.OnClickListener.onClick(android.view.View))")
    public void viewClicked(final ProceedingJoinPoint joinPoint) {
        /**
         * 保存点击事件
         */
        Log.d(TAG, "viewClicked: ");
    }
}