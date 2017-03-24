package com.masahirosaito.spigot.homes.commands.subcommands

import com.masahirosaito.spigot.homes.Homes
import com.masahirosaito.spigot.homes.tests.utils.MyVault
import com.masahirosaito.spigot.homes.tests.utils.TestInstanceCreator
import com.masahirosaito.spigot.homes.tests.utils.TestInstanceCreator.homes
import com.masahirosaito.spigot.homes.tests.utils.TestInstanceCreator.nepian
import com.masahirosaito.spigot.homes.tests.utils.lastMsg
import com.masahirosaito.spigot.homes.tests.utils.setOps
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Server
import org.bukkit.World
import org.bukkit.command.PluginCommand
import org.bukkit.entity.Player
import org.bukkit.plugin.PluginDescriptionFile
import org.bukkit.plugin.PluginManager
import org.bukkit.plugin.RegisteredServiceProvider
import org.bukkit.plugin.ServicesManager
import org.bukkit.plugin.java.JavaPluginLoader
import org.hamcrest.CoreMatchers.`is`
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner

@RunWith(PowerMockRunner::class)
@PrepareForTest(Homes::class, JavaPluginLoader::class, PluginDescriptionFile::class,
        Server::class, PluginCommand::class, Player::class, Location::class, World::class, Bukkit::class,
        PluginManager::class, ServicesManager::class, MyVault::class, RegisteredServiceProvider::class)
class SetCommandTest {
    lateinit var setCommand: SetCommand

    @Before
    fun setUp() {
        assertTrue(TestInstanceCreator.setUp())
        setCommand = SetCommand(homes)
    }

    @After
    fun tearDown() {
        assertTrue(TestInstanceCreator.tearDown())
    }

    @Test
    fun デフォルトホームの設定ができる() {
        homes.homeManager.findPlayerHome(nepian).removeDefaultHome(nepian)

        "[Homes] Successfully set as default home".apply {
            setCommand.execute(nepian, listOf())
            assertThat(homes.homeManager.findDefaultHome(nepian).location(), `is`(nepian.location))
            assertThat(nepian.lastMsg(), `is`(this))
        }
    }

    @Test
    fun デフォルトホームを更新できる() {

        "[Homes] Successfully set as default home".apply {
            setCommand.execute(nepian, listOf())
            assertThat(homes.homeManager.findDefaultHome(nepian).location(), `is`(nepian.location))
            assertThat(nepian.lastMsg(), `is`(this))
        }
    }
}
