package com.ss.giphy.model;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by Shubham
 */
public class GiphyModel implements Parcelable {

	@SerializedName("data")
	private List<JsonObject> giphyEntityList;


	public GiphyModel() {

	}

	protected GiphyModel(Parcel in) {
		this.giphyEntityList = in.readArrayList(GiphyEntity.class.getClassLoader());

	}

	public List<JsonObject> getGiphyEntityList() {
		return giphyEntityList;
	}

	public void setGiphyEntityList(List<JsonObject> giphyEntityList) {
		this.giphyEntityList = giphyEntityList;
	}

	public static final Creator<GiphyModel> CREATOR = new Creator<GiphyModel>() {
		@Override
		public GiphyModel createFromParcel(Parcel in) {
			return new GiphyModel(in);
		}

		@Override
		public GiphyModel[] newArray(int size) {
			return new GiphyModel[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeList(this.giphyEntityList);
	}
}
