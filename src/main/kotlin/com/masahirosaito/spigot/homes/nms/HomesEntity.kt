package com.masahirosaito.spigot.homes.nms

import com.masahirosaito.spigot.homes.Configs
import com.masahirosaito.spigot.homes.NMSManager
import com.masahirosaito.spigot.homes.homedata.HomeData
import com.masahirosaito.spigot.homes.toData
import org.bukkit.Chunk
import org.bukkit.Location
import org.bukkit.OfflinePlayer

data class HomesEntity(
        val offlinePlayer: OfflinePlayer,
        val location: Location,
        var homeName: String? = null,
        var isPrivate: Boolean = false
) {
    private var entities: List<NMSEntityArmorStand> = emptyList()

    fun isOwner(offlinePlayer: OfflinePlayer): Boolean {
        return this.offlinePlayer.uniqueId == offlinePlayer.uniqueId
    }

    fun toHomeData(): HomeData {
        return HomeData(homeName, location.toData(), isPrivate)
    }

    fun despawnEntities() {
        entities.forEach { it.dead() }
    }

    fun reSpawnEntities() {
        despawnEntities()
        spawnEntities()
    }

    fun spawnEntities() {
        if (Configs.onHomeDisplay) {
            entities = NMSManager.spawn(this)
        }
    }

    fun inChunk(chunk: Chunk): Boolean {
        return chunk.x == location.chunk.x && chunk.z == location.chunk.z
    }
}
