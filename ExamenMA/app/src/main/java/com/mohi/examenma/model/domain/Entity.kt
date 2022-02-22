package com.mohi.examenma.model.domain

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "entity")
data class Entity(
    @PrimaryKey
    var id: Int = 0,
    @ColumnInfo
    val nume: String = "",
    @ColumnInfo
    val medie: Int = 0,
    @ColumnInfo
    val etaj: Int = 0,
    @ColumnInfo
    val orientare: Boolean = true,
    @ColumnInfo
    val camera: String = "",
    @ColumnInfo
    val status: Boolean = false,
    @ColumnInfo
    var isLocal: Boolean = false
)
