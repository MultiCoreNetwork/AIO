package it.multicoredev.aio.api.tp;

import com.google.common.base.Preconditions;
import it.multicoredev.aio.api.AIO;
import it.multicoredev.aio.api.events.PlayerPostTeleportEvent;
import it.multicoredev.aio.api.events.PlayerPreTeleportEvent;
import it.multicoredev.aio.api.events.PlayerTeleportCancelledEvent;
import it.multicoredev.aio.api.events.PlayerTeleportRequestEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

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
public class Teleport {
    private final Player player;
    private Location from;
    private Location to;
    private long timer;
    private BukkitTask task;

    /**
     * Create a new Teleport object.
     *
     * @param player the player to teleport.
     * @param to     the destination of the player.
     * @param timer  the time in ticks before the teleport.
     */
    public Teleport(@NotNull Player player, @NotNull Location to, long timer) {
        Preconditions.checkNotNull(player);
        Preconditions.checkNotNull(to);

        this.player = player;
        this.from = player.getLocation();
        this.to = to;
        this.timer = timer;
    }

    /**
     * Get the teleported player.
     *
     * @return the teleported player.
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Get the starting location of the player.
     *
     * @return the starting location of the player.
     */
    public Location getFrom() {
        return from;
    }

    /**
     * Get the destination of the player.
     *
     * @return the destination of the player.
     */
    public Location getTo() {
        return to;
    }

    /**
     * Set the destination of the player.
     *
     * @param to the destination of the player.
     */
    public void setTo(@NotNull Location to) {
        Preconditions.checkNotNull(to);

        this.to = to;
    }

    /**
     * Get the time to wait for the teleport.
     *
     * @return the time in ticks for the teleport.
     */
    public long getTimer() {
        return timer;
    }

    /**
     * Set the time to wait for the teleport.
     *
     * @param timer the time in ticks for the teleport.
     */
    public void setTimer(long timer) {
        this.timer = timer;
    }

    public void teleport() {
        PlayerTeleportRequestEvent tpReqEvent = new PlayerTeleportRequestEvent(this);
        Bukkit.getPluginManager().callEvent(tpReqEvent);

        if (tpReqEvent.isCancelled()) {
            cancel(tpReqEvent.getCancelReason(), tpReqEvent.shouldNotify());
            return;
        }

        if (timer <= 0) {
            teleportNow();
        } else {
            if (player.hasPermission("aio.teleport.instant")) {
                teleportNow();
            } else {
                task = Bukkit.getScheduler().runTaskLater(AIO.getInstance(), this::teleportNow, timer);
            }
        }
    }

    /**
     * Cancel this task. DO NOT USE THIS METHOD DIRECTLY.
     *
     * @param reason Reason for the cancellation.
     * @param notify Whether to notify the player.
     */
    public void cancel(String reason, boolean notify) {
        if (task != null) {
            if (!task.isCancelled()) task.cancel();
            task = null;
        }

        PlayerTeleportCancelledEvent tpCancEvent = new PlayerTeleportCancelledEvent(this, reason, notify);
        Bukkit.getPluginManager().callEvent(tpCancEvent);
    }

    private void teleportNow() {
        if (!player.isOnline()) {
            cancel("Player is not online", false);
            return;
        }

        from = player.getLocation();

        PlayerPreTeleportEvent preTpEvent = new PlayerPreTeleportEvent(this);
        Bukkit.getPluginManager().callEvent(preTpEvent);

        if (preTpEvent.isCancelled()) {
            cancel(preTpEvent.getCancelReason(), preTpEvent.shouldNotify());
            return;
        }

        player.teleport(to);

        Bukkit.getPluginManager().callEvent(new PlayerPostTeleportEvent(this));
    }
}
