package dev.chachy.editor.debug.screen

import dev.chachy.editor.api.SaveNotifier
import dev.chachy.editor.ui.ComponentEditor
import gg.essential.elementa.ElementaVersion
import gg.essential.elementa.WindowScreen
import gg.essential.elementa.components.UIBlock
import gg.essential.elementa.dsl.childOf
import gg.essential.elementa.dsl.percentOfWindow

class DebugScreen : WindowScreen(ElementaVersion.V2), SaveNotifier {
    init {
        val testComponent = UIBlock()

        ComponentEditor(
            testComponent,
            window,
            saveNotifiers = listOf(this)
        ) {
            width = 20.percentOfWindow
            height = 30.percentOfWindow
        } childOf window
    }

    override fun notify(relativeX: Float, relativeY: Float, relativeWidth: Float, relativeHeight: Float) {
        println("Changed values:")
        println("relativeX: $relativeX, relativeY: $relativeY, relativeWidth: $relativeWidth, relativeHeight: $relativeHeight")
    }
}