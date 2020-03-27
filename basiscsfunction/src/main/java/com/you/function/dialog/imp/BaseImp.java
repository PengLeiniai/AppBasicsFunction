package com.you.function.dialog.imp;

/**
 * Created by apple on 18/4/16.
 */

public interface BaseImp {
    int getContentView();
    void init();
    void initView();
    void initListener() throws NoSuchFieldException;
}
