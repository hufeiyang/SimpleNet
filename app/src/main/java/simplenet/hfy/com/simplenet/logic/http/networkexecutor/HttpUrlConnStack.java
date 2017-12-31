package simplenet.hfy.com.simplenet.logic.http.networkexecutor;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.Set;

import simplenet.hfy.com.simplenet.logic.http.request.Request;
import simplenet.hfy.com.simplenet.logic.http.response.Response;


/**
 * Created by hWX393093 on 2017/12/31.
 * 网络请求的正真执行者：HTTPURLConnection
 */

public class HttpUrlConnStack implements HttpStack {

    public HttpUrlConnStack() {
    }

    @Override
    public Response performRequest(Request<?> request) {
        //1、获取HTTPURLConnection
        HttpURLConnection connection = getHttpUrlConnection(request);

        //2、设置请求头部
        setRequestHeader(connection, request);
        Response response = null;
        try {
            //3、设置请求参数
            setRequestParam(connection, request);
            //4、获取结果
            response = getResponse(connection);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    private HttpURLConnection getHttpUrlConnection(Request<?> request) {
        URL url = null;
        HttpURLConnection connection = null;
        try {
            url = new URL(request.getUrl());
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(50000);
            connection.setReadTimeout(50000);
            connection.setDoInput(true);
            connection.setUseCaches(false);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return connection;
    }

    private void setRequestHeader(HttpURLConnection connection, Request<?> request) {
        Set<Map.Entry<String, String>> entries = request.getHeaders().entrySet();
        for (Map.Entry<String, String> entry : entries) {
            connection.setRequestProperty(entry.getKey(), String.valueOf(entry));
        }

    }

    private void setRequestParam(HttpURLConnection connection, Request<?> request) throws IOException {
        Request.HttpMethod httpMethod = request.getHttpMethod();
        connection.setRequestMethod(httpMethod.toString());
        connection.setRequestProperty("Content-Type", request.getBodyContentType());

        byte[] body = request.getBody();
        if (body != null) {
            connection.setDoOutput(true);
            OutputStream outputStream = connection.getOutputStream();//隐含连接 即connect
            DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
            dataOutputStream.write(body);
            dataOutputStream.flush();
            dataOutputStream.close();
        }
    }

    private Response getResponse(HttpURLConnection connection) throws IOException {

        InputStream inputStream = connection.getInputStream();
        Response response = new Response();
        response.setStatusCode(connection.getResponseCode());
        response.setMsg(connection.getResponseMessage());

        byte[] byteResponse = getByteResponse(inputStream);
        response.setByteResponse(byteResponse);
        return response;
    }
    public byte[] getByteResponse(InputStream inputStream) throws IOException {
        byte[] bytes = new byte[0];
        if (inputStream != null) {

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] byteBuffer = new byte[1024];
            int len = 0;
            while ((len = inputStream.read(byteBuffer)) != -1) {
                outputStream.write(byteBuffer, 0, len);
            }
            bytes = outputStream.toByteArray();
        }
        return bytes;
    }
}
