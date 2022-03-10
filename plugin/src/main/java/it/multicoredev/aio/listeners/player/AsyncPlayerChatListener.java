package it.multicoredev.aio.listeners.player;

import it.multicoredev.aio.AIO;
import it.multicoredev.aio.api.User;
import it.multicoredev.aio.listeners.PluginListenerExecutor;
import it.multicoredev.aio.storage.config.modules.ChatModule;
import it.multicoredev.aio.storage.config.modules.PingModule;
import it.multicoredev.aio.utils.Utils;
import it.multicoredev.mbcore.spigot.Chat;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import static it.multicoredev.aio.AIO.LUCKPERMS;
import static it.multicoredev.aio.AIO.VAULT;

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
public class AsyncPlayerChatListener extends PluginListenerExecutor<AsyncPlayerChatEvent> {
    private final ChatModule chatModule;
    private final PingModule pingModule;

    public AsyncPlayerChatListener(Class<AsyncPlayerChatEvent> eventClass, AIO aio) {
        super(eventClass, aio);
        chatModule = aio.getModuleManager().getModule(AIO.CHAT_MODULE);
        pingModule = aio.getModuleManager().getModule(AIO.PING_MODULE);
    }

    @Override
    public void onEvent(@NotNull AsyncPlayerChatEvent event) {

        Bukkit.getScheduler().callSyncMethod(aio, () -> {
            // AFK
            User user = storage.getUser(event.getPlayer());
            if (user != null && config.afkSection.afkRemoveOnMessage) {
                user.setAfk(false);
            }
            return null;
        });

        if (config.modules.get("chat")) chatModule(event);

        if (config.modules.get("ping")) {
            if (!pingModule(event)) return;
        }
    }

    private void chatModule(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();
        String formattedMessage = getFormattedMessage(player, message);
        event.setFormat(formattedMessage);

        if (chatModule.chatRadius > 0) {
            event.setCancelled(true);

            Location senderLocation = player.getLocation();
            for (Player receiver : Bukkit.getOnlinePlayers()) {
                if (receiver.hasPermission("aio.chat.ignore-radius")) {
                    Chat.send(formattedMessage, receiver);
                    continue;
                }

                Location receiverLocation = receiver.getLocation();
                if (!Objects.equals(senderLocation.getWorld(), receiverLocation.getWorld())) continue;
                if (senderLocation.distance(receiverLocation) > chatModule.chatRadius) continue;

                Chat.send(formattedMessage, receiver);
            }
        }
    }

    private boolean pingModule(AsyncPlayerChatEvent event) {
        String msg = event.getMessage().toLowerCase(Locale.ROOT);
        Player sender = event.getPlayer();

        Bukkit.getScheduler().runTaskAsynchronously(aio, () -> {
            Map<Player, Integer> pendingPings = new HashMap<>();

            if (pingModule.pingPlayers) {
                if (!sender.hasPermission("aio.chat.ping.players")) return;

                Bukkit.getOnlinePlayers().forEach(player -> {
                    if (msg.contains(player.getName().toLowerCase(Locale.ROOT)) ||
                            msg.contains(Chat.getDiscolored(player.getDisplayName().toLowerCase(Locale.ROOT)))) {
                        pendingPings.put(player, 1);
                    }
                });
            }

            if (pingModule.pingGroups) {
                if (!sender.hasPermission("aio.chat.ping.groups")) return;

                for (String group : aio.getPermissionHandler().getGroups()) {
                    if (msg.contains(group.toLowerCase(Locale.ROOT))) {
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            if (aio.getPermissionHandler().isInGroup(player, group)) {
                                pendingPings.put(player, 2);
                            }
                        }
                    }
                }
            }

            if (pingModule.pingStaff) {
                if (!sender.hasPermission("aio.chat.ping.staff")) return;

                for (String keyword : pingModule.staffKeywords) {
                    if (msg.contains(keyword.toLowerCase(Locale.ROOT))) {
                        Bukkit.getOnlinePlayers().forEach(player -> {
                            if (player.hasPermission("aio.staff")) return;

                            pendingPings.put(player, 3);
                        });

                        break;
                    }
                }
            }

            for (Map.Entry<Player, Integer> entry : pendingPings.entrySet()) {
                Player player = entry.getKey();
                int amount = entry.getValue();

                for (int i = 0; i < amount; i++) {
                    player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
                    if (i < amount - 1) Utils.sleep(200);
                }
            }
        });

        return true;
    }

    private String getFormattedMessage(Player player, String message) {
        String format = null;
        String group = "";

        if (VAULT || LUCKPERMS) {
            List<String> groups = aio.getPermissionHandler().getPlayerGroups(player);

            if (groups == null || groups.isEmpty()) {
                format = chatModule.chatFormat;
            } else {
                if (chatModule.groupFormats == null || chatModule.groupFormats.isEmpty()) {
                    format = chatModule.chatFormat;
                } else {
                    for (String f : chatModule.groupFormats.keySet()) {
                        if (groups.contains(f.toLowerCase(Locale.ROOT))) {
                            group = f;
                            format = chatModule.groupFormats.get(f);
                            break;
                        }
                    }
                }
            }
        }

        if (format == null) format = chatModule.chatFormat;

        format = aio.getPlaceholdersUtils().replacePlaceholders(
                format,
                new String[]{"{DISPLAYNAME}", "{NAME}", "{GROUP}"},
                new Object[]{player.getName(), player.getDisplayName(), group});

        if (player.hasPermission("aio.chat.placeholders"))
            message = aio.getPlaceholdersUtils().replacePlaceholders(message);

        return Chat.getTranslated(format).replace("{MESSAGE}", Chat.getTranslated(message, player, "aio.chat-colors"));
    }
}
