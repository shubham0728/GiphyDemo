package com.ss.giphy.repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.os.AsyncTask;

import com.ss.giphy.db.Database;
import com.ss.giphy.db.GiphyDao;
import com.ss.giphy.model.GiphyEntity;

import java.util.List;

/**
 * Abstracted Repository as promoted by the Architecture Guide.
 * https://developer.android.com/topic/libraries/architecture/guide.html
 * Created by Shubham
 */

public class GiphyRepository {

	private GiphyDao giphyDao;
	private LiveData<List<GiphyEntity>> giphyLive;
	private List<GiphyEntity> giphyList;
	private GiphyEntity giphyEntity;
	private Database db;

	/**
	 * Constructor
	 *
	 * @param context
	 */
	public GiphyRepository(Context context) {
		db = Database.getDatabase(context);
		giphyDao = db.giphyDao();
		giphyLive = giphyDao.loadAllGiphy();
		giphyList = giphyDao.getAllGiphy();

	}

	LiveData<List<GiphyEntity>> getAllGiphy() {
		return giphyLive;
	}

	List<GiphyEntity> getGiphy() {
		return giphyList;
	}


	public GiphyEntity loadGiphy(final int id) {
		if (giphyDao.getGiphy(id) != null) {
			giphyEntity = giphyDao.getGiphy(id);
		}
		return giphyEntity;
	}

	public List<GiphyEntity> getFavGiphy() {
		return giphyDao.getFavGiphy();
	}

	public LiveData<List<GiphyEntity>> getLiveFavGiphy() {
		return giphyDao.getLiveFavGiphy();
	}

	/**
	 * Insert data in local db using Asyc Task.
	 *
	 * @param Giphy
	 */
	public void insert(GiphyEntity Giphy) {
		new insertAsyncTask(giphyDao).execute(Giphy);
	}

	private static class insertAsyncTask extends AsyncTask<GiphyEntity, Void, Void> {

		private GiphyDao mAsyncTaskDao;

		insertAsyncTask(GiphyDao dao) {
			mAsyncTaskDao = dao;
		}


		@Override
		protected Void doInBackground(GiphyEntity... giphyEntities) {
			try {
				mAsyncTaskDao.insert(giphyEntities[0]);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
	}

	/**
	 * Update data in local db using Async task.
	 *
	 * @param GiphyEntity
	 */
	public void updateGiphy(GiphyEntity GiphyEntity) {
		new UpdateAsyncTask(giphyDao).execute(GiphyEntity);
	}

	private static class UpdateAsyncTask extends AsyncTask<GiphyEntity, Void, Void> {

		GiphyDao GiphyViewModelDAO;

		public UpdateAsyncTask(GiphyDao giphyViewModelDAO) {
			this.GiphyViewModelDAO = giphyViewModelDAO;
		}

		@Override
		protected Void doInBackground(GiphyEntity... giphyEntities) {
			try {
				GiphyViewModelDAO.update(giphyEntities[0]);
			}catch (Exception e){
				e.printStackTrace();
			}
			return null;
		}
	}

	public void deleteAll() {
		if (giphyDao != null) {
			giphyDao.deleteAll();
		}
	}

}
