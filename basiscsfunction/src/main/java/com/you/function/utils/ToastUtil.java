package com.you.function.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.you.function.Function;
import com.you.function.R;

/**
 * Created by kaiwang on 2017/11/17.
 */

public class ToastUtil {

    public static void showToast(Context context, int resId) {
        show(context, Function.appLicationContext.getResources().getString(resId), Toast.LENGTH_SHORT);
    }
    public static void showToast(Context context, String text) {
        show(context,text, Toast.LENGTH_SHORT);
    }
    public static void showLongToast(Context context, int resId) {
        show(context,Function.appLicationContext.getResources().getString(resId), Toast.LENGTH_LONG);
    }
    public static void showLongToast(Context context, String text) {
        show(context,text, Toast.LENGTH_LONG);
    }
    public static void showToast(Context context, int resId, boolean isSystem) {
        if (isSystem)
            systemShow(context,Function.appLicationContext.getResources().getString(resId), Toast.LENGTH_SHORT);
        else
            show(context,Function.appLicationContext.getResources().getString(resId), Toast.LENGTH_SHORT);

    }


    private static void systemShow(Context context, String text, int showTimeLong){
        Toast.makeText(context,text,showTimeLong);
    }
    /**
     * 自定义 toast
     * @param context
     * @param messages
     * @param showTimeLong
     */
    private static void show(Context context, String messages, int showTimeLong){
        if (context == null){
            return;
        }
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.view_toast, null);
        TextView messagesTv = view.findViewById(R.id.view_toast_tv);
        messagesTv.setGravity(Gravity.CENTER);
        messagesTv.setText(messages);
        Toast toast = new Toast(Function.appLicationContext);
        toast.setGravity(Gravity.CENTER, 12, 20);
        toast.setDuration(showTimeLong);
        toast.setView(view);
        toast.show();
    }
}
