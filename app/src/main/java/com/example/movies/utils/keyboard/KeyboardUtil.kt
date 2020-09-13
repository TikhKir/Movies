package com.example.movies.utils.keyboard

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

abstract class KeyboardUtil {
    companion object{

        fun View.hideKeyboard(): Boolean {
            try {
                val inputMethodManager =
                    context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                return inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
            } catch (ignored: RuntimeException) {
            }
            return false
        }

    }
}