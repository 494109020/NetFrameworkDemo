package net;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Magina on 16/9/8.
 */
public class Response {

    public static final int HTTP_OK = 200;

    private int mStatusCode;
    private String mMessage;
    private HttpEntity mHttpEntity;
    private byte[] mData;

    public Response(int statusCode) {
        mStatusCode = statusCode;
    }

    public void setEntity(HttpEntity entity) {
        mHttpEntity = entity;
        mData = entity2bytes(entity);
    }

    private byte[] entity2bytes(HttpEntity entity) {
        try {
            InputStream is = entity.getInputStream();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] data = new byte[1024 * 1024];
            int length;
            while ((length = is.read(data)) != -1) {
                baos.write(data, 0, length);
            }
            return baos.toByteArray();
        } catch (IOException e) {
            return new byte[0];
        }
    }

    public HttpEntity getHttpEntity() {
        return mHttpEntity;
    }

    public int getStatusCode() {
        return mStatusCode;
    }

    public void setmMessage(String message) {
        this.mMessage = message;
    }

    public String getMessage() {
        return mMessage;
    }

    public byte[] getData() {
        return mData;
    }
}
