package net;

import android.util.Log;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Magina on 16/9/8.
 */
public class RequestQueue {

    private HttpStack mHttpStack;
    //
    private AtomicInteger mSerialNumGenerator = new AtomicInteger(0);
    //
    private BlockingQueue<Request<?>> mRequestQueue = new PriorityBlockingQueue<>();
    //
    private int mDispatchNums = Config.CORE_POOL_SIZE;
    //
    private NetworkExecutor[] mExecutors;

    protected RequestQueue(int coreNums, HttpStack httpStack) {
        mDispatchNums = coreNums;
        mHttpStack = httpStack == null ? new HttpUrlConnStack() : httpStack;
    }

    public void start() {
        stop();
        mExecutors = new NetworkExecutor[mDispatchNums];
        for (int i = 0; i < mDispatchNums; i++) {
            mExecutors[i] = new NetworkExecutor(mRequestQueue, mHttpStack);
            mExecutors[i].start();
        }
    }

    public void stop() {
        if (mExecutors != null && mExecutors.length > 0)
            for (int i = 0; i < mDispatchNums; i++) {
                mExecutors[i].quit();
            }
    }


    public void addRequest(Request<?> request) {
        if (!mRequestQueue.contains(request)) {
            request.setSerialNumber(generateSerialNumber());
            mRequestQueue.add(request);
        } else {
            Log.d(Config.LOG, "request already exists!");
        }
    }

    private int generateSerialNumber() {
        return mSerialNumGenerator.incrementAndGet();
    }
}
