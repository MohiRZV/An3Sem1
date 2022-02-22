package com.mohi.examenma.model.repo.localrepo

import android.content.Context
import androidx.room.*
import com.mohi.examenma.model.domain.Entity
import java.sql.Timestamp
import java.time.Instant
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

const val DATABASE_NAME = "MA_exam_database"
const val DATABASE_VERSION = 1

@Database(entities = [Entity::class], version = DATABASE_VERSION, exportSchema = false)
@TypeConverters(Converters::class)
abstract class LocalDatabase: RoomDatabase() {
    abstract fun entityDao(): EntityDao
    companion object{
        @Volatile
        private var INSTANCE: LocalDatabase?=null
        fun getDatabase(context: Context): LocalDatabase {
            val tempInstance = INSTANCE
            if(tempInstance!=null)
                return tempInstance

            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LocalDatabase::class.java,
                    DATABASE_NAME
                ).allowMainThreadQueries().fallbackToDestructiveMigration().build()
                INSTANCE = instance
                return instance
            }
        }
    }
}

class Converters {
    @TypeConverter
    fun fromTimestampToLocalDateTime(value: Long): LocalDateTime {
        return LocalDateTime.ofInstant(
            Instant.ofEpochMilli(value),
            TimeZone.getDefault().toZoneId())
    }

    @TypeConverter
    fun LocalDateTimeToTimestamp(date: LocalDateTime): Long {
        return Timestamp.valueOf(date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss"))).time
    }

    @TypeConverter
    fun fromTimestampToDate(value: Long): Date {
        return Date(value)
    }

    @TypeConverter
    fun DateToTimestamp(date: Date): Long {
        return date.time
    }
}