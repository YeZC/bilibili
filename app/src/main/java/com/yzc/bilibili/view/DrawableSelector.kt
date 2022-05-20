package com.yzc.bilibili.view

import android.content.Context
import android.graphics.drawable.Drawable
import android.graphics.drawable.StateListDrawable
import androidx.annotation.DrawableRes
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat

class DrawableSelector(
    val context: Context,
    @DrawableRes val defIds: Int, @DrawableRes val selectorIds: Int
) : DrawableCreator() {

    override fun createDrawable(): Drawable {
        return StateListDrawable().apply {
            create(selectorIds)?.let {
                addState(intArrayOf(android.R.attr.state_selected), it)
            }
            create(defIds)?.let {
                addState(intArrayOf(0), it)
            }
        }
    }

    private inline fun create(resId: Int): Drawable? =
        VectorDrawableCompat.create(context.resources, resId, context.theme)
}