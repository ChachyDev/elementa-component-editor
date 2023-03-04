package dev.chachy.editor.debug.screen

import dev.chachy.editor.ui.ComponentEditor
import gg.essential.elementa.ElementaVersion
import gg.essential.elementa.WindowScreen
import gg.essential.elementa.components.UIBlock
import gg.essential.elementa.dsl.childOf
import gg.essential.elementa.dsl.percentOfWindow

class DebugScreen : WindowScreen(ElementaVersion.V2) {
    init {
        val testComponent = UIBlock()

        ComponentEditor(testComponent, window, {
            width = 20.percentOfWindow
            height = 30.percentOfWindow
        }) childOf window
    }
}