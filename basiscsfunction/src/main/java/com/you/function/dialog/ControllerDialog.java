package com.you.function.dialog;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;

import com.you.function.dialog.base.BaseDialogFragment;

import java.util.Objects;

/**
 * 自定义Dialog
 */
public class ControllerDialog extends BaseDialogFragment {
    //Bundle数据
    private Controller controller;
    //view点击时间监听器
    private onClickListener clickListener;
    //初始化view
    private InitView initView;
    //dialog layout
    private int layoutResIds;

    public static ControllerDialog getInstance(Controller controller) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("controller", controller);
        ControllerDialog copyPicUpWxDialog = new ControllerDialog();
        copyPicUpWxDialog.setArguments(bundle);
        return copyPicUpWxDialog;
    }

    public ControllerDialog() {
    }

    @Override
    public int getContentView() {
        if (getArguments() == null && layoutResIds == 0) {
            dismissAllowingStateLoss();
            return 0;
        }
        controller = Objects.requireNonNull(getArguments()).getParcelable("controller");
        if (controller != null && controller.getLayoutResIds() != 0) {
            layoutResIds = controller.getLayoutResIds();
        }
        return layoutResIds;
    }

    @Override
    public void init() {
        assert controller != null;
        setCancelable(controller.getCancelable());
    }

    @Override
    public void initView() {
        initView.initView(this);
        for (int resid : controller.getOnClickResId()) {
            findViewById(resid).setOnClickListener(this);
        }
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissAllowingStateLoss();
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (clickListener != null) {
            clickListener.onClick(view);
        }
    }

    /**
     * 布局xml
     *
     * @param layoutResIds int
     */
    public void setLayoutResIds(int layoutResIds) {
        this.layoutResIds = layoutResIds;
    }

    /**
     * view点击事件监听器
     *
     * @param clickListener onClickListener
     */
    public void setClickListener(onClickListener clickListener) {
        this.clickListener = clickListener;
    }

    interface onClickListener {
        /**
         * View点击事件
         *
         * @param view View
         */
        void onClick(View view);
    }

    /**
     * 显示Dialog
     *
     * @param activity Activity
     * @return ControllerDialog
     */
    public ControllerDialog show(Activity activity) {
        FragmentTransaction fragmentTransaction = activity.getFragmentManager().beginTransaction();
        fragmentTransaction.add(this, "FragmentTransaction");
        fragmentTransaction.commitAllowingStateLoss();
        return this;
    }

    public ControllerDialog setInitView(InitView initView) {
        this.initView = initView;
        return this;
    }

    interface InitView {
        /**
         * 初始化View
         *
         * @param controllerDialog
         */
        void initView(ControllerDialog controllerDialog);
    }
}
