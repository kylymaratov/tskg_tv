package com.example.tvapp.utils

 import android.content.Context
 import android.widget.TextView

class Common {

    companion object {
        fun getWidthInPercent(context: Context, percent: Int): Int {
            val width = context.resources.displayMetrics.widthPixels ?: 0
            return (width * percent) / 100
        }

        fun getHeightInPercent(context: Context, percent: Int): Int {
            val height = context.resources.displayMetrics.heightPixels ?: 0
            return (height * percent) / 100
        }

        fun TextView.isEllipsized(ellipsize: (isEllipsized: Boolean) -> Unit) {
            val lineCount = layout?.lineCount ?: 0
            if (lineCount > 0) {
                val ellipseCount = layout?.getEllipsisCount(lineCount - 1) ?: 0
                ellipsize.invoke(ellipseCount > 0)
            }
        }


    }

}