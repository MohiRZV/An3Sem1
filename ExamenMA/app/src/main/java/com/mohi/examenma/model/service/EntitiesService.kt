package com.mohi.examenma.model.service

import com.mohi.examenma.model.domain.Entity
import retrofit2.http.*

interface EntitiesService {

    @GET("/toate")
    suspend fun getAll(): List<Entity>

    @POST("/inregistrare")
    suspend fun add(@Body entity: Entity): Int

    @FormUrlEncoded
    @POST("/validare")
    suspend fun confirm(@Field("id") id: Int, @Field("etaj") etaj: Int, @Field("camera") camera: String): Entity
}