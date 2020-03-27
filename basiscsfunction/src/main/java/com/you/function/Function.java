package com.you.function;

import android.content.Context;

public class Function {
    public static Context appLicationContext;
    /**
     * 初始化 Function
     * @param context Context
     */
    public static void init(Context context){
        appLicationContext = context;
    }
}
