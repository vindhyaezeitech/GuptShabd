package com.shabdamsdk.db;



import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import io.reactivex.Observable;


@Dao
public interface TaskDao {

    @Query("SELECT * FROM Task")
    Observable<List<Task>> getAll();

    @Insert
    void insert(Task task);

}
