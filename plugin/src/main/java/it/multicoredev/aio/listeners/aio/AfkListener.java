package it.multicoredev.aio.listeners.aio;

import it.multicoredev.aio.AIO;
import it.multicoredev.aio.api.events.AfkToggleEvent;
import it.multicoredev.aio.api.utils.IPlaceholdersUtils;
import it.multicoredev.aio.listeners.PluginListenerExecutor;
import it.multicoredev.mbcore.spigot.Chat;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

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
public class AfkListener extends PluginListenerExecutor<AfkToggleEvent> {
    private final IPlaceholdersUtils placeholdersUtils;

    public AfkListener(Class<AfkToggleEvent> eventClass, AIO aio) {
        super(eventClass, aio);
        this.placeholdersUtils = aio.getPlaceholdersUtils();
    }

    @Override
    public void onEvent(@NotNull AfkToggleEvent event) {
        Player player = event.getPlayer();
        if (event.isAfk()) {
            if (config.afkSection.modifyDisplayName) {
                player.setDisplayName(localization.afkDisplayNamePrefix + player.getDisplayName());
            }
            executeEnterAFKCommands(player);
            broadcast(player, localization.afkEnterBroadcastSelf, localization.afkEnterBroadcastOthers);
        } else {
            if (config.afkSection.modifyDisplayName) {
                player.setDisplayName(player.getDisplayName().replace(localization.afkDisplayNamePrefix, ""));
            }
            executeLeaveAFKCommands(player);
            broadcast(player, localization.afkLeaveBroadcastSelf, localization.afkLeaveBroadcastOthers);
        }
    }

    private void broadcast(Player player, String selfMsg, String othersMsg) {
        Chat.send(placeholdersUtils.replacePlaceholders(selfMsg, "{PLAYER}", player.getName()), player);
        if (config.afkSection.broadcastEverybody || !config.afkSection.doNotBroadcast) {
            boolean checkForPerms = !config.afkSection.broadcastEverybody;
            String msg = placeholdersUtils.replacePlaceholders(othersMsg, "{PLAYER}", player.getName());
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (!p.equals(player)) {
                    if (checkForPerms) {
                        if (p.hasPermission("aio.afk.broadcast")) {
                            Chat.send(msg, p);
                        }
                    } else {
                        Chat.send(msg, p);
                    }
                }
            }
        }
    }

    private void executeEnterAFKCommands(Player player) {
        Server server = Bukkit.getServer();
        ConsoleCommandSender consoleSender = Bukkit.getConsoleSender();
        for (String command : config.afkSection.afkEnterCommandsConsole) {
            try {
                server.dispatchCommand(consoleSender, command);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        for (String command : config.afkSection.afkEnterCommandsPlayer) {
            try {
                player.performCommand(command);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void executeLeaveAFKCommands(Player player) {
        Server server = Bukkit.getServer();
        ConsoleCommandSender consoleSender = Bukkit.getConsoleSender();
        for (String command : config.afkSection.afkLeaveCommandsConsole) {
            try {
                server.dispatchCommand(consoleSender, command);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        for (String command : config.afkSection.afkLeaveCommandsPlayer) {
            try {
                player.performCommand(command);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
