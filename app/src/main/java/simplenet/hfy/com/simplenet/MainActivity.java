package simplenet.hfy.com.simplenet;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import simplenet.hfy.com.simplenet.logic.http.callback.RequestListener;
import simplenet.hfy.com.simplenet.logic.http.networkexecutor.HttpUrlConnStack;
import simplenet.hfy.com.simplenet.logic.http.request.Request;
import simplenet.hfy.com.simplenet.logic.http.request.RequestQueue;
import simplenet.hfy.com.simplenet.logic.http.request.StringRequest;

public class MainActivity extends AppCompatActivity {

    private String TAG = "hfy MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RequestQueue requestQueue = new RequestQueue(2, new HttpUrlConnStack());
        requestQueue.start();

        requestQueue.addRequest(new StringRequest(Request.HttpMethod.GET, "https://www.jianshu.com/p/f9c20074a207", new RequestListener<String>() {
            @Override
            public void onComplete(int stCode, String result, String msg) {
                Log.i(TAG, "onComplete: \n result : " + result + "\n stCode:" + stCode + "\n msg:"  + msg);
            }
        }));
    }
}
