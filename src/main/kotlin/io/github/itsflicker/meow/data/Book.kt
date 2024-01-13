package io.github.itsflicker.meow.data

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.BookMeta
import taboolib.library.reflex.Reflex.Companion.getProperty
import taboolib.library.reflex.Reflex.Companion.setProperty
import taboolib.library.xseries.XMaterial
import taboolib.platform.util.ItemBuilder
import taboolib.platform.util.modifyMeta
import taboolib.platform.util.sendInfoMessage

class Book(
    val id: String,
    val title: String,
    val author: String,
    val pages: List<Page>,
    val permission: String?,
    val dynamic: Boolean = pages.any { it.dynamic },
) : ItemBuilder(XMaterial.WRITTEN_BOOK) {

    init {
        name = title
    }

    val cache by lazy {
        buildBook()
    }

    fun buildBook(player: Player? = null): ItemStack {
        return build().modifyMeta<BookMeta> {
            title = this@Book.title
            author = this@Book.author
            var pages = getProperty<MutableList<Any>>("pages")
            if (pages == null) {
                pages = ArrayList()
                setProperty("pages", pages)
            }
            this@Book.pages.forEach { page ->
                pages += page.getComponent(player).toRawMessage()
            }
        }
    }

    fun display(player: Player) {
        if (permission != null && !player.hasPermission(permission)) {
            player.sendInfoMessage("你没有打开这本书的权限!")
        }
        if (dynamic) {
            player.openBook(buildBook(player))
        } else {
            player.openBook(cache)
        }
    }

    companion object {

        val books = mutableMapOf<String, Book>()

    }

}