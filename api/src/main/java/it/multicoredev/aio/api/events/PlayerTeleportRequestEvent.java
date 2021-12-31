package it.multicoredev.aio.api.events;

import it.multicoredev.aio.api.tp.Teleport;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
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
public class PlayerTeleportRequestEvent extends Event implements Cancellable {
    private static final HandlerList HANDLERS = new HandlerList();
    private boolean cancelled;

    private final Teleport teleport;
    private String cancelReason;
    private boolean notify;

    /**
     * PlayerTeleportRequestEvent is called when a teleport request is called from the {@link it.multicoredev.aio.api.tp.ITeleportManager}.
     *
     * @param teleport the teleport instance.
     */
    public PlayerTeleportRequestEvent(@NotNull Teleport teleport) {
        this.teleport = teleport;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    /**
     * Get the player to be teleported.
     *
     * @return the player to be teleported.
     */
    public Player getPlayer() {
        return teleport.getPlayer();
    }

    /**
     * Get the {@link Location} of the player when the teleport request is called.
     *
     * @return the location of the player when the teleport request is called.
     */
    public Location getFrom() {
        return teleport.getFrom();
    }

    /**
     * Get the destination {@link Location} of the player after the teleport.
     *
     * @return the destination of the player.
     */
    public Location getTo() {
        return teleport.getTo();
    }

    /**
     * Set the destination of the player.
     *
     * @param to the destination of the player.
     */
    public void setTo(@NotNull Location to) {
        teleport.setTo(to);
    }

    /**
     * Get the time to wait for the teleport.
     *
     * @return the time in ticks for the teleport.
     */
    public long getTimer() {
        return teleport.getTimer();
    }

    /**
     * Set the time to wait for the teleport.
     *
     * @param timer the time in ticks for the teleport.
     */
    public void setTimer(long timer) {
        teleport.setTimer(timer);
    }

    /**
     * Get the reason of the cancellation of the teleport.
     *
     * @return the reason of the cancellation of the teleport.
     */
    public String getCancelReason() {
        return cancelReason;
    }

    /**
     * Set the reason of the cancellation of the teleport.
     *
     * @param cancelReason the reason of the cancellation of the teleport.
     */
    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }

    /**
     * Check if the player should be notified.
     *
     * @return true if the player should be notified.
     */
    public boolean shouldNotify() {
        return notify;
    }

    /**
     * Set if the player should be notified.
     *
     * @param notify true if the player should be notified.
     */
    public void setNotify(boolean notify) {
        this.notify = notify;
    }
}
