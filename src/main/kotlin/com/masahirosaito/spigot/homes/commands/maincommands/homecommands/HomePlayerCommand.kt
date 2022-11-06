package com.masahirosaito.spigot.homes.commands.maincommands.homecommands

import com.masahirosaito.spigot.homes.Configs.onFriendHome
import com.masahirosaito.spigot.homes.DelayTeleporter
import com.masahirosaito.spigot.homes.Homes.Companion.homes
import com.masahirosaito.spigot.homes.Permission.home_command
import com.masahirosaito.spigot.homes.Permission.home_command_player
import com.masahirosaito.spigot.homes.commands.PlayerSubCommand
import com.masahirosaito.spigot.homes.commands.subcommands.player.PlayerCommand
import com.masahirosaito.spigot.homes.findOfflinePlayer
import org.bukkit.entity.Player

class HomePlayerCommand(
    private val homeCommand: HomeCommand,
) : PlayerSubCommand(homeCommand), PlayerCommand {
    override val permissions: List<String> = listOf(
        home_command,
        home_command_player
    )

    override fun fee(): Double = homes.fee.HOME_PLAYER

    override fun configs(): List<Boolean> = listOf(onFriendHome)

    override fun isValidArgs(args: List<String>): Boolean = args.size == 2 && args[0] == "-p"

    override fun execute(player: Player, args: List<String>) {
        DelayTeleporter.run(player, homeCommand.getTeleportLocation(findOfflinePlayer(args[1])), this)
    }
}
