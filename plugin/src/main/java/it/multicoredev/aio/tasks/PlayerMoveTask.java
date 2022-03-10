package it.multicoredev.aio.tasks;

import it.multicoredev.aio.AIO;
import it.multicoredev.aio.api.IStorage;
import it.multicoredev.aio.api.models.User;
import it.multicoredev.aio.api.tp.ITeleportManager;
import it.multicoredev.aio.storage.config.Config;
import it.multicoredev.aio.storage.config.Localization;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * Copyright © 2021 - 2022 by Lorenzo Magni
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
public class PlayerMoveTask implements Runnable {
    private final Config config;
    private final IStorage storage;
    private final Localization localization;
    private final ITeleportManager teleportManager;
    private final Map<Player, Location> locationPlayerMap;

    public PlayerMoveTask(AIO aio) {
        this.config = aio.getConfiguration();
        this.storage = aio.getStorage();
        this.localization = aio.getLocalization();
        this.teleportManager = aio.getTeleportManager();
        this.locationPlayerMap = new HashMap<>();
    }

    @Override
    public void run() {
        long time = System.currentTimeMillis();

        for (Player p : Bukkit.getOnlinePlayers()) {
            User user = storage.getUser(p);
            if (user == null) continue;

            @Nullable Location oldLoc = user.getAfkLastLocation();
            Location loc = p.getLocation();

            if (locationPlayerMap.containsKey(p)) {
                if (!locationPlayerMap.get(p).equals(loc)) {
                    if (teleportManager.hasPendingTeleport(p)) {
                        teleportManager.cancelTeleport(p, localization.stayStillTpCancelled, true);
                    }

                    locationPlayerMap.remove(p);
                    locationPlayerMap.put(p, loc);
                }
            } else locationPlayerMap.put(p, loc);

            if (config.afkSection.afkRemoveOnMovement && !loc.equals(oldLoc)) {
                user.setAfk(false);
                continue;
            }

            long oldTime = user.getAfkCooldownTimestamp();
            if (oldTime < 0) {
                continue;
            }

            if (user.isAfk()) {
                if (oldTime + config.afkSection.afkMillisecondsCooldown > time) {
                    user.setAfk(false);
                }
            } else {
                if (oldTime + config.afkSection.afkMillisecondsCooldown < time) {
                    user.setAfk(true);
                }
            }
        }
    }
}
