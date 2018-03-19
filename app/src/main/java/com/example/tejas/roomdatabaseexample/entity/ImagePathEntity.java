package com.example.tejas.roomdatabaseexample.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "image_path")
public class ImagePathEntity {

    @PrimaryKey(autoGenerate = true)
    private int pid;

    @ColumnInfo(name = "image_url")
    private String imageUrl;

    @ColumnInfo(name = "local_path")
    private String localPath;

    @ColumnInfo(name = "asset_id")
    private String assetId;

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    public String getAssetId() {
        return assetId;
    }

    public void setAssetId(String assetId) {
        this.assetId = assetId;
    }

}
