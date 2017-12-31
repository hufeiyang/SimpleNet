package simplenet.hfy.com.simplenet.logic.http.request;

import android.util.Log;

import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

import simplenet.hfy.com.simplenet.logic.http.networkexecutor.HttpStack;
import simplenet.hfy.com.simplenet.logic.http.networkexecutor.HttpStackFactory;
import simplenet.hfy.com.simplenet.logic.http.networkexecutor.NetWorkExecutor;

/**
 * Created by hWX393093 on 2017/12/30.
 * 请求队列，用于管理请求，启动 请求执行器
 */

public class RequestQueue {

    private static final String TAG = "hfy";

    // 默认的核心数 ：CPU核心数+1
    private static final int DEFAULT_CORE_NUM = Runtime.getRuntime().availableProcessors() + 1;

    //核心数
    private int coreNum = DEFAULT_CORE_NUM;

    //http请求的正真执行者
    private final HttpStack httpStack;

    //线程安全的请求队列
    private PriorityBlockingQueue<Request<?>> queue = new PriorityBlockingQueue<>();

    //netWorkExecutors
    private NetWorkExecutor[] netWorkExecutors = null;

    //序列化生成器
    private AtomicInteger serialNumGenerator = new AtomicInteger(0);

    public RequestQueue(int coreNums, HttpStack httpStack) {
        this.coreNum = coreNums;
        this.httpStack = httpStack != null ? httpStack : HttpStackFactory.createHttpStack();
    }

    //向队列中添加请求
    public void addRequest(Request<?> request){
        if (!queue.contains(request)) {
            request.setSerialNum(this.generateSerialNum());
            queue.add(request);
        }else {
            Log.d(TAG, "addRequest: requestQueue has this request.");
        }
    }

    // 启动队列：先停止，再启动 n + 1 个 NetWorkExecutor
    public void start(){
        if (netWorkExecutors != null && netWorkExecutors.length > 0) {
            for (int i = 0; i < netWorkExecutors.length; i++) {
                netWorkExecutors[i].quit();
            }
        }

        netWorkExecutors = new NetWorkExecutor[coreNum];
        for (int i = 0; i < coreNum; i++) {
            NetWorkExecutor netWorkExecutor = new NetWorkExecutor(queue, httpStack);
            netWorkExecutors[i] = netWorkExecutor;
            netWorkExecutor.start();
        }
    }

    //为请求生成序列号
    private int generateSerialNum() {
        return serialNumGenerator.decrementAndGet();
    }
}
