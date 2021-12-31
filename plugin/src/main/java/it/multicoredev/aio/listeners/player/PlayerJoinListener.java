package it.multicoredev.aio.listeners.player;

import it.multicoredev.aio.AIO;
import it.multicoredev.aio.api.User;
import it.multicoredev.aio.listeners.PluginListenerExecutor;
import it.multicoredev.aio.models.HelpBook;
import it.multicoredev.aio.storage.config.modules.JoinQuitModule;
import it.multicoredev.aio.storage.config.modules.SpawnModule;
import it.multicoredev.aio.utils.Utils;
import it.multicoredev.mbcore.spigot.Chat;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

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
public class PlayerJoinListener extends PluginListenerExecutor<PlayerJoinEvent> {
    private final SpawnModule spawnModule;
    private final JoinQuitModule joinAndQuitModule;

    public PlayerJoinListener(AIO aio) {
        super(aio);

        this.spawnModule = aio.getModuleManager().getModule(SpawnModule.class);
        this.joinAndQuitModule = aio.getModuleManager().getModule(JoinQuitModule.class);
    }

    @Override
    public void onEvent(@NotNull PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Bukkit.getScheduler().runTaskAsynchronously(aio, () -> aio.addToUsermap(player.getName(), player.getUniqueId()));

        boolean isNew = !storage.userExists(player.getUniqueId());

        User user;

        if (isNew) {
            user = new User(player);
            user.setLastLocation(spawnModule.spawnLocation);
            user.setLogin(new Date().getTime());

            if (!config.helpBookSection.firstJoinBooks.isEmpty()) {
                Inventory inventory = player.getInventory();
                config.helpBookSection.firstJoinBooks.forEach(book -> {
                    HelpBook hb = config.helpBookSection.getBook(book);
                    inventory.addItem(hb.getBook());
                });
            }

            storage.registerUser(user);
        } else {
            user = storage.getUser(player.getUniqueId());
            user.setName(player.getName());
            user.setLastLocation(player.getLocation());

            user.setLogin(new Date().getTime());

            if (user.hasGod()) {
                if (config.disableGodOnJoin) {
                    user.setGod(false);
                    player.setInvulnerable(false);
                } else {
                    player.setInvulnerable(true);
                }
            }

            storage.updateUser(user);
        }

        aio.addUserToCache(user);

        if (config.modules.get("spawn")) {
            if (!spawnModule(event, isNew)) return;
        }

        if (config.modules.get("join_and_quit")) {
            if (!joinAndQuitModule(event, isNew)) return;
        }
    }

    private boolean spawnModule(PlayerJoinEvent event, boolean isNew) {
        Player player = event.getPlayer();

        if (!isNew && !spawnModule.spawnOnJoin) return true;

        Location spawn = spawnModule.spawnLocation;
        if (spawn == null) return true;

        player.teleport(spawn);

        return true;
    }

    private boolean joinAndQuitModule(PlayerJoinEvent event, boolean isNew) {
        if (!joinAndQuitModule.overrideJoinMessage) return true;
        event.setJoinMessage(null);

        Player player = event.getPlayer();

        if (isNew) {
            if (joinAndQuitModule.showFirstJoinMessage) sendJoinMessage(player, localization.firstJoinMsg);
            else sendJoinMessage(player, localization.joinMsg);

            if (joinAndQuitModule.sendWelcomeMessage) {
                List<String> welcomeMsg = new ArrayList<>(Arrays.asList(localization.welcomeMsg.split("\\n")));
                for (String msg : welcomeMsg) {
                    msg = msg.replace("{DISPLAYNAME}", player.getDisplayName()).replace("{NAME}", player.getName());
                    if (AIO.PAPI) PlaceholderAPI.setPlaceholders(player, msg);
                }

                Chat.send(welcomeMsg, player);
            }

            if (joinAndQuitModule.sendMotd) sendMotd(player);

            if (joinAndQuitModule.playJingleOnFirstJoin) {
                Sound sound = Sound.valueOf(joinAndQuitModule.jingleSound);
                float volume = joinAndQuitModule.jingleVolume;

                Bukkit.getScheduler().runTaskAsynchronously(aio, () -> {
                    Map<String, Float> instructions = parseJingle();

                    for (Player p : Bukkit.getOnlinePlayers()) {
                        playJingle(p, instructions, sound, volume);
                    }
                });
            }
        }

        return true;
    }

    private void sendJoinMessage(Player player, String msg) {
        msg = msg.replace("{DISPLAYNAME}", player.getDisplayName()).replace("{NAME}", player.getName());
        if (AIO.PAPI) PlaceholderAPI.setPlaceholders(player, msg);

        if (Utils.isVanished(player)) Chat.broadcast(Chat.getTranslated(msg), "pv.see");
        else Chat.broadcast(Chat.getTranslated(msg));
    }

    private Map<String, Float> parseJingle() {
        Map<String, Float> instructions = new HashMap<>();

        for (String line : joinAndQuitModule.jingle) {
            float duration;
            try {
                duration = Float.parseFloat(line.substring(line.indexOf(":") + 1));
            } catch (Exception ignored) {
                continue;
            }

            if (line.toLowerCase(Locale.ROOT).startsWith("p")) {
                instructions.put("play", duration);
            } else {
                instructions.put("pause", duration);
            }
        }

        return instructions;
    }

    private void playJingle(Player player, Map<String, Float> instructions, Sound sound, float volume) {
        for (Map.Entry<String, Float> instruction : instructions.entrySet()) {
            if (instruction.getKey().equals("play")) {
                player.playSound(player.getLocation(), sound, volume, instruction.getValue());
            } else {
                Utils.sleep(instruction.getValue().longValue());
            }
        }
    }

    private void sendMotd(Player player) {
        String motd = localization.motd;

        motd = motd.replace("{DISPLAYNAME}", player.getDisplayName())
                .replace("{NAME}", player.getName());

        if (Utils.isValidURL(motd)) {
            try {
                HttpURLConnection connection = (HttpURLConnection) new URL(motd).openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("User-Agent", "Mozilla/5.0");

                StringBuilder builder = new StringBuilder();

                try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        builder.append(line).append("\n");
                    }
                }

                motd = builder.toString();
            } catch (IOException ignored) {
                return;
            }
        }

        motd = motd.replace("{DISPLAYNAME}", player.getDisplayName())
                .replace("{NAME}", player.getName());
        PlaceholderAPI.setPlaceholders(player, motd);

        List<String> motdList = Arrays.asList(motd.split("\n"));

        if (joinAndQuitModule.motdDelay > 0)
            Bukkit.getScheduler().runTaskLaterAsynchronously(aio, () -> Chat.send(motdList, player), joinAndQuitModule.motdDelay * 20);
        else Chat.send(motdList, player);
    }
}
