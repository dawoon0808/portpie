package com.portpie.app.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.portpie.app.data.model.OwnedAsset


@Database(entities = [OwnedAsset::class], version = 1)
 abstract class AppDatabase: RoomDatabase() {
    abstract fun ownedAssetDao(): OwnedAssetDao

    companion object{
        @Volatile private var INSTANCE : AppDatabase? = null

        fun getInstance(context: Context): AppDatabase=
            INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "asset_db"
                ).build()
                INSTANCE = instance
                instance
            }
    }
}