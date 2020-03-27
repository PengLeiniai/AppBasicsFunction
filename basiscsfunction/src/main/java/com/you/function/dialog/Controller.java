package com.you.function.dialog;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

import com.you.function.dialog.imp.ControllerImp;

@SuppressLint("ParcelCreator")
public abstract class Controller implements Parcelable, ControllerImp {
    int[] getOnClickResId(int... onClickResIds) {
        return onClickResIds == null ? new int[]{} : onClickResIds;
    }
    @Override
    public int[] getOnClickResId() {
        return new int[0];
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }

    @Override
    public boolean getCancelable() {
        return false;
    }
}
