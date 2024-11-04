package com.bella.android_demo_public.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.bella.android_demo_public.bean.User;

import java.util.List;

@Dao
public interface UserDao {

    @Query("select * from user")
    List<User> getAllUserList();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(User user);

    @Update
    void update(User user);

    @Delete
    void delete(User user);

}
