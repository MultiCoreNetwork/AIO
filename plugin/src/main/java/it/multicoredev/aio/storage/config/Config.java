package it.multicoredev.aio.storage.config;

import com.google.gson.annotations.SerializedName;
import it.multicoredev.aio.storage.config.sections.*;
import it.multicoredev.mclib.json.JsonConfig;
import org.bukkit.event.EventPriority;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Copyright © 2021 - 2022 by Lorenzo Magni & Daniele Patella
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
public class Config extends JsonConfig {
    private String help;
    public Map<String, Boolean> modules;
    public Map<String, CommandData> commands;
    @SerializedName("event_priorities")
    public Map<String, String> eventPriorities;

    @SerializedName("storage")
    public StorageSection storageSection;
    @SerializedName("commands_cooldown")
    public CommandsCooldownSection commandsCooldown;
    @SerializedName("help_book")
    public HelpBookSection helpBookSection;
    @SerializedName("nickname")
    public NicknameSection nicknameSection;
    @SerializedName("rtp")
    public RTPSection rtpSection;

    @SerializedName("back_teleport_delay")
    public Long backTeleportDelay;
    @SerializedName("default_home_limit")
    public Integer defaultHomeLimit;
    @SerializedName("home_teleport_delay")
    public Long homeTeleportDelay;
    @SerializedName("warp_teleport_delay")
    public Long warpTeleportDelay;
    @SerializedName("disable_player_death_messages")
    public Boolean disablePlayerDeathMessages;
    @SerializedName("disable_god_on_join")
    public Boolean disableGodOnJoin;
    @SerializedName("clear_players_cache")
    public Long clearPlayersCache;
    @SerializedName("save_players_data")
    public Long savePlayersData;

    public Boolean debug;

    public Config() {
        init();
    }

    @Override
    protected void init() {
        if (help == null) help = "https://github.com/MultiCoreNetwork/AIO/wiki"; //TODO

        if (modules == null) modules = ModuleManager.DEF_MODULES.values().stream().collect(Collectors.toMap(s -> s, s -> true));

        if (commands == null) commands = new HashMap<>();
        if (!commands.containsKey("back")) commands.put("back", CommandData.TRUE);
        if (!commands.containsKey("cleanchat")) commands.put("cleanchat", new CommandData(true, "cchat", "cc"));
        if (!commands.containsKey("day")) commands.put("day", CommandData.TRUE);
        if (!commands.containsKey("delhome")) commands.put("delhome", CommandData.TRUE);
        if (!commands.containsKey("delwarp")) commands.put("delwarp", CommandData.TRUE);
        if (!commands.containsKey("diesnchant")) commands.put("disenchant", new CommandData(true, "denchant"));
        if (!commands.containsKey("economy")) commands.put("economy", new CommandData(true, "eco"));
        if (!commands.containsKey("enchant")) commands.put("enchant", CommandData.TRUE);
        if (!commands.containsKey("feed")) commands.put("feed", CommandData.TRUE);
        if (!commands.containsKey("fly")) commands.put("fly", CommandData.TRUE);
        if (!commands.containsKey("entitylist")) commands.put("entitylist", new CommandData(true, "el"));
        if (!commands.containsKey("gamemode")) commands.put("gamemode", new CommandData(true, "gm"));
        if (!commands.containsKey("god")) commands.put("god", CommandData.TRUE);
        if (!commands.containsKey("heal")) commands.put("heal", CommandData.TRUE);
        if (!commands.containsKey("helpbook")) commands.put("helpbook", new CommandData(true, "hbook", "hb"));
        if (!commands.containsKey("home")) commands.put("home", CommandData.TRUE);
        if (!commands.containsKey("homes")) commands.put("homes", CommandData.TRUE);
        if (!commands.containsKey("kit")) commands.put("kit", CommandData.TRUE);
        if (!commands.containsKey("kits")) commands.put("kits", CommandData.TRUE);
        if (!commands.containsKey("lightning")) commands.put("lightning", new CommandData(true, "zeus", "thor", "zap"));
        if (!commands.containsKey("nickname")) commands.put("nickname", new CommandData(true, "nick"));
        if (!commands.containsKey("night")) commands.put("night", CommandData.TRUE);
        if (!commands.containsKey("playerhome")) commands.put("playerhome", CommandData.TRUE);
        if (!commands.containsKey("rain")) commands.put("rain", CommandData.TRUE);
        if (!commands.containsKey("repair")) commands.put("repair", CommandData.TRUE);
        if (!commands.containsKey("rtp")) commands.put("rtp", new CommandData(true, "randomteleport"));
        if (!commands.containsKey("runlater")) commands.put("runlater", new CommandData(true, "rlater", "rl"));
        if (!commands.containsKey("sethome")) commands.put("sethome", CommandData.TRUE);
        if (!commands.containsKey("setspawn")) commands.put("setspawn", CommandData.TRUE);
        if (!commands.containsKey("setwarp")) commands.put("setwarp", CommandData.TRUE);
        if (!commands.containsKey("spawn")) commands.put("spawn", CommandData.TRUE);
        if (!commands.containsKey("speed")) commands.put("speed", CommandData.TRUE);
        if (!commands.containsKey("sudo")) commands.put("sudo", CommandData.TRUE);
        if (!commands.containsKey("sun")) commands.put("sun", CommandData.TRUE);
        if (!commands.containsKey("thunder")) commands.put("thunder", CommandData.TRUE);
        if (!commands.containsKey("tpall")) commands.put("tpall", CommandData.TRUE);
        if (!commands.containsKey("trash")) commands.put("trash", new CommandData(true, "disposal"));
        if (!commands.containsKey("warp")) commands.put("warp", new CommandData(true, "wp"));
        if (!commands.containsKey("warps")) commands.put("warps", new CommandData(true, "wps"));

        if (eventPriorities == null) eventPriorities = new HashMap<>();
        if (!eventPriorities.containsKey("PlayerPostTeleportEvent")) eventPriorities.put("PlayerPostTeleportEvent", "LOWEST");
        if (!eventPriorities.containsKey("PlayerTeleportCancelledEvent")) eventPriorities.put("PlayerTeleportCancelledEvent", "NORMAL");

        if (!eventPriorities.containsKey("EntityDamageEvent")) eventPriorities.put("EntityDamageEvent", "LOWEST");

        if (!eventPriorities.containsKey("AsyncPlayerChatEvent")) eventPriorities.put("AsyncPlayerChatEvent", "LOWEST");
        if (!eventPriorities.containsKey("PlayerCommandPreprocessEvent")) eventPriorities.put("PlayerCommandPreprocessEvent", "LOWEST");
        if (!eventPriorities.containsKey("AsyncPlayerChatEvent")) eventPriorities.put("AsyncPlayerChatEvent", "LOWEST");
        if (!eventPriorities.containsKey("PlayerJoinEvent")) eventPriorities.put("PlayerJoinEvent", "LOWEST");
        if (!eventPriorities.containsKey("PlayerQuitEvent")) eventPriorities.put("PlayerQuitEvent", "LOWEST");
        if (!eventPriorities.containsKey("PlayerRespawnEvent")) eventPriorities.put("PlayerRespawnEvent", "LOWEST");
        if (!eventPriorities.containsKey("PlayerTeleportEvent")) eventPriorities.put("PlayerTeleportEvent", "LOWEST");

        if (!eventPriorities.containsKey("EntityDamageEvent")) eventPriorities.put("EntityDamageEvent", "LOWEST");
        if (!eventPriorities.containsKey("PlayerDeathEvent")) eventPriorities.put("PlayerDeathEvent", "LOWEST");
        if (!eventPriorities.containsKey("PlayerJoinEvent")) eventPriorities.put("PlayerJoinEvent", "LOWEST");
        if (!eventPriorities.containsKey("PlayerQuitEvent")) eventPriorities.put("PlayerQuitEvent", "LOWEST");
        if (!eventPriorities.containsKey("PlayerRespawnEvent")) eventPriorities.put("PlayerRespawnEvent", "LOWEST");
        if (!eventPriorities.containsKey("PlayerTeleportEvent")) eventPriorities.put("PlayerTeleportEvent", "LOWEST");

        if (storageSection == null) storageSection = new StorageSection();
        if (commandsCooldown == null) commandsCooldown = new CommandsCooldownSection();
        if (helpBookSection == null) helpBookSection = new HelpBookSection();
        if (nicknameSection == null) nicknameSection = new NicknameSection();
        if (rtpSection == null) rtpSection = new RTPSection();

        if (backTeleportDelay == null) backTeleportDelay = -1L;
        if (defaultHomeLimit == null) defaultHomeLimit = 3;
        if (homeTeleportDelay == null) homeTeleportDelay = -1L;
        if (warpTeleportDelay == null) warpTeleportDelay = -1L;
        if (disablePlayerDeathMessages == null) disablePlayerDeathMessages = false;
        if (disableGodOnJoin == null) disableGodOnJoin = false;
        if (clearPlayersCache == null) clearPlayersCache = 12000L;
        if (savePlayersData == null) savePlayersData = 6000L;

        if (debug == null) debug = false;
    }

    public CommandData getCommandData(String command) {
        return commands.get(command);
    }

    public EventPriority getEventPriority(String event) {
        String p = eventPriorities.get(event);
        if (p == null) return EventPriority.NORMAL;
        p = p.toLowerCase(Locale.ROOT);

        if (p.equalsIgnoreCase("lowest")) {
            return EventPriority.LOWEST;
        } else if (p.equals("low")) {
            return EventPriority.LOW;
        } else if (p.equals("normal")) {
            return EventPriority.NORMAL;
        } else if (p.equals("high")) {
            return EventPriority.HIGH;
        } else if (p.equals("highest")) {
            return EventPriority.HIGHEST;
        } else if (p.equals("monitor")) {
            return EventPriority.MONITOR;
        } else {
            return EventPriority.LOWEST;
        }
    }
}
