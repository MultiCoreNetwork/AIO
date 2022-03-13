package it.multicoredev.aio.api.tp;

import com.google.common.base.Preconditions;
import org.bukkit.Location;
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
public class Teleport {
    private final Player player;
    private Location to;
    private long timer;
    private String pendingMessage;
    private String postMessage;

    /**
     * Create a new Teleport object.
     *
     * @param player         the player to teleport.
     * @param to             the destination of the player.
     * @param timer          the time in ticks before the teleport.
     * @param pendingMessage the message to send to the player instantly if the timer is greater than 0.
     * @param postMessage    the message to send to the player after the teleport.
     */
    public Teleport(@NotNull Player player, @NotNull Location to, long timer, String pendingMessage, String postMessage) {
        Preconditions.checkNotNull(player);
        Preconditions.checkNotNull(to);

        this.player = player;
        this.to = to;
        this.timer = timer;
        this.pendingMessage = pendingMessage;
        this.postMessage = postMessage;
    }

    /**
     * Create a new Teleport object.
     *
     * @param player the player to teleport.
     * @param to     the destination of the player.
     * @param timer  the time in ticks before the teleport.
     */
    public Teleport(@NotNull Player player, @NotNull Location to, long timer) {
        this(player, to, timer, null, null);
    }

    /**
     * Create a new Teleport object.
     * Default timer is 0.
     *
     * @param player         the player to teleport.
     * @param to             the destination of the player.
     * @param pendingMessage the message to send to the player instantly if the timer is greater than 0.
     * @param postMessage    the message to send to the player after the teleport.
     */
    public Teleport(@NotNull Player player, @NotNull Location to, String pendingMessage, String postMessage) {
        this(player, to, 0, pendingMessage, postMessage);
    }

    /**
     * Create a new Teleport object.
     * Default timer is 0.
     *
     * @param player the player to teleport.
     * @param to     the destination of the player.
     */
    public Teleport(@NotNull Player player, @NotNull Location to) {
        this(player, to, 0, null, null);
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
        return player.getLocation();
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
     * Get the time in seconds to wait for the teleport.
     *
     * @return the time in seconds for the teleport.
     */
    public long getTimer() {
        return timer;
    }

    /**
     * Set the time in seconds to wait for the teleport.
     *
     * @param timer the time in seconds for the teleport.
     */
    public void setTimer(long timer) {
        this.timer = timer;
    }

    /**
     * Get the message to send to the player instantly if the timer is greater than 0.
     *
     * @return the message to send to the player.
     */
    public String getPendingMessage() {
        return pendingMessage;
    }

    /**
     * Set the message to send to the player instantly if the timer is greater than 0.
     *
     * @param pendingMessage the message to send to the player.
     * @return this object.
     */
    public Teleport setPendingMessage(String pendingMessage) {
        this.pendingMessage = pendingMessage;
        return this;
    }

    /**
     * Get the message to send to the player after the teleport.
     *
     * @return the message to send to the player.
     */
    public String getPostMessage() {
        return postMessage;
    }

    /**
     * Set the message to send to the player after the teleport.
     *
     * @param postMessage the message to send to the player.
     * @return this object.
     */
    public Teleport setPostMessage(String postMessage) {
        this.postMessage = postMessage;
        return this;
    }

    /**
     * Enumerator of the reasons for a teleport to be cancelled.
     */
    public enum CancelReason {
        NOT_ONLINE,
        MOVEMENT,
        DAMAGE,
        UNSAFE_DESTINATION,
        OTHER
    }
}
