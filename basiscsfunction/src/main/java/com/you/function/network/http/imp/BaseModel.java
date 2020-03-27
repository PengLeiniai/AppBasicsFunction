package com.you.function.network.http.imp;

import android.os.Parcelable;

/**
 * Created by you on 18/5/3.
 */

public abstract class BaseModel implements Parcelable {
    private String string;

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }
}
