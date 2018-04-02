package com.example.tejas.roomdatabaseexample.DataBase;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface UserDao {

    @Query("SELECT * FROM image_path")
    List<ImagePathEntity> getAll();

    @Query("SELECT * FROM image_path where asset_id LIKE  :assetId")
    ImagePathEntity findByAssestId(String assetId);

    @Query("SELECT COUNT(*) from image_path")
    int countUsers();

    @Insert
    void insertAll(ImagePathEntity... imagePathEntities);

    @Delete
    void delete(ImagePathEntity imagePathEntity);
}
