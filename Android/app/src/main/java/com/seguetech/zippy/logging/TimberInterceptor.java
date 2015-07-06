package com.seguetech.zippy.logging;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

import timber.log.Timber;

public class TimberInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        long start = System.nanoTime();

        // log the request details.
        Timber.d(
                String.format(
                        "Sending %s request %s on %s%n%s",
                        request.method(),
                        request.urlString(),
                        chain.connection(),
                        request.headers()
                )
        );

        Response response = chain.proceed(request);

        long end = System.nanoTime();
        // log the response details.
        Timber.d(
                String.format(
                        "Received %n %s response for %s in %.1f milliseconds %n%s",
                        response.code(),
                        response.request().urlString(),
                        (end-start)/1e6d,
                        response.headers())
        );

        return response;
    }
}