package com.you.function.network.http.utlis;

import android.Manifest;
import android.accounts.NetworkErrorException;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AppOpsManager;
import android.app.Application;
import android.app.NotificationManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Outline;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import com.you.function.Function;
import com.you.function.R;
import com.you.function.cache.InfoCache;
import com.you.function.utils.ToastUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.NetworkInterface;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.UUID;
import static android.content.Context.CLIPBOARD_SERVICE;

/**
 * Created by wangkai on 2017/10/21.
 */

public class CommonUtil {
    public static Point sPoint;
    //设备id
    public static String DEVICE_ID = "DEVICE_ID";

    /**
     * 获取屏幕宽高
     *
     * @param context
     * @return
     */
    public static Point getScreenProperty(Context context) {
        if (sPoint != null) {
            return sPoint;
        }
        if (context == null) {
            return null;
        }
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;// 屏幕宽度（像素）
        int height = dm.heightPixels; // 屏幕高度（像素）

        if (width > 0 && height > 0) {
            Point point = new Point();
            point.x = width;
            point.y = height;
            sPoint = point;
            return point;
        }

        return null;
    }

    public static Point getScreenProperty() {
        if (sPoint != null) {
            return sPoint;
        }
        WindowManager wm = (WindowManager) Function.appLicationContext.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;// 屏幕宽度（像素）
        int height = dm.heightPixels; // 屏幕高度（像素）

        if (width > 0 && height > 0) {
            Point point = new Point();
            point.x = width;
            point.y = height;
            sPoint = point;
            return point;
        }

        return null;
    }

    /**
     * 获取本地图片宽高
     *
     * @param path String
     * @return Point
     */
    public static Point getImageLenght(String path) {
        if (TextUtils.isEmpty(path)) {
            return null;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(path, options);
        Point point = new Point();
        point.x = options.outWidth;
        point.y = options.outHeight;
        if (bitmap != null) {
            bitmap.recycle();
        }
        return point;
    }

    /**
     * 根据手机分辨率从DP转成PX
     *
     * @param context Context
     * @param dpValue float
     * @return int
     */
    public static int dip2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int dip2px(float dpValue) {
        float scale = Function.appLicationContext.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 将sp值转换为px值
     *
     * @param spValue float
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 根据手机的分辨率PX(像素)转成DP
     *
     * @param context Context
     * @param pxValue float
     * @return int
     */
    public static int px2dip(Context context, float pxValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将px值转换为sp值
     *
     * @param pxValue float
     * @return int
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    public static void copy(Context context, String text, boolean toast) {
        if (TextUtils.isEmpty(text)) return;
        copy(context, text, null);
        if (toast) {
            ToastUtil.showToast(context, R.string.toast_copy);
        }
    }
    public static void copy(Context context, String text) {
        copy(context, text, null);
    }

    /**
     * 给剪贴板添加内容
     * @param context Context
     * @param text CharSequence
     */
    public static void copy(Context context, String text, String toast) {
        if (TextUtils.isEmpty(text)) return;
        ClipboardManager cmb = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
        assert cmb != null;
        cmb.setPrimaryClip(ClipData.newPlainText("text", text));
        if (!TextUtils.isEmpty(toast)) {
            ToastUtil.showToast(context, toast);
        }
    }

    /**
     * 获取剪切板的内容。
     */
    public static String getPrimaryClip(Context context) {
        ClipboardManager manager = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
        if (manager == null) {
            return "";
        }
        ClipData data = manager.getPrimaryClip();
        if (data == null || data.getItemCount() <= 0) {
            return "";
        }
        ClipData.Item item = data.getItemAt(0);
        return item != null && item.getText() != null ? item.getText().toString() : "";
    }

    /**
     * 删除文件
     *
     * @param path String
     */
    public static void deleteFile(String path) {
        if (!TextUtils.isEmpty(path)) {
            List<String> list = new ArrayList<>();
            list.add(path);
            deleteFile(list);
        }
    }

    /**
     * 删除文件
     *
     * @param pathList List<String>
     */
    public static void deleteFile(List<String> pathList) {
        if (pathList != null && pathList.size() > 0) {
            for (String path : pathList) {
                if (!TextUtils.isEmpty(path)) {
                    File file = new File(path);
                    if (file.exists() && file.isFile()) {
                        file.delete();
                    }
                }
            }
        }
    }

    public static String getCDN(String imageUrl, int cdnWidth, int cdnHeight) {
        return imageUrl + "?x-oss-process=image/resize,m_lfit,h_" + cdnWidth + ",w_" + cdnHeight;
    }

    /**
     * 隐藏键盘
     *
     * @param editText
     * @param context
     */
    public static void closeSoftKeybord(EditText editText, Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    /**
     * 获取手机相册路径
     *
     * @return
     */
    public static String getCameraPath() {
        String cameraPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/Camera/";
        File file = new File(cameraPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        return cameraPath;
    }

    /**
     * 获取手微信相册路径
     *
     * @return
     */
    public static String getWXSaveImagePath() {
        String cameraPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/tencent/MicroMsg/WeiXin";
        File file = new File(cameraPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        return cameraPath;
    }
    /**
     * 更新系统图库
     *
     * @param imgPath String
     */
    public static void sendImageChangeBroadcast(String imgPath) {
        if (TextUtils.isEmpty(imgPath)) return;
        File file = new File(imgPath);
        if (file.exists() && file.isFile()) {
            MediaScannerConnection.scanFile(Function.appLicationContext, new String[]{file.getAbsolutePath()}, null, null);
        }
        Log.e("TAG", "获取相册时间1：" + System.currentTimeMillis());
        //更新系统图库保存时间
    }

    /**
     * 获取系统相册路径
     */
    public static String getServicePhonePath() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath();
    }

    /**
     * 加密手机号
     *
     * @param phone String
     * @return String
     */
    public static String getPhoneEncrypt(String phone) {
        if (!TextUtils.isEmpty(phone) && phone.length() == 11) {
            return phone.substring(0, 3) + "****" + phone.substring(7, phone.length());
        }
        return phone;
    }

    /**
     * 复制文件
     *
     * @param oldFile String
     * @param newFile String
     * @param isImage  boolean
     */
    public static void copyFile(String oldFile, String newFile, boolean isImage) {
        FileInputStream fis = null;
        FileOutputStream fos = null;
        try {
            fis = new FileInputStream(oldFile);
            fos = new FileOutputStream(newFile);
            byte[] buf = new byte[1024];
            int by = 0;
            while ((by = fis.read(buf)) != -1) {
                fos.write(buf, 0, by);
            }
            if (isImage) {
                sendImageChangeBroadcast(newFile);
            }
            fis.close();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 金额转化
     *
     * @param money
     * @return
     */
    public static String getNumber(Integer money) {
        if (money == null || money == 0) {
            return "0";
        }
        String result;
        try {
            if (String.valueOf(money).length() >= 10) {
                result = String.valueOf(money);
                StringBuilder stringBuilde = new StringBuilder();
                stringBuilde.append(result);
                stringBuilde.insert(result.length() - 2, ".");
                result = stringBuilde.toString().replaceAll("0+?$", "");//去掉后面无用的零
                result = result.replaceAll("[.]$", "");//如小数点后面全是零则去掉小数点
                return result;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        DecimalFormat df = new DecimalFormat("0.00");
        result = df.format((double) money / 100);
        result = result.replaceAll("0+?$", "");//去掉后面无用的零
        result = result.replaceAll("[.]$", "");//如小数点后面全是零则去掉小数点
        return result;
    }

    /**
     * 金额转化
     *
     * @param money
     * @return
     */
    public static String getNumber(Long money) {
        if (money == null || money == 0) {
            return "0";
        }
        String result;
        try {
            if (String.valueOf(money).length() >= 10) {
                result = String.valueOf(money);
                StringBuilder stringBuilde = new StringBuilder();
                stringBuilde.append(result);
                stringBuilde.insert(result.length() - 2, ".");
                result = stringBuilde.toString().replaceAll("0+?$", "");//去掉后面无用的零
                result = result.replaceAll("[.]$", "");//如小数点后面全是零则去掉小数点
                return result;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        DecimalFormat df = new DecimalFormat("0.00");
        result = df.format((float) money / 100);
        result = result.replaceAll("0+?$", "");//去掉后面无用的零
        result = result.replaceAll("[.]$", "");//如小数点后面全是零则去掉小数点
        return result;
    }

    /**
     * 获取设备id
     *
     * @return
     */
    public static String getDeviceId() {
        // 先从本地取
        String uniqueId = InfoCache.getInstance().getCacheData(DEVICE_ID);
        if (!TextUtils.isEmpty(uniqueId)) return uniqueId;
        // 先判断有没有权限
        if (ActivityCompat.checkSelfPermission(Function.appLicationContext, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            uniqueId = Settings.System.getString(Function.appLicationContext.getContentResolver(), Settings.System.ANDROID_ID);
        } else {
            uniqueId = ((TelephonyManager) Function.appLicationContext.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
        }

        if (TextUtils.isEmpty(uniqueId)) {
            uniqueId = UUID.randomUUID().toString();
        }
        InfoCache.getInstance().saveData(DEVICE_ID,uniqueId);
        return uniqueId;
    }

    /**
     * MD5加密
     *
     * @param info String
     * @return String
     */
    public static String getMD5(String info) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(info.getBytes("UTF-8"));
            byte[] encryption = md5.digest();

            StringBuilder strBuf = new StringBuilder();
            for (byte anEncryption : encryption) {
                if (Integer.toHexString(0xff & anEncryption).length() == 1) {
                    strBuf.append("0").append(Integer.toHexString(0xff & anEncryption));
                } else {
                    strBuf.append(Integer.toHexString(0xff & anEncryption));
                }
            }

            return strBuf.toString();
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            return "";
        }
    }

    /**
    /**
     * 获取Drawable
     *
     * @return
     */
    public static Drawable getDrawable(int resId) {
        Drawable drawable = Function.appLicationContext.getResources().getDrawable(resId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        return drawable;

    }

    public static Drawable getDrawable(Drawable drawable) {
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        return drawable;

    }

    /**
     * 拨打电话（跳转到拨号界面，用户手动点击拨打）
     *
     * @param phoneNum 电话号码
     */
    public static void callPhone(Activity activity, String phoneNum) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        activity.startActivity(intent);
    }

    /**
    /**
     * 判断设备是否安装淘宝app
     *
     * @return boolean
     */
    public static boolean isPkgInstalledTb() {
        return isAppInstall("com.taobao.taobao");
    }

    /**
     * 判断设备是否安装微信
     *
     * @return boolean
     */
    public static boolean isPkgInstalledWX() {
        return isAppInstall("com.tencent.mm");
    }

    /**
     * 判断是否安装凭多多
     *
     * @return boolean
     */
    public static boolean isPkgInstalledPDD() {
        return isAppInstall("com.xunmeng.pinduoduo");
    }

    /**
     * 判断设备是否安装QQ
     *
     * @return boolean
     */
    public static boolean isPkgInstalledQQ() {
        return isAppInstall("com.tencent.mobileqq");
    }

    public static boolean isAppInstall(String packageName) {
        android.content.pm.ApplicationInfo info = null;
        try {
            info = Function.appLicationContext.getPackageManager().getApplicationInfo(packageName, 0);
            return info != null;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 打开微信
     */
    public static void openWx(Context context) {
        if (isPkgInstalledWX()) {
            openWXClient(context);
        } else {
            ToastUtil.showToast(Function.appLicationContext, "您的手机没有安装微信");
        }
    }

    public static void openQQ(Context context) {
        if (isPkgInstalledQQ()) {
            openQQClient(context);
        } else {
            ToastUtil.showToast(context, "您的手机没有安装QQ");
        }
    }

    /**
     * 打开微信客户端
     */
    private static void openWXClient(Context context) {
        Intent intent = new Intent();
        ComponentName cmp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI");
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setComponent(cmp);
        context.startActivity(intent);
    }

    /**
     * 打开QQ客户端
     */
    private static void openQQClient(Context context) {
        Intent intent = new Intent();
        ComponentName cmp = new ComponentName("com.tencent.mobileqq", "com.tencent.mobileqq.activity.SplashActivity");
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setComponent(cmp);
        context.startActivity(intent);
    }

    /**
     * 获取屏幕密度
     *
     * @param context
     * @return
     */
    public static float getScreenDensity(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }

    /**
     * 身份证格式校验
     *
     * @param num
     * @return
     */
    public static boolean verForm(String num) {
        String reg = "^\\d{15}$|^\\d{17}[0-9X]$";
        if (!num.matches(reg)) {
            System.out.println("Format Error!");
            return false;
        }
        return true;
    }


    /**
     * 是否使用代理(WiFi状态下的,避免被抓包)
     */
    public static boolean isWifiProxy(Context context) {
        @SuppressLint("ObsoleteSdkInt") final boolean is_ics_or_later = Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH;
        String proxyAddress;
        int proxyPort;
        if (is_ics_or_later) {
            proxyAddress = System.getProperty("http.proxyHost");
            String portstr = System.getProperty("http.proxyPort");
            proxyPort = Integer.parseInt((portstr != null ? portstr : "-1"));
            System.out.println(proxyAddress + "~");
            System.out.println("port = " + proxyPort);
        } else {
            proxyAddress = android.net.Proxy.getHost(context);
            proxyPort = android.net.Proxy.getPort(context);
            Log.e("address = ", proxyAddress + "~");
            Log.e("port = ", proxyPort + "~");
        }
        return (!TextUtils.isEmpty(proxyAddress)) && (proxyPort != -1);
    }

    /**
     * 是否正在使用VPN
     */
    public static boolean isVpnUsed() {
        try {
            Enumeration niList = NetworkInterface.getNetworkInterfaces();
            if (niList != null) {
                ArrayList<NetworkInterface> interfaceArrayList = Collections.list(niList);
                for (NetworkInterface intf : interfaceArrayList) {
                    if (!intf.isUp() || intf.getInterfaceAddresses().size() == 0) {
                        continue;
                    }
                    Log.d("-----", "isVpnUsed() NetworkInterface Name: " + intf.getName());
                    if ("tun0".equals(intf.getName()) || "ppp0".equals(intf.getName())) {
                        return true; // The VPN is up
                    }
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 判断是否打开wifi
     *
     * @return
     */
    public static boolean isWifiAvailable() {
        WifiManager wifiManager = (WifiManager) Function.appLicationContext.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        return wifiManager.isWifiEnabled();
    }

    /**
     * 时间错转化年月
     *
     * @return
     */
    public static String getTimeTransformDate(long time, String text) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat(text);
        Date date = new Date(String.valueOf(time).length() > 10 ? time : time * 1000);
        return simpleDateFormat.format(date);
    }

    /**
     * 获取昨天提起
     *
     * @return 昨天
     */
    private static String getYesterday() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        Date date = calendar.getTime();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        return fmt.format(date);
    }

    /**
     * 获取剪贴板内容
     *
     * @return 剪贴板内容
     */
    public static String getClipboardContent(Activity activity) {
        ClipboardManager clipboardManager = (ClipboardManager) activity.getSystemService(CLIPBOARD_SERVICE);
        if (clipboardManager.hasPrimaryClip() && clipboardManager.getPrimaryClip() != null && clipboardManager.getPrimaryClip().getItemCount() > 0) {
            CharSequence addedText = clipboardManager.getPrimaryClip().getItemAt(0).getText();
            if (!TextUtils.isEmpty(addedText)) {
                return addedText.toString();
            }
        }
        return null;
    }

    /**
     * 获取是否开启了推送权限
     *
     * @param context
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static boolean isNotificationEnabled(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ///< 8.0手机以上
            if (((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE)).getImportance() == NotificationManager.IMPORTANCE_NONE) {
                return false;
            }
        }
        AppOpsManager mAppOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        ApplicationInfo appInfo = context.getApplicationInfo();
        String pkg = context.getApplicationContext().getPackageName();
        int uid = appInfo.uid;

        Class appOpsClass = null;
        try {
            appOpsClass = Class.forName(AppOpsManager.class.getName());
            Method checkOpNoThrowMethod = appOpsClass.getMethod("checkOpNoThrow", Integer.TYPE, Integer.TYPE, String.class);
            Field opPostNotificationValue = appOpsClass.getDeclaredField("OP_POST_NOTIFICATION");
            int value = (Integer) opPostNotificationValue.get(Integer.class);
            return ((Integer) checkOpNoThrowMethod.invoke(mAppOps, value, uid, pkg) == AppOpsManager.MODE_ALLOWED);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static int getErrorCode(String message) {
        try {
            String[] error = message.split("\\|");
            if (error.length > 1 && !TextUtils.isEmpty(error[0])) {
                return Integer.parseInt(error[1]);
            }
        } catch (Exception e) {
            return 0;
        }
        return 0;
    }
}