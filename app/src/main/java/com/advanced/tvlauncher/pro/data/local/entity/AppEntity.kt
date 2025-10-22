package com.advanced.tvlauncher.pro.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "apps")
data class AppEntity(
    @PrimaryKey
    @ColumnInfo(name = "package_name")
    val packageName: String,
    
    @ColumnInfo(name = "name")
    val name: String,
    
    @ColumnInfo(name = "class_name")
    val className: String?,
    
    @ColumnInfo(name = "category")
    val category: String,
    
    @ColumnInfo(name = "is_favorite")
    val isFavorite: Boolean,
    
    @ColumnInfo(name = "is_system_app")
    val isSystemApp: Boolean,
    
    @ColumnInfo(name = "install_time")
    val installTime: Long,
    
    @ColumnInfo(name = "update_time")
    val updateTime: Long,
    
    @ColumnInfo(name = "last_used")
    val lastUsed: Long,
    
    @ColumnInfo(name = "usage_count")
    val usageCount: Int,
    
    @ColumnInfo(name = "version_name")
    val versionName: String?,
    
    @ColumnInfo(name = "version_code")
    val versionCode: Int
)
