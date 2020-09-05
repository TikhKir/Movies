package com.example.movies.utils

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.GridLayoutManager

//for remove predicting animations in main recyclerViews
class NpaGridLayoutManager : GridLayoutManager {

    override fun supportsPredictiveItemAnimations(): Boolean {
        return false
    }

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
    }

    constructor(context: Context?, spanCount: Int) : super(context, spanCount) {}

    constructor(
        context: Context?,
        spanCount: Int,
        orientation: Int,
        reverseLayout: Boolean
    ) : super(context, spanCount, orientation, reverseLayout) {
    }
}