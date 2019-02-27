package com.ss.giphy.fragments;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;


import com.ss.giphy.R;
import com.ss.giphy.adapter.FavGridAdapter;
import com.ss.giphy.model.GiphyEntity;
import com.ss.giphy.repository.GiphyViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shubham
 * Fragment to show list of Gif's marked as favourite.
 */
public class FragmentFav extends Fragment {

	private GridView grid;
	private View view;
	private FavGridAdapter adapter;
	private ArrayList<GiphyEntity> movieArr = new ArrayList<>();
	private GiphyViewModel giphyModel;
	private SwipeRefreshLayout pullToRefresh;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_fav, container, false);
		init();
		return view;
	}

	/**
	 * Initialising  widgets.
	 */
	private void init() {
		grid = (GridView) view.findViewById(R.id.gridView);
		pullToRefresh = view.findViewById(R.id.pullToRefresh);
		giphyModel = ViewModelProviders.of(this).get(GiphyViewModel.class);

		//Favourite Gif Observable.
		giphyModel.getLiveFavGiphy().observe(this, new Observer<List<GiphyEntity>>() {
			@Override
			public void onChanged(@Nullable List<GiphyEntity> giphyEntities) {
				adapter = new FavGridAdapter(getActivity(), giphyEntities, giphyModel);
				grid.setAdapter(adapter);
			}
		});

		// Pull to referesh the list if the latestv data is not displayed.
		pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				giphyModel.getLiveFavGiphy().observe(getActivity(), new Observer<List<GiphyEntity>>() {
					@Override
					public void onChanged(@Nullable List<GiphyEntity> giphyEntities) {
						adapter = new FavGridAdapter(getActivity(), giphyEntities, giphyModel);
						grid.setAdapter(adapter);
						pullToRefresh.setRefreshing(false);
					}
				});
			}
		});
	}
}
