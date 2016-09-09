package net;

import java.io.InputStream;

/**
 * Created by Magina on 16/9/8.
 */
public class HttpEntity {

    private InputStream mInputStream;
    private int mContentLength;
    private String mContentEncoding;
    private String mContentType;

    public void setContent(InputStream is) {
        mInputStream = is;
    }

    public void setContentLength(int contentLength) {
        mContentLength = contentLength;
    }

    public void setContentEncoding(String contentEncoding) {
        mContentEncoding = contentEncoding;
    }

    public void setContentType(String contentType) {
        mContentType = contentType;
    }

    public InputStream getInputStream() {
        return mInputStream;
    }

    public int getContentLength() {
        return mContentLength;
    }

    public String getContentEncoding() {
        return mContentEncoding;
    }

    public String getContentType() {
        return mContentType;
    }
}
