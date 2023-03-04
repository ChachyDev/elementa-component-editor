package dev.chachy.editor.ui

import gg.essential.elementa.components.UIContainer

class HollowUIContainer : UIContainer() {
    override fun isPointInside(x: Float, y: Float): Boolean {
        return children.any { it.isPointInside(x, y) }
    }
}