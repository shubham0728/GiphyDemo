package com.ss.giphy.fragments;


import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.giphy.sdk.core.models.Media;
import com.giphy.sdk.core.models.enums.MediaType;
import com.giphy.sdk.core.network.api.CompletionHandler;
import com.giphy.sdk.core.network.api.GPHApi;
import com.giphy.sdk.core.network.api.GPHApiClient;
import com.giphy.sdk.core.network.response.ListMediaResponse;
import com.google.gson.JsonObject;
import com.like.LikeButton;
import com.like.OnAnimationEndListener;
import com.like.OnLikeListener;
import com.rey.material.widget.ProgressView;
import com.ss.giphy.R;
import com.ss.giphy.Util.Consts;
import com.ss.giphy.Util.EndlessScrollListener;
import com.ss.giphy.Util.GiphySharedPreferences;
import com.ss.giphy.network.ServiceGenerator;
import com.ss.giphy.adapter.TopRatedGridAdapter;
import com.ss.giphy.api.GiphyApiClient;
import com.ss.giphy.model.GiphyEntity;
import com.ss.giphy.model.GiphyModel;
import com.ss.giphy.repository.GiphyViewModel;
import com.ss.giphy.views.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by Shubham
 * Fragment to show list of Gif's from the data returned by the api..
 */
public class FragmentTopRated extends Fragment implements MainActivity.DataListener {

	private ProgressView progress;
	private ProgressView circular_progress_bottom;
	private RequestQueue mRequestQueue;
	private GiphyViewModel giphyModel;
	private TopRatedGridAdapter adapter;
	private GridView grid;
	private View view;
	private TextView placeholerText;
	private ArrayList<GiphyEntity> movieArr = new ArrayList<>();
	private ArrayList<GiphyEntity> searchList = new ArrayList<>();
	private String API_KEY = "TqN8irYgIanH1pmCt6tFPi3i0TA05KMd";
	private int offset = 0;
	private GPHApi client;
	private int itemPerPage = 26;
	private GiphySharedPreferences preferences;
	private boolean isLoading = false;
	private int count = 0;
	private MainActivity mActivity;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_movies, container, false);
		client = new GPHApiClient(API_KEY);
		preferences = new GiphySharedPreferences(getActivity().getApplicationContext());
		mActivity = (MainActivity) getActivity();
		mActivity.setDataListener(this);
		init();
		return view;
	}

	/**
	 * Initialising widgets.
	 */
	private void init() {
		mRequestQueue = Volley.newRequestQueue(getActivity());
		giphyModel = ViewModelProviders.of(this).get(GiphyViewModel.class);
		grid = view.findViewById(R.id.gridView);
		progress = (ProgressView) view.findViewById(R.id.circular_progress);
		circular_progress_bottom = (ProgressView) view.findViewById(R.id.circular_progress_bottom);
		placeholerText = view.findViewById(R.id.placeholerText);
		progress.start();
		checkConnection();

		grid.setOnScrollListener(new EndlessScrollListener() {
			@Override
			public boolean onLoadMore(int page, int totalItemsCount) {
				if (!isLoading) {
					count = totalItemsCount;
					circular_progress_bottom.setVisibility(View.VISIBLE);
					circular_progress_bottom.start();
					offset = totalItemsCount;
					fetchTrendingGifsWithSDK();
					isLoading = true;
				}
				return true;
			}
		});
	}

	/**
	 * Fetch data using Retrofit.
	 */
	private void fetchTrendingGifsWithRetrofit() {
		//movieArr.clear();
		try {
			GiphyApiClient giphyApiClient = ServiceGenerator.createService(GiphyApiClient.class);
			Call<GiphyModel> call = giphyApiClient.getGifs(API_KEY, String.valueOf(itemPerPage), "G", String.valueOf(offset), "json");
			call.enqueue(new Callback<GiphyModel>() {
				@Override
				public void onResponse(Call<GiphyModel> call, retrofit2.Response<GiphyModel> response) {
					if (response.isSuccessful()) {
						List<JsonObject> giphyEntityList = response.body().getGiphyEntityList();
						for (int i = 0; i < giphyEntityList.size(); i++) {
							JsonObject obj = giphyEntityList.get(i);
							GiphyEntity entity = new GiphyEntity();
							String id = String.valueOf(obj.get("id"));
							id = id.replaceAll("\"", "");
							entity.setId(id);
							String type = String.valueOf(obj.get("type"));
							type = type.replaceAll("\"", "");
							entity.setType(type);
							entity.setButtonId(i);
							JsonObject image_obj = obj.getAsJsonObject("images");
							JsonObject original_obj = image_obj.getAsJsonObject("original");
							String url = String.valueOf(original_obj.get("url"));
							url = url.replaceAll("\"", "");
							entity.setUrl(url);
							giphyModel.insert(entity);
							movieArr.add(entity);
						}
						progress.stop();
						circular_progress_bottom.setVisibility(View.INVISIBLE);
						circular_progress_bottom.stop();
						grid.setVisibility(View.VISIBLE);
						adapter = new TopRatedGridAdapter(getActivity(), movieArr, giphyModel);
						grid.setAdapter(adapter);
						adapter.notifyDataSetChanged();
						placeholerText.setVisibility(View.INVISIBLE);
						isLoading = false;
						isLoading = false;
						if (offset != 0) {
							grid.smoothScrollToPosition(count);
						}
					} else {
						circular_progress_bottom.setVisibility(View.INVISIBLE);
						circular_progress_bottom.stop();
						progress.stop();
						placeholerText.setVisibility(View.VISIBLE);
						Toast.makeText(getActivity(), R.string.error_toast, Toast.LENGTH_SHORT).show();
					}
				}

				@Override
				public void onFailure(Call<GiphyModel> call, Throwable t) {
					if (t instanceof SocketTimeoutException || t instanceof IOException) {
						circular_progress_bottom.setVisibility(View.INVISIBLE);
						circular_progress_bottom.stop();
						progress.stop();
						placeholerText.setVisibility(View.VISIBLE);
						Toast.makeText(getActivity(), R.string.error_toast, Toast.LENGTH_SHORT).show();
					} else {
						circular_progress_bottom.setVisibility(View.INVISIBLE);
						circular_progress_bottom.stop();
						progress.stop();
						placeholerText.setVisibility(View.VISIBLE);
						Toast.makeText(getActivity(), R.string.error_toast, Toast.LENGTH_SHORT).show();
					}
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
			circular_progress_bottom.setVisibility(View.INVISIBLE);
			circular_progress_bottom.stop();
			progress.stop();
			placeholerText.setVisibility(View.VISIBLE);
			Toast.makeText(getActivity(), R.string.error_toast, Toast.LENGTH_SHORT).show();
		}
	}


	/**
	 * Fetch data using Volley.
	 */
	private void fetchTrendingGifsWithVolley() {
		//	movieArr.clear();
		JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, Consts.BASE_URL + "api_key=" + API_KEY + "&limit=" + String.valueOf(itemPerPage) + "&rating=G&offset=" + String.valueOf(offset) + "&fmt=json", null,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						try {
							JSONArray arr = response.getJSONArray("data");
							for (int i = 0; i < arr.length(); i++) {
								JSONObject obj = arr.getJSONObject(i);
								GiphyEntity entity = new GiphyEntity();
								entity.setId(obj.getString("id"));
								entity.setType(obj.getString("type"));
								entity.setButtonId(i);
								JSONObject image_obj = obj.getJSONObject("images");
								JSONObject original_obj = image_obj.getJSONObject("original");
								entity.setUrl(original_obj.getString("url"));
								giphyModel.insert(entity);
								movieArr.add(entity);
							}
							JSONObject pagination_obj = response.getJSONObject("pagination");
							preferences.setCount(pagination_obj.getString("total_count"));
							progress.stop();
							grid.setVisibility(View.VISIBLE);
							adapter = new TopRatedGridAdapter(getActivity(), movieArr, giphyModel);
							grid.setAdapter(adapter);
							adapter.notifyDataSetChanged();
							placeholerText.setVisibility(View.INVISIBLE);
							isLoading = false;
							isLoading = false;
							if (offset != 0) {
								grid.smoothScrollToPosition(count);
							}
						} catch (JSONException e) {
							e.printStackTrace();
							circular_progress_bottom.setVisibility(View.INVISIBLE);
							circular_progress_bottom.stop();
							progress.stop();
							placeholerText.setVisibility(View.VISIBLE);
							Toast.makeText(getActivity(), R.string.error_toast, Toast.LENGTH_SHORT).show();
						}
					}
				}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				VolleyLog.e("Error: ", error.getMessage());
				circular_progress_bottom.setVisibility(View.INVISIBLE);
				circular_progress_bottom.stop();
				progress.stop();
				placeholerText.setVisibility(View.VISIBLE);
				Toast.makeText(getActivity(), R.string.error_toast, Toast.LENGTH_SHORT).show();
			}
		});
		/* Add your Requests to the RequestQueue to execute */
		mRequestQueue.add(req);
	}

	/**
	 * Fetch data using Giphy SDK.
	 */
	private void fetchTrendingGifsWithSDK() {
		//	movieArr.clear();
		client.trending(MediaType.gif, itemPerPage, offset, null, new CompletionHandler<ListMediaResponse>() {
			@Override
			public void onComplete(ListMediaResponse result, Throwable e) {
				if (result == null) {
					circular_progress_bottom.setVisibility(View.INVISIBLE);
					circular_progress_bottom.stop();
					progress.stop();
					placeholerText.setVisibility(View.VISIBLE);
					Toast.makeText(getActivity(), R.string.error_toast, Toast.LENGTH_SHORT).show();
				} else {
					if (result.getData() != null) {
						for (int i = 0; i < result.getData().size(); i++) {
							Media gif = result.getData().get(i);
							GiphyEntity entity = new GiphyEntity();
							entity.setId(gif.getId());
							entity.setType(String.valueOf(gif.getType()));
							entity.setUrl(gif.getImages().getOriginal().getGifUrl());
							entity.setButtonId(i);
							giphyModel.insert(entity);
							movieArr.add(entity);
						}
						preferences.setCount(String.valueOf(result.pagination.getTotalCount()));
						circular_progress_bottom.setVisibility(View.INVISIBLE);
						circular_progress_bottom.stop();
						progress.stop();
						grid.setVisibility(View.VISIBLE);
						adapter = new TopRatedGridAdapter(getActivity(), movieArr, giphyModel);
						grid.setAdapter(adapter);
						adapter.notifyDataSetChanged();
						placeholerText.setVisibility(View.INVISIBLE);
						isLoading = false;
						if (offset != 0) {
							grid.smoothScrollToPosition(count);
						}

					} else {
						circular_progress_bottom.setVisibility(View.INVISIBLE);
						circular_progress_bottom.stop();
						progress.stop();
						placeholerText.setVisibility(View.VISIBLE);
						Toast.makeText(getActivity(), R.string.error_toast, Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
	}

	/**
	 * Check if network is available.
	 *
	 * @return
	 */
	protected boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * If network is available fetch data from api.
	 * If network is not available then check if data is present in local Db.
	 */
	public void checkConnection() {
		if (isOnline()) {
			giphyModel.deleteAll();
			fetchTrendingGifsWithSDK();
		} else {

			List<GiphyEntity> entities = giphyModel.getGiphywithoutLiveData();
			if (entities.size() == 0) {
				progress.stop();
				circular_progress_bottom.setVisibility(View.INVISIBLE);
				circular_progress_bottom.stop();
				Toast.makeText(getActivity(), R.string.network_toast, Toast.LENGTH_SHORT).show();
			} else {
				circular_progress_bottom.setVisibility(View.INVISIBLE);
				circular_progress_bottom.stop();
				progress.stop();
				adapter = new TopRatedGridAdapter(getActivity(), entities, giphyModel);
				grid.setAdapter(adapter);
			}
		}
	}

	/**
	 * This method is called when the search query is submitted
	 * or any text change appears in the query.
	 * @param s
	 */
	@Override
	public void onDataReceived(String s) {
		if (s.length() != 0) {
			searchList.clear();
			client.search(s, MediaType.gif, itemPerPage, offset, null, null, new CompletionHandler<ListMediaResponse>() {
				@Override
				public void onComplete(ListMediaResponse result, Throwable e) {
					if (result == null) {
						circular_progress_bottom.setVisibility(View.INVISIBLE);
						circular_progress_bottom.stop();
						progress.stop();
						placeholerText.setVisibility(View.VISIBLE);
						Toast.makeText(getActivity(), R.string.error_toast, Toast.LENGTH_SHORT).show();
					} else {
						if (result.getData() != null) {
							for (int i = 0; i < result.getData().size(); i++) {
								Media gif = result.getData().get(i);
								GiphyEntity entity = new GiphyEntity();
								entity.setId(gif.getId());
								entity.setType(String.valueOf(gif.getType()));
								entity.setUrl(gif.getImages().getOriginal().getGifUrl());
								entity.setButtonId(i);
								//giphyModel.insert(entity);
								searchList.add(entity);
							}
							preferences.setCount(String.valueOf(result.pagination.getTotalCount()));
							circular_progress_bottom.setVisibility(View.INVISIBLE);
							circular_progress_bottom.stop();
							progress.stop();
							grid.setVisibility(View.VISIBLE);
							adapter = new TopRatedGridAdapter(getActivity(), searchList, giphyModel);
							grid.setAdapter(adapter);
							adapter.notifyDataSetChanged();
							placeholerText.setVisibility(View.INVISIBLE);
							isLoading = false;
							if (offset != 0) {
								grid.smoothScrollToPosition(count);
							}
						} else {
							circular_progress_bottom.setVisibility(View.INVISIBLE);
							circular_progress_bottom.stop();
							progress.stop();
							placeholerText.setVisibility(View.VISIBLE);
							Toast.makeText(getActivity(), R.string.error_toast, Toast.LENGTH_SHORT).show();
						}
					}
				}
			});
		} else {
			adapter = new TopRatedGridAdapter(getActivity(), movieArr, giphyModel);
			grid.setAdapter(adapter);
			adapter.notifyDataSetChanged();
			placeholerText.setVisibility(View.INVISIBLE);
		}
	}
}
