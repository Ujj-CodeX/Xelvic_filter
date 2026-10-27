package com.example.xelvicfilters.install

import com.example.xelvicfilters.core.FilterInfo

interface FilterRepository {
    suspend fun fetchCatalogue(): List<FilterInfo>
    suspend fun install(id: String): Result<Unit>
}