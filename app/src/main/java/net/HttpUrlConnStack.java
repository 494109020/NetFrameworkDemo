package net;


import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Set;

/**
 * Created by Magina on 16/9/8.
 */
public class HttpUrlConnStack implements HttpStack {

    @Override
    public Response performRequest(Request<?> request) {
        HttpURLConnection urlConnection = null;
        try {
            urlConnection = getHttpURLConnection(request.getUrl());
            setRequestHeader(urlConnection, request);
            setRequestParams(urlConnection, request);
            return fetchResponse(urlConnection);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null)
                urlConnection.disconnect();
        }
        return null;
    }

    private void setRequestHeader(HttpURLConnection urlConnection, Request<?> request) {
        Set<Map.Entry<String, String>> entrySet = request.getHeaders().entrySet();
        for (Map.Entry<String, String> entry : entrySet) {
            urlConnection.setRequestProperty(entry.getKey(), entry.getValue());
        }
    }

    private void setRequestParams(HttpURLConnection urlConnection, Request<?> request) throws IOException {
        urlConnection.setRequestMethod(request.getHttpMethod().toString());
        byte[] body = request.getBody();
        if (body != null) {
            urlConnection.setDoOutput(true);
            urlConnection.addRequestProperty(Request.HEADER_CONTENT_TYPE, request.getBodyContentType());
            DataOutputStream dos = new DataOutputStream(urlConnection.getOutputStream());
            dos.write(body);
            dos.close();
        }
    }

    private HttpURLConnection getHttpURLConnection(String url) throws IOException {
        HttpURLConnection urlConnection;
        urlConnection = (HttpURLConnection) new URL(url).openConnection();
        urlConnection.setConnectTimeout(Config.TIME_OUT);
        urlConnection.setDoInput(true);
        urlConnection.setDefaultUseCaches(false);
        return urlConnection;
    }

    private Response fetchResponse(HttpURLConnection urlConnection) throws IOException {
        int responseCode = urlConnection.getResponseCode();
        if (responseCode == -1) {
            throw new IOException("error");
        }
        Response response = new Response(responseCode);
        response.setEntity(getHttpEntity(urlConnection));
        return response;
    }

    private HttpEntity getHttpEntity(HttpURLConnection urlConnection) {
        HttpEntity entity = new HttpEntity();
        InputStream is;
        try {
            is = urlConnection.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
            is = urlConnection.getErrorStream();
        }
        entity.setContent(is);
        entity.setContentLength(urlConnection.getContentLength());
        entity.setContentEncoding(urlConnection.getContentEncoding());
        entity.setContentType(urlConnection.getContentType());
        return entity;
    }
}
