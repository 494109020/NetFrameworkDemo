package net;

/**
 * Created by Magina on 16/9/8.
 */
public enum HttpMethod {
    GET("GET"), POST("POST"), PUT("PUT"), DELETE("DELETE");

    private String mMethod;

    private HttpMethod(String value) {
        mMethod = value;
    }

    @Override
    public String toString() {
        return mMethod;
    }
}
