package com.mohi.examenma.model.repo.localrepo

import androidx.room.*
import com.mohi.examenma.model.domain.Entity

@Dao
interface EntityDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(entity: Entity) : Long

    @Query("select * from entity order by etaj ASC")
    fun getAll(): List<Entity>

    @Query("select COUNT(*) from entity where id= :id")
    fun findById(id: Int): Int

    @Update
    fun update(entity: Entity)

    @Query("delete from entity")
    fun nuke()

}