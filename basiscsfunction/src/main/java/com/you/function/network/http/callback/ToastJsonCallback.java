package com.you.function.network.http.callback;

import android.accounts.NetworkErrorException;

import com.lzy.okgo.model.Response;
import com.you.function.Function;
import com.you.function.R;
import com.you.function.network.http.data.ResponseData;
import com.you.function.network.http.utlis.CommonUtil;
import com.you.function.utils.ToastUtil;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeoutException;

/**
 * Created by you on 2019-09-24
 */

public abstract class ToastJsonCallback<T extends ResponseData> extends JsonCallback<T> {

    @Override
    public void onError(Response<T> response) {
        ToastUtil.showToast(Function.appLicationContext, getErrorText(response));
        super.onError(response);
    }

    private <T extends ResponseData> String getErrorText(Response<T> response) {
        String errorString;
        if (response.getException() instanceof IllegalStateException) {
            errorString = response.body().getMessage();
        } else if (response.getException() instanceof NetworkErrorException || response.getException() instanceof UnknownHostException) {
            errorString = Function.appLicationContext.getResources().getString(R.string.error_network_anomaly);
        } else if (response.getException() instanceof TimeoutException) {
            errorString = Function.appLicationContext.getResources().getString(R.string.error_network_outTime);
        } else if (response.getException() instanceof SocketTimeoutException) {
            errorString = Function.appLicationContext.getResources().getString(R.string.error_network_outTime);
        } else {
            errorString = Function.appLicationContext.getResources().getString(R.string.error_network_server_eorro);
        }
        return errorString;
    }
}
