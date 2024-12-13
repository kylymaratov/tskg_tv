package com.example.tvapp.utils

 import android.content.Context
 import android.graphics.Rect
 import android.view.View
 import android.widget.TextView
 import androidx.recyclerview.widget.RecyclerView

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

    class GridSpacingItemDecoration(
        private val spanCount: Int,
        private val spacing: Int,
        private val includeEdge: Boolean
    ) : RecyclerView.ItemDecoration() {

        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            val position = parent.getChildAdapterPosition(view)
            val column = position % spanCount

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount
                outRect.top = spacing

                outRect.right = (column + 1) * spacing / spanCount
                outRect.bottom = spacing
            } else {

                outRect.left = column * spacing / spanCount
                outRect.right = spacing - (column + 1) * spacing / spanCount
                outRect.top = spacing
                outRect.bottom = spacing
            }
        }
    }

}