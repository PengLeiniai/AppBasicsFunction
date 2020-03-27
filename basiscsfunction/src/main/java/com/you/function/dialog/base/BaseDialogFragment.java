package com.you.function.dialog.base;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.you.function.dialog.imp.BaseImp;

/**
 * 所有DialogFragment必须继承该类
 */

public abstract class BaseDialogFragment extends DialogFragment implements BaseImp, View.OnClickListener {
    public View rootView;
    private Context activity;

    public Context getContext() {
        return activity;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            onCurrentAttach(context);
        } else {
            activity = context;
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            onCurrentAttach(activity);
        } else {
            this.activity = activity;
        }
    }

    protected void onCurrentAttach(Context mContext){
        activity = mContext;
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        assert window != null;
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics( dm );

        ViewGroup.LayoutParams params = window.getAttributes();
        //params.gravity = Gravity.BOTTOM;
        // 使用ViewGroup.LayoutParams，以便Dialog 宽度充满整个屏幕
        params.width =  ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        window.setAttributes((WindowManager.LayoutParams) params);
    }

    @Override
    public void onClick(View view) {
        Log.d("", "");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = inflater.inflate(getContentView(), container);
        init();
        initView();
        initListener();
        return rootView;
    }

    @Override
    public void init() {
        Log.i("","");
    }

    @Override
    public void initView() {
        Log.i("","");
    }

    @Override
    public void initListener() {
        Log.i("","");
    }

    public View findViewById(int resId){
        return rootView.findViewById(resId);
    }
}
