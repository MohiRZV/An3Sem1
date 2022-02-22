package com.mohi.examenma.model.repo.localrepo

import android.util.Log
import com.mohi.examenma.model.domain.Entity
import com.mohi.examenma.model.repo.localrepo.EntityDao
import javax.inject.Inject

class LocalEntitiesRepository @Inject constructor(
    private val entityDao: EntityDao
){

    fun getAll(): List<Entity> {
        return entityDao.getAll()
    }

    fun save(entity: Entity): Int {
        Log.d("Mohi","Saved locally $entity")
        return entityDao.save(entity).toInt()
    }

    fun exists(id: Int): Boolean {
        return (entityDao.findById(id)>0)
    }

    fun nuke() {
        entityDao.nuke()
    }

    fun update(entity: Entity) {
        entityDao.update(entity)
    }
}