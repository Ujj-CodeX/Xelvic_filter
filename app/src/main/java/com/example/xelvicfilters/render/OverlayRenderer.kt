package com.example.xelvicfilters.render

interface OverlayRenderer {
    fun setActiveFilter(filterId: String?)   // null = clear
}