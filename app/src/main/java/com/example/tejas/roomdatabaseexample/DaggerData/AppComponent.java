package com.example.tejas.roomdatabaseexample.DaggerData;


import com.example.tejas.roomdatabaseexample.MainActivity;

import javax.inject.Singleton;

import dagger.Component;


@Singleton
@Component(modules = {StorageModule.class})
public interface AppComponent {
    void inject(MainActivity mainActivity);
}