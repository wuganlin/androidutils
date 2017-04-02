package com.wgl.android.library.http;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;


/**
 * Created by wuganlin on 2017/3/31.
 */

public class HttpRequestHelper {
    protected String url;
    protected Retrofit mRetrofit;

    public HttpRequestHelper(String url) {
        this.url = url;
        mRetrofit = onCreateRetrofit();
    }

    /**
     * @return
     */
    protected List<Interceptor> onCreateInterceptors() {
        List<Interceptor> interceptors = new ArrayList<>();
        /**
         * Logger
         */
        HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor();
        logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        interceptors.add(logInterceptor);
        /**
         * 增加头部信息
         */
        Interceptor headerInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request build = chain.request().newBuilder()
                        .addHeader("Content-Type", "application/json")
                        .build();
                return chain.proceed(build);
            }
        };
        interceptors.add(headerInterceptor);
        return interceptors;
    }

    protected Cache onCreateCache() {
        return null;
    }

    /**
     * @return
     */
    protected OkHttpClient onCreateOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .readTimeout(8000, TimeUnit.MILLISECONDS)
                .connectTimeout(8000, TimeUnit.MILLISECONDS);
        /**
         * Interceptor
         */
        List<Interceptor> interceptors = onCreateInterceptors();
        if (interceptors != null && !interceptors.isEmpty()) {
            for (Interceptor interceptor : interceptors) {
                builder.addInterceptor(interceptor);
            }
        }
        /**
         * Cache
         */
        Cache cache = onCreateCache();
        if (cache != null) {
            builder.cache(cache);
        }

        return builder.build();
    }

    /**
     * @return
     */
    protected Retrofit onCreateRetrofit() {
        Retrofit.Builder builder = new Retrofit.Builder()
                .client(onCreateOkHttpClient())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(url);
        return builder.build();
    }

    public <T> T create(Class<T> clazz) {
        return mRetrofit.create(clazz);
    }
}
