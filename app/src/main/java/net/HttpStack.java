package net;

/**
 * Created by Magina on 16/9/8.
 */
public interface HttpStack {

    Response performRequest(Request<?> request);
}
