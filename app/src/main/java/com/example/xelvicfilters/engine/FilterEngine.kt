package com.example.xelvicfilters.engine

import com.example.xelvicfilters.core.FilterError
import com.example.xelvicfilters.core.FilterInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

// UI sirf isse baat karega. iOS mein same API Swift mein banegi.
interface FilterEngine {
    val filters: StateFlow<List<FilterInfo>>
    val selectedId: StateFlow<String?>
    val errors: Flow<FilterError>
    suspend fun refresh()
    suspend fun install(id: String)
    fun select(id: String)
    fun clear()
}