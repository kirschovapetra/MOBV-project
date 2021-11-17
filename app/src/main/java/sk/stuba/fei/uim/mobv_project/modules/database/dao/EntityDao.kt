package sk.stuba.fei.uim.mobv_project.modules.database.dao

import androidx.room.*

interface EntityDao<T> {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(vararg t: T)

    @Update
    fun update(vararg t: T)

    @Delete
    fun delete(vararg t: T)

}