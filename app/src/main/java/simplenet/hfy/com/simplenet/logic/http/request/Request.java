package simplenet.hfy.com.simplenet.logic.http.request;

import android.support.annotation.NonNull;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import simplenet.hfy.com.simplenet.logic.http.callback.RequestListener;
import simplenet.hfy.com.simplenet.logic.http.response.Response;


/**
 * Created by hWX393093 on 2017/12/29.
 * 自定义网络框架之Request:
 * 包含请求的 header、body，以及 解析原始结果 和 回调
 *
 * T ：用户可自定义 返回结果的类型，所有此处用泛型
 * 请求 有加入请求队列的顺序，且可以设置优先级，所以实现Comparable
 */
public abstract class Request<T> implements Comparable<Request<T>> {

    //默认编码方式
    private static final String DEFAULT_PARAMS_ENCODING = "UTF-8";

    //请求方式(默认为 GET)
    private HttpMethod httpMethod = HttpMethod.GET;

    //url
    private String url = "";

    //结果回调
    private final RequestListener<T> listener;

    //优先级（默认为Normal）
    private Priority mPriority = Priority.NORMAL;

    //请求序号
    private int serialNum;

    //请求头
    private Map<String, String> headers = new HashMap<>();

    //请求参数
    private Map<String, String> params = new HashMap<>();

    //请求是否取消
    private boolean canceled;

    //请求结果是否要缓存
    private boolean needCache;

    //Http 请求方式枚举
    public static enum HttpMethod {
        GET("GET"),
        POST("POST"),
        PUT("PUT"),
        DELETE("DELETE");


        private final String method;

        HttpMethod(String method) {
            this.method = method;
        }

        @Override
        public String toString() {
            return method;
        }

    }

    // 请求优先级 枚举
    public static enum Priority {
        LOW,
        NORMAL,
        HIGH,
        IMMEDIATE;

    }

    /**
     * Http请求 构造方法
     *
     * @param method   httpMethod
     * @param url      url
     * @param listener 回调给UI
     */
    public Request(HttpMethod method, String url, RequestListener<T> listener) {
        httpMethod = method;
        this.url = url;
        this.listener = listener;
    }

    /**
     * 按优先级排序，
     * 优先级相同，则看加入队列的顺序
     *
     * @param request
     * @return
     */
    @Override
    public int compareTo(@NonNull Request<T> request) {
        return mPriority.equals(request.mPriority) ?
                this.getSerialNum() - request.getSerialNum() : this.mPriority.ordinal() - request.getPriority().ordinal();
    }

    /**
     * 解析原始结果，子类要重写
     *
     * @param response
     * @return
     */
    public abstract T parseResponse(Response response);

    /**
     * 处理response ，此方法要在UI线程执行
     *
     * @param response
     */
    public void deliveryResponde(Response response) {
        //解析请求结果
        T result = parseResponse(response);
        if (listener != null) {
            int stCode = response == null ? -1 : response.getStatusCode();
            String msg = response == null ? "unknow error" : response.getMsg();
            listener.onComplete(stCode, result, msg);
        }
    }


    protected String getParamsEncoding() {
        return DEFAULT_PARAMS_ENCODING;
    }

    public String getBodyContentType() {
        return "application/x-www-form-urlencoded;charset=" + getParamsEncoding();
    }

    //获取 POST或PUT 请求时的Body参数 字节数组
    public byte[] getBody() {
        if (params != null && params.size() > 0) {
            return encodeParams();
        }
        return null;
    }

    //将参数转换为URl编码的字符串，格式为 key1=value1&key2=value2
    private byte[] encodeParams() {
        StringBuilder encodedParams = new StringBuilder();
        try {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                encodedParams.append(URLEncoder.encode(entry.getKey(), getParamsEncoding()));
                encodedParams.append("=");
                encodedParams.append(URLEncoder.encode(entry.getValue(), getParamsEncoding()));
                encodedParams.append("&");
            }
            return encodedParams.toString().getBytes(getParamsEncoding());

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    private byte[] encodeHeaders() {
        return new byte[0];
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }


    public String getUrl() {
        return url;
    }

    public RequestListener<T> getListener() {
        return listener;
    }

    public Priority getPriority() {
        return mPriority;
    }

    public void setmPriority(Priority mPriority) {
        this.mPriority = mPriority;
    }

    public int getSerialNum() {
        return serialNum;
    }

    public void setSerialNum(int serialNum) {
        this.serialNum = serialNum;
    }

    public void setHttpMethod(HttpMethod httpMethod) {
        this.httpMethod = httpMethod;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public boolean isCanceled() {
        return canceled;
    }

    public void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }

    public boolean isNeedCache() {
        return needCache;
    }

    public void setNeedCache(boolean needCache) {
        this.needCache = needCache;
    }
}
