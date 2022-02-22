package com.mohi.examenma.model.repo

import android.util.Log
import com.mohi.examenma.MainActivity
import com.mohi.examenma.model.domain.Entity
import com.mohi.examenma.model.repo.localrepo.LocalDatabase
import com.mohi.examenma.model.repo.localrepo.LocalEntitiesRepository
import com.mohi.examenma.model.service.EntitiesService
import com.mohi.examenma.utils.InternetStatus
import com.mohi.examenma.utils.InternetStatusLive
import retrofit2.HttpException
import java.lang.Exception
import java.net.ConnectException
import javax.inject.Inject

interface EntitiesRepository {
    suspend fun getAll(): List<Entity>
    suspend fun add(entity: Entity): Int
    suspend fun backOnline(): List<Entity>
    suspend fun confirm(entity: Entity): Entity
}

class BaseEntitiesRepository @Inject constructor(
    private val service: EntitiesService
) : EntitiesRepository {
    private var retrieved = false
    private val localRepo = LocalEntitiesRepository(LocalDatabase.getDatabase(MainActivity.bcontext).entityDao())

    private val myCustomComparatorAll=  Comparator<Entity> { a, b ->
        when {
            (a.etaj > b.etaj) -> 1
            (a.etaj == b.etaj && a.camera > b.camera) -> 1
            (a.etaj == b.etaj && a.camera < b.camera) -> -1
            (a.etaj == b.etaj && a.orientare > b.orientare) -> 1
            (a.etaj == b.etaj && a.orientare < b.orientare) -> -1
            (a.etaj == b.etaj && a.orientare == b.orientare && a.camera == b.camera && a.nume > b.nume) -> 1
            (a.etaj == b.etaj && a.orientare == b.orientare && a.camera == b.camera && a.nume < b.nume) -> -1
            else -> 0
        }
    }
    override suspend fun getAll(): List<Entity> {
        val list = localRepo.getAll()
        if(InternetStatusLive.status.value?.equals(InternetStatus.ONLINE) == true && !retrieved && list.isEmpty()){
            service.getAll().forEach {
                if (!localRepo.exists(it.id))
                    localRepo.save(it)
            }
            retrieved = true
        }
        list.sortedWith(myCustomComparatorAll)
        return list
    }

    override suspend fun add(entity: Entity): Int {
        return if(InternetStatusLive.status.value?.equals(InternetStatus.OFFLINE) == true) {
            entity.isLocal = true
            Log.d("Mohi","Saving $entity locally")
            localRepo.save(entity)
        } else {
            Log.d("Mohi","Saving $entity")
            val saved = service.add(entity)
            entity.id = saved
            localRepo.save(entity)
            saved
        }
    }

    override suspend fun backOnline(): List<Entity> {
        retrieved = true
        localRepo.getAll().forEach { entity ->
            if(entity.isLocal) {
                try {
                    service.add(entity)
                } catch (ex: HttpException) {
                    //not relevant here
                }
            }
        }
        var entities = listOf<Entity>()
        try {
            entities = service.getAll()
            entities.forEach {
                if(!localRepo.exists(it.id))
                    localRepo.save(it)
            }
        } catch (ex: ConnectException) {
            Log.d("ConnectException", ex.message.toString())
        }
        return entities
    }

    override suspend fun confirm(entity: Entity): Entity {
        localRepo.update(entity)
        var savedEntity = entity.copy(status = true)
        Log.d("Mohi", "Confirming $entity locally")
        if(InternetStatusLive.status.value?.equals(InternetStatus.ONLINE) == true) {
            savedEntity = service.confirm(entity.id, entity.etaj, entity.camera)
            Log.d("Mohi", "$entity confirmed on server")
        }
        return savedEntity
    }
}