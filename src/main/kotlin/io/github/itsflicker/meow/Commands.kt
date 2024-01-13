package io.github.itsflicker.meow

import io.github.itsflicker.meow.data.Book
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import taboolib.common.platform.command.*
import taboolib.expansion.createHelper
import taboolib.platform.util.sendInfoMessage

@CommandHeader("itsbook", permissionDefault = PermissionDefault.TRUE)
object Commands {

    @CommandBody(optional = true)
    val open = subCommand {
        dynamic("book") {
            suggest { Book.books.keys.toList() }
            execute<Player> { sender, _, arg ->
                Book.books[arg]?.display(sender)
            }
        }
    }

    @CommandBody(optional = true, permission = "itsbook.admin")
    val reload = subCommand {
        execute<CommandSender> { sender, _, _ ->
            ItsBook.reload(sender)
            sender.sendInfoMessage("重载完成")
        }
    }

    @CommandBody
    val main = mainCommand {
        createHelper()
    }

}