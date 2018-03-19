package com.example.tejas.roomdatabaseexample.utils;


import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.tejas.roomdatabaseexample.database.AppDatabase;
import com.example.tejas.roomdatabaseexample.entity.ImagePathEntity;

import java.util.List;

public class DatabaseInitializer {

    private static final String TAG = DatabaseInitializer.class.getName();

    public static void populateAsync(@NonNull final  AppDatabase db,String imageUrl,String localPath,String assestId) {
        PopulateDbAsync task = new PopulateDbAsync(db,imageUrl,localPath,assestId);
        task.execute();
    }

    public static void populateSync(@NonNull final AppDatabase db,String imageUrl,String localPath,String assestId) {
        populateWithTestData(db,imageUrl,localPath,assestId);
    }

    private static ImagePathEntity addUser(final AppDatabase db, ImagePathEntity imagePathEntity) {
        db.userDao().insertAll(imagePathEntity);
        return imagePathEntity;
    }

    private static void populateWithTestData(AppDatabase db,String imageUrl,String localPath,String assestId) {
        ImagePathEntity imagePathEntity = new ImagePathEntity();
        imagePathEntity.setImageUrl(imageUrl);
        imagePathEntity.setLocalPath(localPath);
        imagePathEntity.setAssetId(assestId);
        addUser(db, imagePathEntity);

        List<ImagePathEntity> imagePathEntityList = db.userDao().getAll();
        Log.d(DatabaseInitializer.TAG, "Rows Count: " + imagePathEntityList.size());
    }

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final AppDatabase mDb;
        String mImageUrl,mLocalPath,mAssestId;
        PopulateDbAsync(AppDatabase db,String imageUrl,String localPath,String assestId) {
            mDb = db;
            mImageUrl=imageUrl;
            mLocalPath=localPath;
            mAssestId=assestId;
        }

        @Override
        protected Void doInBackground(final Void... params) {
            populateWithTestData(mDb,mImageUrl,mLocalPath,mAssestId);
            return null;
        }

    }
}
