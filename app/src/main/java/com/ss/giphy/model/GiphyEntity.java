package com.ss.giphy.model;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

/**
 * Created by Shubham
 * Model class
 */
@Entity(tableName = "Giphy")
public class GiphyEntity implements Parcelable, Comparable<GiphyEntity> {

	@NonNull
	@PrimaryKey
	@ColumnInfo(name = "id")
	private String id;

	@ColumnInfo(name = "type")
	private String type;

	@ColumnInfo(name = "url")
	private String url;

	@ColumnInfo(name = "buttonId")
	private int buttonId;

	@ColumnInfo(name = "fav")
	private boolean fav;

	public GiphyEntity(String id, String type, String url) {
		this.id = id;
		this.type = type;
		this.url = url;
	}

	public GiphyEntity() {

	}

	@NonNull
	public String getId() {
		return id;
	}

	public void setId(@NonNull String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public boolean isFav() {
		return fav;
	}

	public void setFav(boolean fav) {
		this.fav = fav;
	}

	public int getButtonId() {
		return buttonId;
	}

	public void setButtonId(int buttonId) {
		this.buttonId = buttonId;
	}

	public static final Creator<GiphyEntity> CREATOR = new Creator<GiphyEntity>() {
		@Override
		public GiphyEntity createFromParcel(Parcel source) {
			return new GiphyEntity(source);
		}

		@Override
		public GiphyEntity[] newArray(int size) {
			return new GiphyEntity[size];
		}
	};


	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int i) {
		dest.writeString(this.id);
		dest.writeString(this.type);
		dest.writeString(this.url);
		dest.writeInt(this.buttonId);
		dest.writeByte((byte) (fav ? 1 : 0));
	}

	protected GiphyEntity(Parcel in) {
		this.id = in.readString();
		this.type = in.readString();
		this.url = in.readString();
		this.buttonId = in.readInt();
		this.fav = in.readByte() != 0;
	}

	@Override
	public int compareTo(@NonNull GiphyEntity GiphyEntity) {
		return 0;
	}
}