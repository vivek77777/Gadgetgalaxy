package com.example.gadgetgalaxy.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.gadgetgalaxy.data.model.ProductEntity

@Database(entities = [ProductEntity::class], version = 1)
public abstract class ProductRoomDB : RoomDatabase() {
    abstract fun productDao(): ProductDao
}