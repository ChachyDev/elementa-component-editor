package dev.chachy.editor.api

interface SaveNotifier {
    fun notify(relativeX: Float, relativeY: Float, relativeWidth: Float, relativeHeight: Float)
}