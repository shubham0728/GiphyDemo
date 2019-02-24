package com.ss.giphy.repository;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.ss.giphy.model.GiphyEntity;

import java.util.List;

/**
 * Created by Shubham
 */
public class GiphyViewModel extends AndroidViewModel {


	private GiphyRepository repo;
	private LiveData<List<GiphyEntity>> observableGiphy;
	private List<GiphyEntity> giphyEntityList;

	/**
	 * Constructor
	 *
	 * @param application
	 */
	public GiphyViewModel(@NonNull Application application) {
		super(application);
		repo = new GiphyRepository(application);
		observableGiphy = repo.getAllGiphy();
		giphyEntityList = repo.getGiphy();
	}

	public LiveData<List<GiphyEntity>> getGiphy() {
		return observableGiphy;
	}

	public List<GiphyEntity> getGiphywithoutLiveData() {
		return giphyEntityList;
	}

	public GiphyEntity loadGiphy(int id) {
		return repo.loadGiphy(id);
	}

	public void insert(GiphyEntity Giphy) {
		repo.insert(Giphy);
	}

	public void updateGiphy(GiphyEntity GiphyEntity) {
		repo.updateGiphy(GiphyEntity);
	}

	public List<GiphyEntity> getFavGiphy() {
		return repo.getFavGiphy();
	}

	public LiveData<List<GiphyEntity>> getLiveFavGiphy() {
		return repo.getLiveFavGiphy();
	}

	public void deleteAll() {
		repo.deleteAll();
	}
}


