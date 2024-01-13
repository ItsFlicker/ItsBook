package io.github.itsflicker.meow.data

import org.bukkit.entity.Player
import taboolib.module.chat.ComponentText
import taboolib.module.chat.component
import taboolib.platform.compat.replacePlaceholder

data class Page(
    val id: String,
    val content: List<Line>,
    val dynamic: Boolean
) {

    val cache by lazy {
        buildComponent()
    }

    fun buildComponent(player: Player? = null): ComponentText {
        var text = content.joinToString("\n") { it.text }
        if (dynamic && player != null) {
            text = text.replacePlaceholder(player)
        }
        return text.component().buildColored()
    }

    fun getComponent(player: Player? = null): ComponentText {
        return if (dynamic) {
           buildComponent(player)
        } else {
            cache
        }
    }

    companion object {

        val pages = mutableMapOf<String, Page>()

    }

}