package com.ss.giphy.network;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Shubham
 */
public class ServiceGenerator {

	private static HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);

	private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
			.addInterceptor(loggingInterceptor)
			.connectTimeout(30, TimeUnit.SECONDS)
			.readTimeout(30, TimeUnit.SECONDS);

	private static Gson gson = new GsonBuilder()
			.setLenient()
			.create();

	private static Retrofit.Builder builder = new Retrofit.Builder()
			.baseUrl("https://api.giphy.com/v1/gifs/")
			.addConverterFactory(GsonConverterFactory.create(gson));


	/**
	 * Create service for api call.
	 *
	 * @param serviceClass
	 * @param <S>
	 * @return
	 */
	public static <S> S createService(Class<S> serviceClass) {
		Retrofit retrofit = builder.client(httpClient.build()).build();
		return retrofit.create(serviceClass);
	}

}

