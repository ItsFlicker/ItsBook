package io.github.itsflicker.meow

import io.github.itsflicker.meow.data.Book
import io.github.itsflicker.meow.data.Line
import io.github.itsflicker.meow.data.Page
import org.bukkit.command.CommandSender
import taboolib.common.platform.function.getDataFolder
import taboolib.common.platform.function.releaseResourceFile
import taboolib.common.util.unsafeLazy
import taboolib.module.chat.colored
import taboolib.module.configuration.Configuration
import taboolib.platform.util.sendInfoMessage
import java.io.File
import kotlin.system.measureTimeMillis

object Loader {

    private val pageFolder by unsafeLazy {
        val folder = File(getDataFolder(), "pages")
        if (!folder.exists()) {
            releaseResourceFile("pages/example.yml", replace = true)
        }
        folder
    }

    private val bookFolder by unsafeLazy {
        val folder = File(getDataFolder(), "books")
        if (!folder.exists()) {
            releaseResourceFile("books/example.yml", replace = true)
        }
        folder
    }

    fun load(sender: CommandSender) {
        val pageTime = measureTimeMillis { loadPages() }
        sender.sendInfoMessage("§3成功载入 §b${Page.pages.size} §3个书页... &8[$pageTime Ms]")
        val bookTime = measureTimeMillis { loadBooks() }
        sender.sendInfoMessage("§3成功载入 §b${Book.books.size} §3个书本... &8[$bookTime Ms]")
    }

    fun loadPages() {
        Page.pages.clear()
        filterFiles(pageFolder).forEach { file ->
            val conf = Configuration.loadFromFile(file)
            val id = conf.getString("id") ?: file.nameWithoutExtension
            val content = conf.getString("content")
                ?.split("\n")
                ?.map { Line(it) }
                ?: error("No content in page $id")
            val dynamic = conf.getBoolean("dynamic", false)
            Page.pages[id] = Page(id, content, dynamic)
        }
    }

    fun loadBooks() {
        Book.books.clear()
        filterFiles(bookFolder).forEach { file ->
            val conf = Configuration.loadFromFile(file)
            val id = conf.getString("id") ?: file.nameWithoutExtension
            val title = conf.getString("title") ?: ItsBook.conf.getString("default-title")!!.colored()
            val author = conf.getString("author") ?: ItsBook.conf.getString("default-author")!!
            val pages = conf.getStringList("pages").map { Page.pages[it] ?: error("No page matches $it") }
            val permission = conf.getString("permission")
            Book.books[id] = Book(id, title, author, pages, permission)
        }
    }

    private fun filterFiles(file: File): List<File> {
        return mutableListOf<File>().apply {
            if (file.isDirectory) {
                file.listFiles()?.forEach {
                    addAll(filterFiles(it))
                }
            } else if (file.extension.equals("yml", true)) {
                add(file)
            }
        }
    }

}