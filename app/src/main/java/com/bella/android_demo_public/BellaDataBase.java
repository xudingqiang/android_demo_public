package com.bella.android_demo_public;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.bella.android_demo_public.bean.User;
import com.bella.android_demo_public.dao.UserDao;

@Database(entities = {User.class}, version = 1 ,exportSchema = false)
public abstract class BellaDataBase extends RoomDatabase {

    public abstract UserDao userDao();

    private static BellaDataBase instance;

    public static synchronized BellaDataBase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context,
                            BellaDataBase.class, "bella_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
