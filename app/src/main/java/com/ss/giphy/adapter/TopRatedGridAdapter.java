package com.ss.giphy.adapter;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.bumptech.glide.Glide;
import com.like.LikeButton;
import com.like.OnAnimationEndListener;
import com.like.OnLikeListener;
import com.ss.giphy.R;
import com.ss.giphy.model.GiphyEntity;
import com.ss.giphy.repository.GiphyViewModel;


import java.util.ArrayList;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;

/**
 * Created by Shubham
 * Adapter class to show the data returned from api call.
 */
public class TopRatedGridAdapter extends BaseAdapter implements OnLikeListener, OnAnimationEndListener{

	private LayoutInflater inflter;

	private Activity _ctx;

	private List<GiphyEntity> arr = new ArrayList<>();

	private GiphyViewModel giphyViewModel;

	private GifImageView mGif;

	private LikeButton mFav;

	/**
	 * Constructor
	 *
	 * @param ctx
	 * @param arr
	 * @param viewModel
	 */
	public TopRatedGridAdapter(Activity ctx, List<GiphyEntity> arr, GiphyViewModel viewModel) {
		_ctx = ctx;
		this.arr = arr;
		this.inflter = (LayoutInflater.from(ctx));
		this.giphyViewModel = viewModel;
	}

	@Override
	public int getCount() {
		return arr.size();
	}

	@Override
	public Object getItem(int i) {
		return null;
	}

	@Override
	public long getItemId(int i) {
		return 0;
	}

	@Override
	public View getView(int i, View view, ViewGroup viewGroup) {
		view = inflter.inflate(R.layout.grid_adapter, null);

		GiphyEntity entity = arr.get(i);

		mGif = view.findViewById(R.id.img_movie);
		mFav = view.findViewById(R.id.img_fav);
		mFav.setId(entity.getButtonId());
		mFav.setOnLikeListener(this);
		Glide.with(_ctx).load(entity.getUrl()).into(mGif);

		if (entity.isFav())
			mFav.setLiked(true);
		else
			mFav.setLiked(false);

		return view;
	}

	@Override
	public void onAnimationEnd(LikeButton likeButton) {
		// Perfrem action when animation ends.
	}

	@Override
	public void liked(LikeButton likeButton) {
		int id = likeButton.getId();
		GiphyEntity giphyEntity = giphyViewModel.loadGiphy(id);
		giphyEntity.setFav(true);
		giphyViewModel.updateGiphy(giphyEntity);
		likeButton.setLiked(true);
	}

	@Override
	public void unLiked(LikeButton likeButton) {
		int id = likeButton.getId();
		GiphyEntity giphyEntity = giphyViewModel.loadGiphy(id);
		giphyEntity.setFav(false);
		giphyViewModel.updateGiphy(giphyEntity);
		likeButton.setLiked(false);
	}
}
