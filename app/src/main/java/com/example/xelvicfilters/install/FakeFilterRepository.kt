package com.example.xelvicfilters.install

import com.example.xelvicfilters.core.FilterInfo
import kotlinx.coroutines.delay

class FakeFilterRepository : FilterRepository {
    override suspend fun fetchCatalogue(): List<FilterInfo> {
        delay(400)   // network latency ka dikhawa
        return listOf(
            FilterInfo("gold-crown", "Gold Crown", "1.0.0"),
            FilterInfo("round-glasses", "Round Glasses", "1.0.0"),
            FilterInfo("broken-pack", "Broken Pack (error test)", "1.0.0")
        )
    }

    override suspend fun install(id: String): Result<Unit> {
        delay(600)
        return if (id == "broken-pack") {
            Result.failure(IllegalStateException("Checksum mismatch"))
        } else {
            Result.success(Unit)
        }
    }
}