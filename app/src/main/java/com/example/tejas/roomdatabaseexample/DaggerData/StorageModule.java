package com.example.tejas.roomdatabaseexample.DaggerData;


import android.arch.persistence.room.Room;
import android.content.Context;

import com.example.tejas.roomdatabaseexample.DataBase.AppDatabase;
import com.example.tejas.roomdatabaseexample.DataBase.UserDao;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class StorageModule {

    private Context context;

    public StorageModule(Context context) {
        this.context = context;
    }

    @Singleton
    @Provides
    public Context provideContext(){
        return context;
    }

    @Singleton @Provides
    public AppDatabase provideMyDatabase(Context context){

        return Room.databaseBuilder(context, AppDatabase.class, "user-database").allowMainThreadQueries().build();
    }

    @Singleton @Provides
    public UserDao provideUserDao(AppDatabase myDatabase){
        return myDatabase.userDao();
    }

}
