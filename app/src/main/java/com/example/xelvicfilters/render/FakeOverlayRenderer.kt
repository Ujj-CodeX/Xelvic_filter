package com.example.xelvicfilters.render

import android.util.Log

class FakeOverlayRenderer : OverlayRenderer {
    override fun setActiveFilter(filterId: String?) {
        Log.d("FakeOverlayRenderer", "active filter = $filterId")
    }
}