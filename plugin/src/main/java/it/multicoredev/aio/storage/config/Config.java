package it.multicoredev.aio.storage.config;

import com.google.gson.annotations.SerializedName;
import it.multicoredev.aio.storage.config.sections.*;
import it.multicoredev.mbcore.spigot.Chat;
import it.multicoredev.mclib.json.JsonConfig;
import org.bukkit.event.EventPriority;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Copyright &copy; 2021 - 2022 by Lorenzo Magni &amp; Daniele Patella
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
    @SerializedName("storage")
    public StorageSection storageSection;
    @SerializedName("teleports")
    public TeleportSection teleportSection;
    @SerializedName("afk")
    public AfkSection afkSection;
    @SerializedName("command_cooldown")
    public CommandCooldownSection commandCooldown;

    @SerializedName("nickname_min_length")
    private Integer nicknameMinLength;
    @SerializedName("nickname_max_length")
    private Integer nicknameMaxLength;
    @SerializedName("blacklisted_nicknames")
    private List<String> blacklistedNicknames;

    @SerializedName("default_book")
    public String defBook;
    @SerializedName("first_join_books")
    public List<String> firstJoinBooks;

    @SerializedName("disable_player_death_messages")
    public Boolean disablePlayerDeathMessages;
    @SerializedName("disable_god_on_join")
    public Boolean disableGodOnJoin;
    @SerializedName("suicide_broadcast")
    public Boolean suicideBroadcast;
    @SerializedName("clear_players_cache")
    public Integer clearPlayersCache;
    @SerializedName("save_players_data")
    public Integer savePlayersData;

    @SerializedName("event_priorities")
    private Map<String, String> eventPriorities;

    public Boolean debug;

    @Override
    public void init() {
        if (storageSection == null) {
            storageSection = new StorageSection();
            storageSection.init();
        }
        if (teleportSection == null) {
            teleportSection = new TeleportSection();
            teleportSection.init();
        }
        if (afkSection == null) {
            afkSection = new AfkSection();
            afkSection.init();
        }
        if (commandCooldown == null) {
            commandCooldown = new CommandCooldownSection();
            commandCooldown.init();
        }

        if (nicknameMinLength == null) nicknameMinLength = 4;
        if (nicknameMaxLength == null) nicknameMaxLength = 32;
        if (blacklistedNicknames == null) blacklistedNicknames = new ArrayList<>();

        if (defBook == null) defBook = "rules";
        if (firstJoinBooks == null) firstJoinBooks = new ArrayList<>();





        if (disablePlayerDeathMessages == null) disablePlayerDeathMessages = false;
        if (disableGodOnJoin == null) disableGodOnJoin = false;
        if (suicideBroadcast == null) suicideBroadcast = true;
        if (clearPlayersCache == null) clearPlayersCache = 600;
        if (savePlayersData == null) savePlayersData = 300;

        if (eventPriorities == null) eventPriorities = new HashMap<>();
        if (!eventPriorities.containsKey("PlayerPostTeleportEvent")) eventPriorities.put("PlayerPostTeleportEvent", "LOWEST");
        if (!eventPriorities.containsKey("PlayerTeleportCancelledEvent")) eventPriorities.put("PlayerTeleportCancelledEvent", "NORMAL");
        if (!eventPriorities.containsKey("PostCommandEvent")) eventPriorities.put("PostCommandEvent", "HIGH");
        if (!eventPriorities.containsKey("AfkToggleEvent")) eventPriorities.put("AfkToggleEvent", "LOWEST");

        if (!eventPriorities.containsKey("EntityDamageEvent")) eventPriorities.put("EntityDamageEvent", "LOWEST");

        if (!eventPriorities.containsKey("AsyncPlayerChatEvent")) eventPriorities.put("AsyncPlayerChatEvent", "LOWEST");
        if (!eventPriorities.containsKey("PlayerCommandPreprocessEvent")) eventPriorities.put("PlayerCommandPreprocessEvent", "LOWEST");
        if (!eventPriorities.containsKey("AsyncPlayerChatEvent")) eventPriorities.put("AsyncPlayerChatEvent", "LOWEST");
        if (!eventPriorities.containsKey("PlayerInteractEvent")) eventPriorities.put("PlayerInteractEvent", "LOWEST");
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

        if (debug == null) debug = false;
    }

    public int getNicknameMinLength() {
        return Math.max(1, nicknameMinLength);
    }

    public int getNicknameMaxLength() {
        return Math.max(getNicknameMinLength(), nicknameMaxLength);
    }

    public boolean isNicknameBlacklisted(String nickname) {
        return blacklistedNicknames.stream().anyMatch(Chat.getDiscolored(nickname)::equalsIgnoreCase);
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
