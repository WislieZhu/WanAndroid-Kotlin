package com.wislie.wanandroid.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 *    author : Wislie
 *    e-mail : 254457234@qq.comn
 *    date   : 2023/1/23 9:27 PM
 *    desc   :
 *    version: 1.0
 */
@Database(version = 1, entities = [SearchKey::class], exportSchema = false)
abstract class AppDatabase :RoomDatabase(){
    abstract fun getSearchKeyDao():SearchKeyDao

    companion object {
        private var instance: AppDatabase? = null

        //写一个双重校验的单利模式
        fun getDatabaseSingleton(context: Context): AppDatabase {
            if (instance == null) {
                synchronized(AppDatabase::class.java) {
                    if (instance == null) {
                        instance = Room.databaseBuilder(
                            context.applicationContext,
                            AppDatabase::class.java,
                            "app__lang_database"
                        ).allowMainThreadQueries().build()
                            .apply { instance = this }
                    }
                }
            }
            return instance!!
        }
    }
}