package sk.stuba.fei.uim.mobv_project.data.dao

import androidx.room.*

@Dao
interface EntityDao<T> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg entity: T): List<Long>
    @Update
    suspend fun update(vararg entity: T): Int
    @Delete
    suspend fun delete(vararg entity: T): Int
}