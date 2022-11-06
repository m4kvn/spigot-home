package com.masahirosaito.spigot.homes.commands

import com.masahirosaito.spigot.homes.Messenger
import com.masahirosaito.spigot.homes.commands.subcommands.console.ConsoleCommand
import com.masahirosaito.spigot.homes.commands.subcommands.player.PlayerCommand
import com.masahirosaito.spigot.homes.exceptions.ArgumentIncorrectException
import com.masahirosaito.spigot.homes.exceptions.InvalidCommandSenderException
import com.masahirosaito.spigot.homes.exceptions.NotAllowByConfigException
import org.bukkit.command.CommandSender
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.entity.Player

interface BaseCommand {
    val name: String
    val description: String
    val usage: CommandUsage
    val commands: List<BaseCommand>

    fun configs(): List<Boolean>

    fun isValidArgs(args: List<String>): Boolean

    fun send(sender: CommandSender, message: String) {
        if (message.isNotBlank()) Messenger.send(sender, message)
    }

    fun send(sender: CommandSender, obj: Any?) {
        send(sender, obj.toString())
    }

    fun checkConfig() {
        if (configs().contains(false)) throw NotAllowByConfigException()
    }

    fun checkArgs(args: List<String>) {
        if (!isValidArgs(args)) throw ArgumentIncorrectException(usage)
    }

    fun executeCommand(sender: CommandSender, args: List<String>) {
        val cmd = findCommand(args)
        when {
            cmd is PlayerCommand && sender is Player -> cmd.onCommand(sender, args)
            cmd is ConsoleCommand && sender is ConsoleCommandSender -> cmd.onCommand(sender, args)
            else -> throw InvalidCommandSenderException()
        }
    }

    private fun findCommand(args: List<String>): BaseCommand {
        return commands.find { it.isValidArgs(args) } ?: this
    }
}
