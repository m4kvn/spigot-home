package com.masahirosaito.spigot.homes.commands.subcommands.player.invitecommands

import com.masahirosaito.spigot.homes.Configs.onInvite
import com.masahirosaito.spigot.homes.Homes.Companion.homes
import com.masahirosaito.spigot.homes.commands.BaseCommand
import com.masahirosaito.spigot.homes.commands.CommandUsage
import com.masahirosaito.spigot.homes.commands.subcommands.player.PlayerCommand
import com.masahirosaito.spigot.homes.exceptions.AlreadyHasInvitationException
import com.masahirosaito.spigot.homes.exceptions.NoReceivedInvitationException
import com.masahirosaito.spigot.homes.findOnlinePlayer
import com.masahirosaito.spigot.homes.nameOrUnknown
import com.masahirosaito.spigot.homes.nms.HomesEntity
import com.masahirosaito.spigot.homes.strings.commands.InviteCommandStrings
import com.masahirosaito.spigot.homes.strings.commands.InviteCommandStrings.USAGE_INVITE
import com.masahirosaito.spigot.homes.strings.commands.InviteCommandStrings.USAGE_INVITE_PLAYER
import com.masahirosaito.spigot.homes.strings.commands.InviteCommandStrings.USAGE_INVITE_PLAYER_NAME
import com.masahirosaito.spigot.homes.strings.commands.InviteCommandStrings.createAcceptInvitationFrom
import com.masahirosaito.spigot.homes.strings.commands.InviteCommandStrings.createAcceptInvitationTo
import com.masahirosaito.spigot.homes.strings.commands.InviteCommandStrings.createCancelInvitationFrom
import com.masahirosaito.spigot.homes.strings.commands.InviteCommandStrings.createCancelInvitationTo
import org.bukkit.entity.Player
import org.bukkit.metadata.FixedMetadataValue
import kotlin.concurrent.thread

class PlayerInviteCommand : PlayerCommand {
    override var payNow: Boolean = true
    override val name: String = "invite"
    override val description: String = InviteCommandStrings.DESCRIPTION
    override val permissions: List<String> = listOf()
    override val usage: CommandUsage = CommandUsage(this, listOf(
        "/home invite" to USAGE_INVITE,
        "/home invite <player_name>" to USAGE_INVITE_PLAYER,
        "/home invite <player_name> <home_name>" to USAGE_INVITE_PLAYER_NAME
    ))
    override val commands: List<BaseCommand> = listOf(
            PlayerInvitePlayerCommand(this),
            PlayerInvitePlayerNameCommand(this)
    )

    override fun fee(): Double = homes.fee.INVITE

    override fun configs(): List<Boolean> = listOf(onInvite)

    override fun isValidArgs(args: List<String>): Boolean = args.isEmpty()

    override fun execute(player: Player, args: List<String>) {
        if (!player.hasMetadata(INVITE_META)) {
            throw NoReceivedInvitationException()
        } else {
            val th = player.getMetadata(INVITE_META).first().value() as Thread
            if (th.isAlive) {
                th.interrupt()
                th.join()
            }
        }
    }

    fun inviteHome(homesEntity: HomesEntity, player: Player, playerName: String, message: String) {
        val op = findOnlinePlayer(playerName).apply {
            if (hasMetadata(INVITE_META)) {
                throw AlreadyHasInvitationException(this)
            }
            send(this, message)
        }
        val th = thread(name = "$INVITE_META.${player.name}.$playerName") {
            try {
                Thread.sleep(30000)
                val target = findOnlinePlayer(playerName)
                if (target.hasMetadata(INVITE_META)) {
                    target.removeMetadata(INVITE_META, homes)
                    send(target, createCancelInvitationFrom(player.name))
                    send(player, createCancelInvitationTo(target.name))
                }
            } catch (e: InterruptedException) {
                try {
                    val target = findOnlinePlayer(playerName)
                    target.teleport(homesEntity.location)
                    target.removeMetadata(INVITE_META, homes)
                    send(target, createAcceptInvitationFrom(homesEntity.offlinePlayer.nameOrUnknown))
                    try {
                        val owner = findOnlinePlayer(homesEntity.offlinePlayer.uniqueId)
                        send(owner, createAcceptInvitationTo(target.name))
                    } catch (_: Exception) {
                    }
                } catch (_: Exception) {
                }
            } catch (e: Exception) {
                e.message?.let { send(player, it) }
            }
        }
        op.setMetadata(INVITE_META, FixedMetadataValue(homes, th))
    }

    companion object {
        private const val INVITE_META = "homes.invite"
    }
}
