package dev.chachy.editor.ui

import dev.chachy.editor.api.SaveNotifier
import gg.essential.elementa.UIComponent
import gg.essential.elementa.UIConstraints
import gg.essential.elementa.components.UIBlock
import gg.essential.elementa.components.UIContainer
import gg.essential.elementa.components.Window
import gg.essential.elementa.constraints.CenterConstraint
import gg.essential.elementa.constraints.ChildBasedMaxSizeConstraint
import gg.essential.elementa.constraints.RelativeConstraint
import gg.essential.elementa.dsl.childOf
import gg.essential.elementa.dsl.constrain
import gg.essential.elementa.dsl.percent
import gg.essential.elementa.dsl.pixels
import gg.essential.elementa.effects.OutlineEffect
import gg.essential.elementa.state.BasicState
import java.awt.Color

class ComponentEditor(
    base: UIComponent,
    private val window: Window,
    initialConstraints: UIConstraints.() -> Unit,
    notifiers: List<SaveNotifier> = listOf(),
    outlineColor: Color = Color.CYAN.darker().darker(),
    outlineWidth: Float = 2f,
    markerOutlineColor: Color = Color.CYAN.darker().darker(),
    markerOutlineColorWidth: Float = 2f
) : UIContainer() {
    private var isMouseDragging = false

    private var dragOffsetX = 0f
    private var dragOffsetY = 0f

    private var isDragging = false
    private var dragStartX = 0f
    private var dragStartY = 0f
    private var dragStartWidth = 0f
    private var dragStartHeight = 0f

    private val outlineColorState = BasicState(outlineColor)
    private val outlineWidthState = BasicState(outlineWidth)

    private val markerOutlineColorState = BasicState(markerOutlineColor)
    private val markerOutlineWidthState = BasicState(markerOutlineColorWidth)

    private val outline = OutlineEffect(outlineColorState, outlineWidthState)

    private val markerOutline = OutlineEffect(markerOutlineColorState, markerOutlineWidthState)

    private val wrapper: HollowUIContainer
    private val component: UIComponent

    init {
        constrain {
            width = 100.percent
            height = 100.percent
        }

        wrapper = HollowUIContainer().constrain {
            x = CenterConstraint()
            y = CenterConstraint()
            width = ChildBasedMaxSizeConstraint()
            height = ChildBasedMaxSizeConstraint()
        } childOf this

        component = base.constrain(initialConstraints)
            .onMouseClick { event ->
                isMouseDragging = true

                dragOffsetX = event.absoluteX
                dragOffsetY = event.absoluteY
            }.onMouseRelease {
                isMouseDragging = false
            }.onMouseDrag { mouseX, mouseY, mouseButton ->
                if (!isMouseDragging || mouseButton != 0) {
                    mouseRelease()
                    return@onMouseDrag
                }

                val absoluteX = mouseX + getLeft()
                val absoluteY = mouseY + getTop()

                val deltaX = absoluteX - dragOffsetX
                val deltaY = absoluteY - dragOffsetY

                dragOffsetX = absoluteX
                dragOffsetY = absoluteY

                val newPosX = getLeft() + deltaX
                val newPosY = getTop() + deltaY


                wrapper.setX(RelativeConstraint(newPosX / window.getWidth()))
                wrapper.setY(RelativeConstraint(newPosY / window.getHeight()))
                notifiers.notifyChange()
            }.onMouseEnter {
                enableEffect(outline)
            }.onMouseLeave {
                removeEffect(outline)
            } childOf wrapper

        UIBlock(Color.WHITE).constrain {
            width = 8.pixels
            height = 8.pixels
            y = (-4).pixels(alignOpposite = true)
            x = 4.pixels(alignOutside = true, alignOpposite = true)
        }.onMouseClick {
            if (it.mouseButton != 0) return@onMouseClick

            isDragging = true
            dragStartX = it.absoluteX
            dragStartY = it.absoluteY
            dragStartWidth = wrapper.getWidth()
            dragStartHeight = wrapper.getHeight()
        }.onMouseDrag { mouseX, mouseY, _ ->
            if (isDragging && (dragStartX != mouseX && dragStartY != mouseY)) {
                val absoluteX = mouseX + getLeft()
                val absoluteY = mouseY + getTop()

                val deltaX = absoluteX - dragStartX
                val deltaY = absoluteY - dragStartY

                val newWidth = dragStartWidth + deltaX
                val newHeight = dragStartHeight + deltaY

                if (newWidth < 0 || newHeight < 0) {
                    mouseRelease()
                    return@onMouseDrag
                }

                component.setWidth(newWidth.pixels)
                component.setHeight(newHeight.pixels)
                notifiers.notifyChange()
            }
        }.onMouseRelease {
            isDragging = false
        }.onMouseEnter {
            enableEffect(markerOutline)
        }.onMouseLeave {
            removeEffect(markerOutline)
        } childOf wrapper
    }

    fun setOutlineWidth(width: Float) {
        outlineWidthState.set(width)
    }

    fun setOutlineColor(color: Color) {
        outlineColorState.set(color)
    }

    private fun List<SaveNotifier>.notifyChange(
        relativeX: Float = wrapper.getLeft() / window.getWidth(),
        relativeY: Float = wrapper.getTop() / window.getHeight(),
        relativeWidth: Float = component.getWidth() / window.getWidth(),
        relativeHeight: Float = component.getHeight() / window.getHeight()
    ) {
        forEach { it.notify(relativeX, relativeY, relativeWidth, relativeHeight) }
    }
}