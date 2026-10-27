package com.example.xelvicfilters.engine

import androidx.lifecycle.ViewModel
import com.example.xelvicfilters.install.FakeFilterRepository
import com.example.xelvicfilters.render.FakeOverlayRenderer

class FilterViewModel : ViewModel() {
    // Rotate karne par Activity banti-bigadti hai, ye object zinda rehta hai.
    val engine: FilterEngine = DefaultFilterEngine(FakeFilterRepository(), FakeOverlayRenderer())
}