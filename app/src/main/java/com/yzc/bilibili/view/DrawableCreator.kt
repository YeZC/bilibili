package com.yzc.bilibili.view

import android.graphics.drawable.Drawable
import androidx.annotation.Nullable

abstract class DrawableCreator: IDrawable {

    private var constantState: Drawable.ConstantState? = null

    @Synchronized
    @Nullable
    override fun get(): Drawable? {
        if (constantState == null) {
            var newDrawable = createDrawable()
            return newDrawable?.let {
                constantState = it.constantState
                it
            }
        }
        return constantState!!.newDrawable()
    }

    abstract fun createDrawable(): Drawable?

}