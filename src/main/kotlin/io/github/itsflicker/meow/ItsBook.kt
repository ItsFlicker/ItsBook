package io.github.itsflicker.meow

import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import taboolib.common.platform.Plugin
import taboolib.module.configuration.Config
import taboolib.module.configuration.Configuration

object ItsBook : Plugin() {

    @Config
    lateinit var conf: Configuration
        private set

    override fun onEnable() {
        reload(Bukkit.getConsoleSender())
    }

    fun reload(sender: CommandSender) {
        conf.reload()
        Loader.load(sender)
    }

}