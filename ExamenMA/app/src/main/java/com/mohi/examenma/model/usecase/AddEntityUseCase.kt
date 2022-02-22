package com.mohi.examenma.model.usecase

import com.mohi.examenma.model.domain.Entity
import com.mohi.examenma.model.repo.EntitiesRepository
import javax.inject.Inject

interface AddEntityUseCase {
    suspend operator fun invoke(entity: Entity): Int
}

class BaseAddEntityUseCase @Inject constructor(
    private val repo: EntitiesRepository
) : AddEntityUseCase {
    override suspend fun invoke(entity: Entity): Int {
        return repo.add(entity)
    }
}