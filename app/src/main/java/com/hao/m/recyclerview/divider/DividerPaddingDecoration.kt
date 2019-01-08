package com.hao.m.recyclerview.divider

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.View
import com.hao.m.utils.UIUtils

/**
 * Created by wangpw on 2018/6/26.
 *
 * 绘制带padding的分割线
 */
class DividerPaddingDecoration(private val mDividerWidth: Int, color: Int, private  val padding: Int) : RecyclerView.ItemDecoration() {
    private val mPaint: Paint?


    init {
        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaint!!.color = UIUtils.getColor(color)
        mPaint.style = Paint.Style.FILL
    }


    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State?) {
        super.getItemOffsets(outRect, view, parent, state)
        val itemPosition = (view.layoutParams as RecyclerView.LayoutParams).viewLayoutPosition
        val spanCount = getSpanCount(parent)
        val childCount = parent.adapter.itemCount


        val isLastRow = isLastRow(parent, itemPosition, spanCount, childCount)
        val isLastColumn = isLastColumn(parent, itemPosition, spanCount, childCount)

        val top = 0
        val left: Int
        var right: Int
        var bottom: Int
        val eachWidth = (spanCount - 1) * mDividerWidth / spanCount
        val dl = mDividerWidth - eachWidth

        left = itemPosition % spanCount * dl
        right = eachWidth - left
        bottom = mDividerWidth
        if (isLastRow) bottom = 0
        if (isLastColumn) right = 0
        outRect.set(left, top, right, bottom)
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State?) {
        super.onDraw(c, parent, state)
        draw(c, parent)
    }


    //绘制item分割线
    private fun draw(canvas: Canvas, parent: RecyclerView) {
        val childCount = parent.childCount
        val spanCount = getSpanCount(parent)
        val colCount = childCount / spanCount
        val pad = UIUtils.dip2px(padding)
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val layoutParams = child.layoutParams as RecyclerView.LayoutParams

            //画水平分隔线
            var left = child.left
            var right = child.right
            var top = child.bottom + layoutParams.bottomMargin
            var bottom = top + mDividerWidth
            if (mPaint != null) {
                if (i % spanCount == 0) {
                    left += pad
                }
                if ((i + 1) % spanCount == 0) {
                    right -= pad
                }
                canvas.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat(), mPaint)
            }
            //画垂直分割线
            top = child.top
            bottom = child.bottom + mDividerWidth
            left = child.right + layoutParams.rightMargin
            right = left + mDividerWidth
            if (mPaint != null) {
                if (i < spanCount) {
                    top += pad
                }
                if (i > (childCount - spanCount - 1)) {
                    bottom -= pad
                }
                canvas.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat(), mPaint)
            }
        }
    }

    private fun isLastColumn(parent: RecyclerView, pos: Int, spanCount: Int,
                             childCount: Int): Boolean {
        var childCount = childCount
        val layoutManager = parent.layoutManager
        if (layoutManager is GridLayoutManager) {
            if ((pos + 1) % spanCount == 0) {// 如果是最后一列，则不需要绘制右边
                return true
            }
        } else if (layoutManager is StaggeredGridLayoutManager) {
            val orientation = layoutManager
                    .orientation
            if (orientation == StaggeredGridLayoutManager.VERTICAL) {
                if ((pos + 1) % spanCount == 0)
                // 如果是最后一列，则不需要绘制右边
                {
                    return true
                }
            } else {
                childCount = childCount - childCount % spanCount
                if (pos >= childCount)
                // 如果是最后一列，则不需要绘制右边
                    return true
            }
        }
        return false
    }

    private fun isLastRow(parent: RecyclerView, pos: Int, spanCount: Int,
                          childCount: Int): Boolean {
        var childCount = childCount
        val layoutManager = parent.layoutManager
        if (layoutManager is GridLayoutManager) {
            val lines = if (childCount % spanCount == 0) childCount / spanCount else childCount / spanCount + 1
            return lines == pos / spanCount + 1
        } else if (layoutManager is StaggeredGridLayoutManager) {
            val orientation = layoutManager
                    .orientation
            // StaggeredGridLayoutManager 且纵向滚动
            if (orientation == StaggeredGridLayoutManager.VERTICAL) {
                childCount = childCount - childCount % spanCount
                // 如果是最后一行，则不需要绘制底部
                if (pos >= childCount)
                    return true
            } else {
                // 如果是最后一行，则不需要绘制底部
                if ((pos + 1) % spanCount == 0) {
                    return true
                }
            }
        }
        return false
    }

    private fun isFirstRow(parent: RecyclerView, pos: Int, spanCount: Int,
                           childCount: Int): Boolean {
        var childCount = childCount
        val layoutManager = parent.layoutManager
        if (layoutManager is GridLayoutManager) {
            return pos / spanCount + 1 == 1
        } else if (layoutManager is StaggeredGridLayoutManager) {
            val orientation = layoutManager
                    .orientation
            // StaggeredGridLayoutManager 且纵向滚动
            if (orientation == StaggeredGridLayoutManager.VERTICAL) {
                childCount = childCount - childCount % spanCount
                // 如果是最后一行，则不需要绘制底部
                if (pos >= childCount)
                    return true
            } else {
                // 如果是最后一行，则不需要绘制底部
                if ((pos + 1) % spanCount == 0) {
                    return true
                }
            }
        }
        return false
    }

    //获取列数
    private fun getSpanCount(parent: RecyclerView): Int {
        var spanCount = -1
        val layoutManager = parent.layoutManager
        if (layoutManager is GridLayoutManager) {

            spanCount = layoutManager.spanCount
        } else if (layoutManager is StaggeredGridLayoutManager) {
            spanCount = layoutManager
                    .spanCount
        }
        return spanCount
    }
}