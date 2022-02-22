package com.mohi.examenma.model.usecase

import com.mohi.examenma.model.domain.Entity
import com.mohi.examenma.model.repo.EntitiesRepository
import javax.inject.Inject

interface ConfirmUseCase {
    suspend operator fun invoke(entity: Entity): Entity
}

class BaseCofirmUseCase @Inject constructor(
    private val repo: EntitiesRepository
) : ConfirmUseCase {
    override suspend fun invoke(entity: Entity): Entity {
        return repo.confirm(entity)
    }
}