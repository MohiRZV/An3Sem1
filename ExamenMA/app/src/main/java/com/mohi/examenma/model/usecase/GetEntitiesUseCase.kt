package com.mohi.examenma.model.usecase

import com.mohi.examenma.model.domain.Entity
import com.mohi.examenma.model.repo.EntitiesRepository
import javax.inject.Inject

interface GetEntitiesUseCase {
    suspend operator fun invoke(): List<Entity>
}

class BaseGetEntitiesUseCase @Inject constructor(
    private val repo: EntitiesRepository
) : GetEntitiesUseCase {
    override suspend fun invoke(): List<Entity> {
        return repo.getAll()
    }
}