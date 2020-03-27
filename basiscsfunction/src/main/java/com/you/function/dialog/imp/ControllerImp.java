package com.you.function.dialog.imp;

import android.annotation.SuppressLint;

@SuppressLint("ParcelCreator")
public interface ControllerImp {
    /**
     *dialog xml layout
     * @return int
     */
    int getLayoutResIds();

    /**
     * view xml id
     * @return int[]
     */
    int[] getOnClickResId();

    /**
     * 点击外部是否关闭dialog
     * @return boolean
     */
    boolean getCancelable();

}
