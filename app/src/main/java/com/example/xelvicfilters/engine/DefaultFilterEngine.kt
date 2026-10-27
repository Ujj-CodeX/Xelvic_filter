package com.example.xelvicfilters.engine

import com.example.xelvicfilters.core.FilterError
import com.example.xelvicfilters.core.FilterInfo
import com.example.xelvicfilters.install.FilterRepository
import com.example.xelvicfilters.render.OverlayRenderer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class DefaultFilterEngine(
    private val repository: FilterRepository,
    private val renderer: OverlayRenderer
) : FilterEngine {

    private val _filters = MutableStateFlow<List<FilterInfo>>(emptyList())
    override val filters: StateFlow<List<FilterInfo>> = _filters.asStateFlow()

    private val _selectedId = MutableStateFlow<String?>(null)
    override val selectedId: StateFlow<String?> = _selectedId.asStateFlow()

    private val _errors = MutableSharedFlow<FilterError>(extraBufferCapacity = 8)
    override val errors: Flow<FilterError> = _errors.asSharedFlow()

    override suspend fun refresh() {
        _filters.value = repository.fetchCatalogue()
    }

    override suspend fun install(id: String) {
        repository.install(id)
            .onSuccess {
                _filters.update { list ->
                    list.map { if (it.id == id) it.copy(installed = true) else it }
                }
            }
            .onFailure {
                _errors.tryEmit(FilterError.InstallFailed(id, it.message ?: "unknown error"))
            }
    }

    override fun select(id: String) {
        val filter = _filters.value.firstOrNull { it.id == id }
        if (filter == null || !filter.installed) {
            _errors.tryEmit(FilterError.Unsupported(id, "Filter installed nahi hai"))
            return
        }
        renderer.setActiveFilter(id)
        _selectedId.value = id
    }

    override fun clear() {
        renderer.setActiveFilter(null)
        _selectedId.value = null
    }
}