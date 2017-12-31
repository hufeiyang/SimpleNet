package simplenet.hfy.com.simplenet.logic.http.request;


import simplenet.hfy.com.simplenet.logic.http.callback.RequestListener;
import simplenet.hfy.com.simplenet.logic.http.response.Response;

/**
 * Created by hWX393093 on 2017/12/31.
 */

public class StringRequest extends Request<String> {
    /**
     * Http请求 构造方法
     *
     * @param method   httpMethod
     * @param url      url
     * @param listener 回调给UI
     */
    public StringRequest(HttpMethod method, String url, RequestListener<String> listener) {
        super(method, url, listener);
    }

    @Override
    public String parseResponse(Response response) {
        byte[] byteResponse = response.getByteResponse();
        return new String(byteResponse);
    }
}
