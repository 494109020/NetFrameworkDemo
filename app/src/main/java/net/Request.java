package net;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Magina on 16/9/8.
 */
public abstract class Request<T> implements Comparable<Request<T>> {
    public static final String HEADER_CONTENT_TYPE = "Content-Type";
    //请求方式
    private HttpMethod mHttpMethod;
    //
    private String mUrl;
    //
    private RequestListener<T> mRequestListener;
    //
    private HashMap<String, String> mHeaders = new HashMap<>();
    //
    private HashMap<String, String> mParams = new HashMap<>();
    //
    Priority mPriority = Priority.NORMAL;
    //
    private int serialNumber;
    //
    private boolean isCache;

    public Request(HttpMethod method, String url, RequestListener<T> requestListener) {
        mHttpMethod = method;
        mUrl = url;
        mRequestListener = requestListener;
    }

    public abstract T parseResponse(Response response);


    public String getUrl() {
        return mUrl;
    }

    public HashMap<String, String> getHeaders() {
        return mHeaders;
    }

    public HashMap<String, String> getParams() {
        return mParams;
    }

    public HttpMethod getHttpMethod() {
        return mHttpMethod;
    }

    public byte[] getBody() {
        if (mParams != null && mParams.size() > 0)
            return encodedParams();
        return null;
    }

    public Priority getPriority() {
        return mPriority;
    }


    private int getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(int serialNumber) {
        this.serialNumber = serialNumber;
    }

    private byte[] encodedParams() {
        String paramsEncoding = getParamsEncoding();
        StringBuilder sb = new StringBuilder();
        try {
            for (Map.Entry<String, String> entry : mParams.entrySet()) {
                sb.append(URLEncoder.encode(entry.getKey(), paramsEncoding))
                        .append("=").append(URLEncoder.encode(entry.getValue(), paramsEncoding))
                        .append("&");
            }
            return sb.toString().getBytes(paramsEncoding);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Encoding not support:" + paramsEncoding, e);
        }
    }

    public String getBodyContentType() {
        return "application/x-www-form-urlencoded; charset=" + getParamsEncoding();
    }

    private String getParamsEncoding() {
        return Config.ENCODING;
    }

    @Override
    public int compareTo(Request<T> o) {
        return getPriority().equals(o.getPriority()) ? this.getSerialNumber() - o.getSerialNumber() : getPriority().getValue() - o.getPriority().getValue();
    }

    public boolean isUseCache() {
        return isCache;
    }

    public final void deliveryResponse(Response response) {
        T result = parseResponse(response);
        if (mRequestListener != null) {
            int code = response == null ? -1 : response.getStatusCode();
            String msg = response == null ? "Unknow error!" : response.getMessage();
            mRequestListener.onComplete(code, result, msg);
        }
    }

    public interface RequestListener<T> {
        void onComplete(int statusCode, T response, String errMsg);
    }
}
