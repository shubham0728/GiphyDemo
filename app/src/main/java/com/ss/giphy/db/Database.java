package com.ss.giphy.db;


import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.ss.giphy.model.GiphyEntity;

/**
 * Created by Shubham
 * Store api call data in local Db. Room persistence library has been used to store information locally.
 */
@android.arch.persistence.room.Database(entities = {GiphyEntity.class}, version = 1, exportSchema = false)
public abstract class Database extends RoomDatabase {

	public abstract GiphyDao giphyDao();

	private static Database INSTANCE;

	// Name of the database
	public static final String DATABASE_NAME = "giphy_database";

	/**
	 * Database Instance.
	 *
	 * @param context
	 * @return
	 */
	public static Database getDatabase(final Context context) {
		if (INSTANCE == null) {
			synchronized (Database.class) {
				if (INSTANCE == null) {
					INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
							Database.class, DATABASE_NAME)
							.fallbackToDestructiveMigration()
							.allowMainThreadQueries()
							.build();
				}
			}
		}
		return INSTANCE;
	}
}
