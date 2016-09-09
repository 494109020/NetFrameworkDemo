package net;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by Magina on 16/9/8.
 */
public class MultipartRequest extends Request<String> {

    private MultipartEntity mMultipartEntity = new MultipartEntity();

    public MultipartRequest(HttpMethod method, String url, RequestListener<String> requestListener) {
        super(method, url, requestListener);
    }

    @Override
    public String parseResponse(Response response) {
        return null;
    }


    public MultipartEntity getMultipartEntity() {
        return mMultipartEntity;
    }

    @Override
    public byte[] getBody() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            mMultipartEntity.writeTo(baos);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return baos.toByteArray();
    }
}
