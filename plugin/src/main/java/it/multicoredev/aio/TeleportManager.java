package it.multicoredev.aio;

import com.google.common.base.Preconditions;
import it.multicoredev.aio.api.tp.ITeleportManager;
import it.multicoredev.aio.api.tp.Teleport;
import it.multicoredev.aio.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

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
public class TeleportManager implements ITeleportManager {
    private final Map<Player, Teleport> pendingTeleports = new HashMap<>();

    @Override
    public void teleport(@NotNull Player player, @NotNull Location to, long timer) {
        Preconditions.checkNotNull(player);
        Preconditions.checkNotNull(to);

        Teleport tp = new Teleport(player, to, timer);
        pendingTeleports.put(player, tp);
        tp.teleport();
    }

    @Override
    public void teleport(@NotNull Player player, @NotNull Location to) {
        Preconditions.checkNotNull(player);
        Preconditions.checkNotNull(to);

        teleport(player, to, 0);
    }

    @Override
    public void randomTeleport(@NotNull Player player, @NotNull Location center, int minDistance, int maxDistance, long timer) {
        Preconditions.checkNotNull(player);
        Preconditions.checkNotNull(center);

        Location to = Utils.getRandomLocation(center, minDistance, maxDistance);
        if (to == null) return;

        teleport(player, to, timer);
    }

    @Override
    public void randomTeleport(@NotNull Player player, @NotNull Location center, int minDistance, int maxDistance) {
        Preconditions.checkNotNull(player);
        Preconditions.checkNotNull(center);

        randomTeleport(player, center, minDistance, maxDistance, 0);
    }

    @Override
    public void randomTeleport(@NotNull Player player, @NotNull String world, double xCenter, double zCenter, int minDistance, int maxDistance, long timer) {
        Preconditions.checkNotNull(player);
        Preconditions.checkNotNull(world);
        Preconditions.checkNotNull(Bukkit.getWorld(world));

        randomTeleport(player, new Location(Bukkit.getWorld(world), xCenter, 0, zCenter), minDistance, maxDistance, timer);
    }

    @Override
    public void randomTeleport(@NotNull Player player, @NotNull String world, double xCenter, double zCenter, int minDistance, int maxDistance) {
        Preconditions.checkNotNull(player);
        Preconditions.checkNotNull(world);
        Preconditions.checkNotNull(Bukkit.getWorld(world));

        randomTeleport(player, new Location(Bukkit.getWorld(world), xCenter, 0, zCenter), minDistance, maxDistance);
    }

    @Override
    public void randomTeleport(@NotNull Player player, double xCenter, double zCenter, int minDistance, int maxDistance, long timer) {
        Preconditions.checkNotNull(player);

        randomTeleport(player, new Location(player.getWorld(), xCenter, 0, zCenter), minDistance, maxDistance, timer);
    }

    @Override
    public void randomTeleport(@NotNull Player player, double xCenter, double zCenter, int minDistance, int maxDistance) {
        Preconditions.checkNotNull(player);

        randomTeleport(player, new Location(player.getWorld(), xCenter, 0, zCenter), minDistance, maxDistance);
    }

    @Override
    public void randomTeleport(@NotNull Player player, int minDistance, int maxDistance, long timer) {
        Preconditions.checkNotNull(player);

        randomTeleport(player, player.getLocation(), minDistance, maxDistance);
    }

    @Override
    public void randomTeleport(@NotNull Player player, int minDistance, int maxDistance) {
        Preconditions.checkNotNull(player);

        randomTeleport(player, player.getLocation(), minDistance, maxDistance);
    }

    @Override
    public void cancelTeleport(@NotNull Teleport teleport, String reason, boolean notify) {
        Preconditions.checkNotNull(teleport);

        teleport.cancel(reason, notify);
    }

    @Override
    public void cancelTeleport(@NotNull Teleport teleport, String reason) {
        Preconditions.checkNotNull(teleport);

        teleport.cancel(reason, false);
    }

    @Override
    public void cancelTeleport(@NotNull Teleport teleport, boolean notify) {
        Preconditions.checkNotNull(teleport);

        teleport.cancel(null, notify);
    }

    @Override
    public void cancelTeleport(@NotNull Teleport teleport) {
        Preconditions.checkNotNull(teleport);

        teleport.cancel(null, false);
    }

    @Override
    public void cancelTeleport(@NotNull Player player, String reason) {
        Preconditions.checkNotNull(player);

        if (hasPendingTeleport(player)) getPendingTeleport(player).cancel(reason, false);
    }

    @Override
    public void cancelTeleport(@NotNull Player player) {
        Preconditions.checkNotNull(player);

        Teleport tp = pendingTeleports.get(player);
        tp.cancel(null, false);
    }

    @Override
    public boolean hasPendingTeleport(@NotNull Player player) {
        Preconditions.checkNotNull(player);

        return pendingTeleports.containsKey(player);
    }

    @Override
    public @Nullable Teleport getPendingTeleport(@NotNull Player player) {
        Preconditions.checkNotNull(player);

        return pendingTeleports.get(player);
    }

    @Override
    public Map<Player, Teleport> getPendingTeleports() {
        return Collections.unmodifiableMap(pendingTeleports);
    }

    public void removeTeleport(Player player) {
        pendingTeleports.remove(player);
    }
}
