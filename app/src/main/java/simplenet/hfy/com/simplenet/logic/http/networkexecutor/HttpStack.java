package simplenet.hfy.com.simplenet.logic.http.networkexecutor;


import simplenet.hfy.com.simplenet.logic.http.request.Request;
import simplenet.hfy.com.simplenet.logic.http.response.Response;

/**
 * Created by hWX393093 on 2017/12/30.
 */

public interface HttpStack {
    Response performRequest(Request<?> request);
}
