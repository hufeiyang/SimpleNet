package simplenet.hfy.com.simplenet.logic.http.networkexecutor;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.PriorityBlockingQueue;

import simplenet.hfy.com.simplenet.logic.http.request.Request;
import simplenet.hfy.com.simplenet.logic.http.response.Response;
import simplenet.hfy.com.simplenet.logic.http.response.ResponseDelivery;

/**
 * Created by hWX393093 on 2017/12/30.
 * <p>
 * 执行器，获取请求、执行请求、把结果发送到UI线程
 */

public class NetWorkExecutor extends Thread {

    //请求队列
    private final PriorityBlockingQueue<Request<?>> queue;

    //执行网络请求的接口，由实现类 决定如何 执行
    private final HttpStack httpStack;

    //是否停止
    private boolean isStop;

    //响应的缓存
    private Map<String, Response> cache = new HashMap<>();

    private String TAG = "hfy NetWorkExecutor";

    //响应发送器
    private ResponseDelivery responseDelivery = new ResponseDelivery();

    public NetWorkExecutor(PriorityBlockingQueue<Request<?>> queue, HttpStack httpStack) {
        this.queue = queue;
        this.httpStack = httpStack;
    }


    @Override
    public void run() {
        try {
            while (!isStop) {
                //1、获取请求
                Request<?> request = null;
                if (queue != null) {
                    request = queue.take();
                }
                if (request == null || request.isCanceled()) {
                    Log.d(TAG, "run: request.isCanceled()");
                    continue;
                }

                //2、执行请求
                Response response;
                if (isUseCache(request)){
                    response = cache.get(request.getUrl());
                }else {
                    response= httpStack.performRequest(request);
                    cache.put(request.getUrl(), response);
                }

                //3、分发请求结果
                responseDelivery.deliveryResponse(request, response);

            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    //是否用缓存
    private boolean isUseCache(Request<?> request) {
        return request.isNeedCache() && cache.get(request.getUrl()) != null;
    }

    //停止获取请求
    public void quit() {
        isStop = true;
        interrupt();
    }
}
