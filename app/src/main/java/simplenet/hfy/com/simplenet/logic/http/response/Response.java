package simplenet.hfy.com.simplenet.logic.http.response;


/**
 * Created by hWX393093 on 2017/12/29.
 * Http 响应的原始结果
 */

public class Response{

    //状态码
    private int statusCode;

    //状态描述
    private String msg;

    //转换为字节数组的响应
    private byte[] byteResponse;

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setByteResponse(byte[] byteResponse) {
        this.byteResponse = byteResponse;
    }

    public byte[] getByteResponse() {
        return byteResponse;
    }
}
