package com.ss.giphy.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.ss.giphy.model.GiphyEntity;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

/**
 * Created by Shubham
 */

@Dao
public interface GiphyDao {

	/**
	 * Returns observable data from local db.
	 *
	 * @return
	 */
	@Query("SELECT * FROM Giphy")
	LiveData<List<GiphyEntity>> loadAllGiphy();

	/**
	 * Returns data from local db.
	 *
	 * @return
	 */
	@Query("SELECT * FROM Giphy")
	List<GiphyEntity> getAllGiphy();

	/**
	 * Returns single entity based on id.
	 *
	 * @param id
	 * @return
	 */
	@Query("SELECT * FROM Giphy WHERE buttonId like :id")
	GiphyEntity getGiphy(int id);

	/**
	 * Insert data in local db.
	 *
	 * @param Giphy
	 */
	@Insert
	void insert(GiphyEntity Giphy);

	/**
	 * Delete all data from local db.
	 */
	@Query("Delete from Giphy")
	void deleteAll();

	/**
	 * Update data for single entity.
	 *
	 * @param Giphy
	 */
	@Update(onConflict = REPLACE)
	void update(GiphyEntity Giphy);

	/**
	 * Returns Gif marked as favourite.
	 *
	 * @return
	 */
	@Query("select * from Giphy where fav == 1")
	List<GiphyEntity> getFavGiphy();

	/**
	 * Returns observable favourite gif.
	 *
	 * @return
	 */
	@Query("select * from Giphy where fav == 1")
	LiveData<List<GiphyEntity>> getLiveFavGiphy();

}
