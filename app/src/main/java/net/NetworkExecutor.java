package net;

import java.util.Hashtable;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadFactory;

/**
 * Created by Magina on 16/9/8.
 */
public class NetworkExecutor extends Thread {

    //
    private BlockingQueue<Request<?>> mRequestQueue;
    //
    private HttpStack mHttpStack;
    //
    private static Hashtable<String, Response> mReqCache = new Hashtable<>();
    private boolean isStop;
    //
    static ResponseDelivery mResponseDelivery = new ResponseDelivery();


    public NetworkExecutor(BlockingQueue<Request<?>> requestQueue, HttpStack httpStack) {
        mHttpStack = httpStack;
        mRequestQueue = requestQueue;
    }

    ThreadFactory threadFactory = new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "name");
        }
    };

    @Override
    public void run() {
        try {
            while (!isStop) {
                Request<?> request = mRequestQueue.take();
                Response response;
                if (isUseCache(request)) {
                    response = mReqCache.get(request.getUrl());
                } else {
                    response = mHttpStack.performRequest(request);
                    if (request.isUseCache() && isSuccess(response)) {
                        mReqCache.put(request.getUrl(), response);
                    }
                }
                mResponseDelivery.deliveryResponse(request, response);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private boolean isUseCache(Request<?> request) {
        return request.isUseCache() && mReqCache.get(request.getUrl()) != null;
    }

    private boolean isSuccess(Response response) {
        return response != null && response.getStatusCode() == Response.HTTP_OK;
    }

    public void quit() {
        isStop = true;
        interrupt();
    }
}
