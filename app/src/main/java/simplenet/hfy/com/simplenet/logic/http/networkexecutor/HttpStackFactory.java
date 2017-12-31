package simplenet.hfy.com.simplenet.logic.http.networkexecutor;

/**
 * Created by hWX393093 on 2017/12/31.
 */

public class HttpStackFactory {
    public static HttpStack createHttpStack() {
        return new HttpUrlConnStack();
    }
}
