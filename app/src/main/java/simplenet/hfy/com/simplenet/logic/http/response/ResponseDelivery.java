package simplenet.hfy.com.simplenet.logic.http.response;


import android.os.Handler;
import android.os.Looper;

import simplenet.hfy.com.simplenet.logic.http.request.Request;


/**
 * Created by hWX393093 on 2017/12/31.
 * 通过内部的 UIHandler 把结果 发送到UI线程
 *
 */

public class ResponseDelivery {

    private Handler uiHandler = new Handler(Looper.getMainLooper());

    public void deliveryResponse(final Request<?> request, final Response response) {

        uiHandler.post(new Runnable() {
            @Override
            public void run() {
                request.deliveryResponde(response);
            }
        });

    }
}
