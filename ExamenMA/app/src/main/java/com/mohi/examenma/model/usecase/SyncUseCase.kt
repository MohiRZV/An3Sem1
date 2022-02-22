package com.mohi.examenma.model.usecase

import com.mohi.examenma.model.domain.Entity
import com.mohi.examenma.model.repo.EntitiesRepository
import javax.inject.Inject

interface SyncUseCase {
    suspend operator fun invoke(): List<Entity>
}

class BaseSyncUseCase @Inject constructor(
    private val repo: EntitiesRepository
) : SyncUseCase {
    override suspend fun invoke(): List<Entity> {
        return repo.backOnline()
    }
}