package it.multicoredev.aio.api.tp;

import it.multicoredev.aio.api.models.tp.TeleportRequest;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
public interface ITeleportManager {

    /**
     * Teleport a player to the give location after some time.
     *
     * @param player the player to teleport.
     * @param to     the destination of the player.
     * @param timer  the time in milliseconds before the teleport.
     */
    void teleport(@NotNull Player player, @NotNull Location to, long timer);

    /**
     * Teleport a player to the give location.
     *
     * @param player the player to teleport.
     * @param to     the destination of the player.
     */
    void teleport(@NotNull Player player, @NotNull Location to);

    /**
     * Teleport a player to a random location after some time.
     *
     * @param player      the player to teleport.
     * @param center      the center of the teleport area.
     * @param minDistance the minimum distance from the center.
     * @param maxDistance the maximum distance from the center.
     * @param timer       the time in milliseconds before the teleport.
     */
    void randomTeleport(@NotNull Player player, @NotNull Location center, int minDistance, int maxDistance, long timer);

    /**
     * Teleport a player to a random location.
     *
     * @param player      the player to teleport.
     * @param center      the center of the teleport area.
     * @param minDistance the minimum distance from the center.
     * @param maxDistance the maximum distance from the center.
     */
    void randomTeleport(@NotNull Player player, @NotNull Location center, int minDistance, int maxDistance);

    /**
     * Teleport a player to a random location after some time.
     *
     * @param player      the player to teleport.
     * @param world       the world.
     * @param xCenter     the x coordinate of the center.
     * @param zCenter     the z coordinate of the center.
     * @param minDistance the minimum distance from the center.
     * @param maxDistance the maximum distance from the center.
     * @param timer       the time in milliseconds before the teleport.
     */
    void randomTeleport(@NotNull Player player, @NotNull String world, double xCenter, double zCenter, int minDistance, int maxDistance, long timer);

    /**
     * Teleport a player to a random location after.
     *
     * @param player      the player to teleport.
     * @param world       the world.
     * @param xCenter     the x coordinate of the center.
     * @param zCenter     the z coordinate of the center.
     * @param minDistance the minimum distance from the center.
     * @param maxDistance the maximum distance from the center.
     */
    void randomTeleport(@NotNull Player player, @NotNull String world, double xCenter, double zCenter, int minDistance, int maxDistance);

    /**
     * Teleport a player to a random location after some time.
     *
     * @param player      the player to teleport.
     * @param xCenter     the x coordinate of the center.
     * @param zCenter     the z coordinate of the center.
     * @param minDistance the minimum distance from the center.
     * @param maxDistance the maximum distance from the center.
     * @param timer       the time in milliseconds before the teleport.
     */
    void randomTeleport(@NotNull Player player, double xCenter, double zCenter, int minDistance, int maxDistance, long timer);

    /**
     * Teleport a player to a random location.
     *
     * @param player      the player to teleport.
     * @param xCenter     the x coordinate of the center.
     * @param zCenter     the z coordinate of the center.
     * @param minDistance the minimum distance from the center.
     * @param maxDistance the maximum distance from the center.
     */
    void randomTeleport(@NotNull Player player, double xCenter, double zCenter, int minDistance, int maxDistance);

    /**
     * Teleport a player to a random location after some time.
     *
     * @param player      the player to teleport.
     * @param minDistance the minimum distance from the center.
     * @param maxDistance the maximum distance from the center.
     * @param timer       the time in milliseconds before the teleport.
     */
    void randomTeleport(@NotNull Player player, int minDistance, int maxDistance, long timer);

    /**
     * Teleport a player to a random location.
     *
     * @param player      the player to teleport.
     * @param minDistance the minimum distance from the center.
     * @param maxDistance the maximum distance from the center.
     */
    void randomTeleport(@NotNull Player player, int minDistance, int maxDistance);

    /**
     * Cancel the teleport of a player.
     *
     * @param teleport the teleport to be cancelled.
     * @param reason   the reason of the cancellation.
     * @param notify   if the player should be notified.
     */
    void cancelTeleport(@NotNull Teleport teleport, String reason, boolean notify);

    /**
     * Cancel the teleport of a player.
     *
     * @param teleport the teleport to be cancelled.
     * @param reason   the reason of the cancellation.
     */
    void cancelTeleport(@NotNull Teleport teleport, String reason);

    /**
     * Cancel the teleport of a player.
     *
     * @param teleport the teleport to be cancelled.
     * @param notify   if the player should be notified.
     */
    void cancelTeleport(@NotNull Teleport teleport, boolean notify);

    /**
     * Cancel the teleport of a player.
     *
     * @param teleport the teleport to be cancelled.
     */
    void cancelTeleport(@NotNull Teleport teleport);

    /**
     * Cancel the teleport of a player.
     *
     * @param player the player whose teleport to be cancelled.
     * @param reason the reason of the cancellation.
     */
    void cancelTeleport(@NotNull Player player, String reason);

    /**
     * Cancel the teleport of a player.
     *
     * @param player the player whose teleport to be cancelled.
     */
    void cancelTeleport(@NotNull Player player);

    /**
     * Check if a player has a pending teleport.
     *
     * @param player the player to check.
     * @return true if the player has a pending teleport, otherwise false
     */
    boolean hasPendingTeleport(@NotNull Player player);

    /**
     * Get the pending teleport of a player.
     *
     * @param player the player whose teleport is pending.
     * @return the pending {@link Teleport} of the player or null if none is found.
     */
    @Nullable Teleport getPendingTeleport(@NotNull Player player);

    /**
     * Get a map of all the pending teleports.
     *
     * @return the map of all the pending teleports.
     */
    Map<Player, Teleport> getPendingTeleports();

    /**
     * Send a new teleport request.
     *
     * @param request the request to send.
     */
    void sendTeleportRequest(TeleportRequest request);
}
