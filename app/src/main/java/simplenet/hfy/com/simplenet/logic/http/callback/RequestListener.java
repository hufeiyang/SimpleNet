package simplenet.hfy.com.simplenet.logic.http.callback;

/**
 * Created by hWX393093 on 2017/12/29.
 */

public interface RequestListener<T> {
    public void onComplete(int stCode, T result, String msg);
}
