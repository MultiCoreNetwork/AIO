package it.multicoredev.aio.storage.config;

import com.google.common.base.Preconditions;
import it.multicoredev.aio.api.models.CommandData;
import it.multicoredev.mclib.json.JsonConfig;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import static it.multicoredev.aio.api.models.CommandData.UsagesBuilder;

/**
 * Copyright © 2022 by Lorenzo Magni
 * This file is part of AIO.
 * AIO is under "The 3-Clause BSD License", you can find a copy <a href="https://opensource.org/licenses/BSD-3-Clause">here</a>.
 * <p>
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following
 * disclaimer in the documentation and/or other materials provided with the distribution.
 * 3. Neither the name of the copyright holder nor the names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 * <p>
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING,
 * BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY,
 * OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */
public class Commands extends JsonConfig {
    private Map<String, CommandData> commands;

    public Commands() {
        init();
    }

    public CommandData getCommand(@NotNull String command) {
        Preconditions.checkNotNull(command);

        return commands.get(command);
    }

    public boolean isEnabled(@NotNull String command) {
        Preconditions.checkNotNull(command);
        return commands.containsKey(command) && commands.get(command).isEnabled();
    }

    @Override
    protected void init() {
        if (commands == null) commands = new HashMap<>();

        if (!commands.containsKey("aio")) commands.put("aio", new CommandData(
                true,
                "Main command of the plugin",
                "/aio"
        ));
        if (!commands.containsKey("back")) commands.put("back", new CommandData(
                true,
                "Teleport back to previous location",
                "/back"
        ));
        if (!commands.containsKey("cleanchat")) commands.put("cleanchat", new CommandData(
                true,
                "Clean the chat for everyone except for who has the permission",
                "/cleanchat",
                "cchat", "cc"
        ));
        if (!commands.containsKey("day")) commands.put("day", new CommandData(
                true,
                "Set the time to day",
                "/day [world]"
        ));
        if (!commands.containsKey("delhome")) commands.put("delhome", new CommandData(
                true,
                "Delete a home",
                "/delhome <home>"
        ));
        if (!commands.containsKey("delwarp")) commands.put("delwarp", new CommandData(
                true,
                "Delete a warp",
                "/delwarp <warp>",
                "delwp"
        ));
        if (!commands.containsKey("disenchant")) commands.put("disenchant", new CommandData(
                true,
                "Remove one or more enchantments from an item",
                new UsagesBuilder().add("default", "/disenchant [enchantment]", "/disenchant [player] [enchantment]").build(),
                "denchant"
        ));
        if (!commands.containsKey("economy")) commands.put("economy", new CommandData(
                true,
                "Manage players' money",
                new UsagesBuilder().add("give", "/eco give <player> <amount>")
                        .add("take", "/eco take <player> <amount>")
                        .add("set", "/eco set <player> <amount>")
                        .add("reset", "/eco reset <player>")
                        .build(),
                "eco"
        ));
        if (!commands.containsKey("enchant")) commands.put("enchant", new CommandData(
                true,
                "Add one or more enchantments to an item",
                new UsagesBuilder().add("default", "/enchant <enchantment> [level]", "/enchant [player] <enchantment> [level]").build()
        ));
        if (!commands.containsKey("entitylist")) commands.put("entitylist", new CommandData(
                true,
                "Get a list of entities in the world",
                "/entitylist [world]",
                "elist", "el"
        ));
        if (!commands.containsKey("feed")) commands.put("feed", new CommandData(
                true,
                "Feed a player",
                "/feed [player]"
        ));
        if (!commands.containsKey("fly")) commands.put("fly", new CommandData(
                true,
                "Toggle fly for a player",
                "/fly <on|off|toggle> [player]"
        ));
        if (!commands.containsKey("gamemode")) commands.put("gamemode", new CommandData(
                true,
                "Change gamemode of a player",
                "/gamemode <mode> [player]",
                "gm"
        ));
        if (!commands.containsKey("god")) commands.put("god", new CommandData(
                true,
                "Toggle god mode for a player",
                "/god [player]"
        ));
        if (!commands.containsKey("hat")) commands.put("hat", new CommandData(
                true,
                "Put an item as hat",
                "/hat [player]"
        ));
        if (!commands.containsKey("heal")) commands.put("heal", new CommandData(
                true,
                "Heal a player",
                "/heal [player]"
        ));
        if (!commands.containsKey("helpbook")) commands.put("helpbook", new CommandData(
                true,
                "Get a book with useful informations",
                "/helpbook [book] [player]",
                "hbook", "hb"
        ));
        if (!commands.containsKey("home")) commands.put("home", new CommandData(
                true,
                "Teleport yourself to a home",
                "/home <home>",
                "waypoint"
        ));
        if (!commands.containsKey("homes")) commands.put("homes", new CommandData(
                true,
                "Get a list of player's homes",
                "/homes [player] [page]",
                "waypoints"
        ));
        if (!commands.containsKey("kit")) commands.put("kit", new CommandData(
                true,
                "Give a kit to a player",
                "/kit <kit> [player]"
        ));
        if (!commands.containsKey("kits")) commands.put("kits", new CommandData(
                true,
                "Get a list of available kits",
                "/kits [page]"
        ));
        if (!commands.containsKey("lightning")) commands.put("lightning", new CommandData(
                true,
                "Summon a lightning to the pointed location or a player",
                "/lightning [player]",
                "zeus", "thor", "zap"
        ));
        if (!commands.containsKey("nickname")) commands.put("nickname", new CommandData(
                true,
                "Change a player's nickname",
                "/nickname [player] <nickname>",
                "nick"
        ));
        if (!commands.containsKey("night")) commands.put("night", new CommandData(
                true,
                "Set the time to night",
                "/night [world]"
        ));
        if (!commands.containsKey("playerhome")) commands.put("playerhome", new CommandData(
                true,
                "Teleport yourself to a player's home",
                "/playerhome <player> <home>",
                "phome", "pwaypoint"
        ));
        if (!commands.containsKey("rain")) commands.put("rain", new CommandData(
                true,
                "Set the weather to rain",
                "/rain [world]"
        ));
        if (!commands.containsKey("repair")) commands.put("repair", new CommandData(
                true,
                "Repair a player's item",
                "/repair [player]"
        ));
        if (!commands.containsKey("rtp")) commands.put("rtp", new CommandData(
                true,
                "Teleport a player to a random location",
                "/rtp [player]",
                "randomteleport", "randomtp"
        ));
        if (!commands.containsKey("runlater")) commands.put("runlater", new CommandData(
                true,
                "Run a command after some time",
                "/runlater <time> <command>",
                "rlater"
        ));
        if (!commands.containsKey("sethome")) commands.put("sethome", new CommandData(
                true,
                "Set a home at your current location",
                "/sethome <home>",
                "setwaypoint"
        ));
        if (!commands.containsKey("setspawn")) commands.put("setspawn", new CommandData(
                true,
                "Set the spawn point of the server",
                "/setspawn"
        ));
        if (!commands.containsKey("setwarp")) commands.put("setwarp", new CommandData(
                true,
                "Set a warp at your current location",
                "/setwarp <warp>",
                "setwp"
        ));
        if (!commands.containsKey("spawn")) commands.put("spawn", new CommandData(
                true,
                "Teleport yourself to the spawn location",
                "/spawn [player]"
        ));
        if (!commands.containsKey("speed")) commands.put("speed", new CommandData(
                true,
                "Set your flying/walking speed",
                "/speed [player] [flying|walking] <speed>"
        ));
        if (!commands.containsKey("sudo")) commands.put("sudo", new CommandData(
                true,
                "Run a command or send a message as another player",
                "/sudo <player|console> <command|message>"
        ));
        if (!commands.containsKey("sun")) commands.put("sun", new CommandData(
                true,
                "Set the time to day",
                "/sun [world]"
        ));
        if (!commands.containsKey("thunder")) commands.put("thunder", new CommandData(
                true,
                "Set the weather to thunderstorm",
                "/thunder [world]"
        ));
        if (!commands.containsKey("tpa")) commands.put("tpa", new CommandData(
                true,
                "Request to a player to be teleported to him",
                "/tpa [player]"
        ));
        if (!commands.containsKey("tpall")) commands.put("tpall", new CommandData(
                true,
                "Teleport all players to your location",
                "/tpall"
        ));
        if (!commands.containsKey("trash")) commands.put("trash", new CommandData(
                true,
                "Open a trash can",
                "/trash",
                "disposal"
        ));
        if (!commands.containsKey("warp")) commands.put("warp", new CommandData(
                true,
                "Teleport yourself to a warp",
                "/warp <warp>",
                "wp"
        ));
        if (!commands.containsKey("warps")) commands.put("warps", new CommandData(
                true,
                "Get a list of warps",
                "/warps [page]",
                "wps"
        ));
    }
}
