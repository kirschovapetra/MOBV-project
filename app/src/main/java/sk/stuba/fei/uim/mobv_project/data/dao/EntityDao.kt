package sk.stuba.fei.uim.mobv_project.data.dao

import androidx.room.*

@Dao
abstract class EntityDao<T> {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insert(entity: T): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insertList(entityList: List<T>): List<Long>

    @Update
    abstract suspend fun update(entity: T): Int

    @Update
    abstract suspend fun updateList(entityList: List<T>): Int

    @Delete
    abstract suspend fun delete(vararg entity: T): Int

    @Transaction
    open suspend fun insertOrUpdate(entity: T) {
        val id = insert(entity)
        if (id == -1L) update(entity)
    }

    @Transaction
    open suspend fun insertOrUpdateList(entityList: List<T>) {
        val insertResult = insertList(entityList)
        val updateList = mutableListOf<T>()

        for (i in insertResult.indices) {
            if (insertResult[i] == -1L) updateList.add(entityList[i])
        }

        if (updateList.isNotEmpty()) updateList(updateList)
    }
}