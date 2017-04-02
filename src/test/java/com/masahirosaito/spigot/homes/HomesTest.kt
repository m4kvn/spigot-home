package com.masahirosaito.spigot.homes

import com.masahirosaito.spigot.homes.commands.maincommands.homecommands.HomeCommand
import com.masahirosaito.spigot.homes.datas.FeeData
import com.masahirosaito.spigot.homes.testutils.TestInstanceCreator
import com.masahirosaito.spigot.homes.testutils.TestInstanceCreator.feeFile
import com.masahirosaito.spigot.homes.testutils.TestInstanceCreator.homes
import com.masahirosaito.spigot.homes.testutils.TestInstanceCreator.pluginFolder
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
import org.hamcrest.CoreMatchers.notNullValue
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner
import java.io.File


@RunWith(PowerMockRunner::class)
@PrepareForTest(Homes::class, JavaPluginLoader::class, PluginDescriptionFile::class,
        Server::class, PluginCommand::class, Player::class, Location::class, World::class, Bukkit::class,
        PluginManager::class, ServicesManager::class, RegisteredServiceProvider::class)
class HomesTest {

    @Before
    fun setUp() {
        assertTrue(TestInstanceCreator.setUp())
    }

    @After
    fun tearDown() {
        assertTrue(TestInstanceCreator.tearDown())
    }

    @Test
    fun プラグインフォルダが生成されている() {
        assertTrue(pluginFolder.exists())
    }

    @Test
    fun 設定ファイルが生成されている() {
        assertTrue(File(pluginFolder, "configs.json").exists())
    }

    @Test
    fun データファイルが生成されている() {
        assertTrue(File(pluginFolder, "playerhomes.json").exists())
    }

    @Test
    fun 料金設定ファイルが生成されている() {
        assertTrue(File(pluginFolder, "fee.json").exists())
    }

    @Test
    fun ホームコマンドが登録されている() {
        assertTrue(homes.getCommand("home").executor is HomeCommand)
    }

    @Test
    fun 料金設定ファイルが読み込まれている() {
        assertThat(homes.fee, `is`(FeeData.load(feeFile)))
    }

    @Test
    fun エコノミーが読み込まれている() {
        assertThat(homes.econ, `is`(notNullValue()))
    }
}
