package com.ss.giphy.api;

import com.ss.giphy.model.GiphyModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Shubham
 */
public interface GiphyApiClient {

	/**
	 * Trending Api
	 *
	 * @param api
	 * @param limit
	 * @param rating
	 * @param offset
	 * @param fmt
	 * @return
	 */
	@GET("trending")
	Call<GiphyModel> getGifs(@Query("api_key") String api, @Query("limit") String limit, @Query("rating") String rating, @Query("offset") String offset, @Query("fmt") String fmt);

}
