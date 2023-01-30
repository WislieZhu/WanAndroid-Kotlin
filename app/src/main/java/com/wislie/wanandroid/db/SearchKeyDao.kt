package com.wislie.wanandroid.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

/**
 *    author : Wislie
 *    e-mail : 254457234@qq.comn
 *    date   : 2023/1/23 9:13 PM
 *    desc   :
 *    version: 1.0
 */
@Dao
interface SearchKeyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSearchKey(searchKey: SearchKey):Long  //插入

    @Query("select * from SearchKey ORDER BY mills DESC")
    fun queryAllSearchKey(): PagingSource<Int,SearchKey> //根据mills 搜索列表

    @Query("delete from SearchKey where hotKey=:name")
    fun deleteSearchKeyByName(name: String):Int

    @Query("DELETE FROM SearchKey")
    fun deleteAllSearchHistory():Int
}