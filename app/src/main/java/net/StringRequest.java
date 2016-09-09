package net;

/**
 * Created by Magina on 16/9/8.
 */
public class StringRequest extends Request<String> {

    public StringRequest(HttpMethod method, String url, RequestListener<String> requestListener) {
        super(method, url, requestListener);
    }

    @Override
    public String parseResponse(Response response) {
        if (response != null) {
            return new String(response.getData());
        }
        return null;
    }
}
