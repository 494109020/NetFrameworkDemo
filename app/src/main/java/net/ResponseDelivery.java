package net;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;

/**
 * Created by Magina on 16/9/8.
 */
public class ResponseDelivery implements Executor {

    Handler mResponseHandler = new Handler(Looper.getMainLooper());

    public void deliveryResponse(final Request<?> request, final Response response) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                request.deliveryResponse(response);
            }
        };
        execute(runnable);
    }

    @Override
    public void execute(Runnable command) {
        mResponseHandler.post(command);
    }
}
