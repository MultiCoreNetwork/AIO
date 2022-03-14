package it.multicoredev.aio.api.tp;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

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
public interface ITeleportManager {

    /**
     * Execute the given teleport.
     *
     * @param teleport the teleport to be executed.
     */
    void teleport(@NotNull Teleport teleport);

    /**
     * Teleport a player to the give location after some time.
     *
     * @param player         the player to teleport.
     * @param to             the destination of the player.
     * @param timer          the time in seconds before the teleport.
     * @param pendingMessage the message to send to the player instantly if the timer is greater than 0.
     * @param postMessage    the message to send to the player after the teleport.
     */
    void teleport(@NotNull Player player, @NotNull Location to, long timer, String pendingMessage, String postMessage);

    /**
     * Teleport a player to the give location after some time.
     *
     * @param player      the player to teleport.
     * @param to          the destination of the player.
     * @param timer       the time in seconds before the teleport.
     * @param postMessage the message to send to the player after the teleport.
     */
    void teleport(@NotNull Player player, @NotNull Location to, long timer, String postMessage);

    /**
     * Teleport a player to the give location after some time.
     * Default value for the timer is 0.
     *
     * @param player the player to teleport.
     * @param to     the destination of the player.
     * @param timer  the time in seconds before the teleport.
     */
    void teleport(@NotNull Player player, @NotNull Location to, long timer);

    /**
     * Teleport a player to the give location.
     * Default value for the timer is 0.
     *
     * @param player      the player to teleport.
     * @param to          the destination of the player.
     * @param postMessage the message to send to the player after the teleport.
     */
    void teleport(@NotNull Player player, @NotNull Location to, String postMessage);

    /**
     * Teleport a player to the give location.
     * Default value for the timer is 0.
     *
     * @param player the player to teleport.
     * @param to     the destination of the player.
     */
    void teleport(@NotNull Player player, @NotNull Location to);

    /**
     * Teleport a player to the give location after some time.
     *
     * @param player         the player to teleport.
     * @param target         the target of the player.
     * @param timer          the time in seconds before the teleport.
     * @param pendingMessage the message to send to the player instantly if the timer is greater than 0.
     * @param postMessage    the message to send to the player after the teleport.
     */
    void teleport(@NotNull Player player, @NotNull Player target, long timer, String pendingMessage, String postMessage);

    /**
     * Teleport a player to the give location after some time.
     *
     * @param player      the player to teleport.
     * @param target      the target of the player.
     * @param timer       the time in seconds before the teleport.
     * @param postMessage the message to send to the player after the teleport.
     */
    void teleport(@NotNull Player player, @NotNull Player target, long timer, String postMessage);

    /**
     * Teleport a player to the give location after some time.
     * Default value for the timer is 0.
     *
     * @param player the player to teleport.
     * @param target the target of the player.
     * @param timer  the time in seconds before the teleport.
     */
    void teleport(@NotNull Player player, @NotNull Player target, long timer);

    /**
     * Teleport a player to the give location.
     * Default value for the timer is 0.
     *
     * @param player      the player to teleport.
     * @param target      the target of the player.
     * @param postMessage the message to send to the player after the teleport.
     */
    void teleport(@NotNull Player player, @NotNull Player target, String postMessage);

    /**
     * Teleport a player to the give location.
     * Default value for the timer is 0.
     *
     * @param player the player to teleport.
     * @param target the target of the player.
     */
    void teleport(@NotNull Player player, @NotNull Player target);

    /**
     * Teleport a player to a random location after some time.
     *
     * @param player         the player to teleport.
     * @param center         the center of the teleport area.
     * @param minDistance    the minimum distance from the center.
     * @param maxDistance    the maximum distance from the center.
     * @param timer          the time in seconds before the teleport.
     * @param pendingMessage the message to send to the player instantly if the timer is greater than 0.
     * @param postMessage    the message to send to the player after the teleport.
     */
    void randomTeleport(@NotNull Player player, @NotNull Location center, int minDistance, int maxDistance, long timer, String pendingMessage, String postMessage);

    /**
     * Teleport a player to a random location after some time.
     *
     * @param player      the player to teleport.
     * @param center      the center of the teleport area.
     * @param minDistance the minimum distance from the center.
     * @param maxDistance the maximum distance from the center.
     * @param timer       the time in seconds before the teleport.
     * @param postMessage the message to send to the player after the teleport.
     */
    void randomTeleport(@NotNull Player player, @NotNull Location center, int minDistance, int maxDistance, long timer, String postMessage);

    /**
     * Teleport a player to a random location after some time.
     *
     * @param player      the player to teleport.
     * @param center      the center of the teleport area.
     * @param minDistance the minimum distance from the center.
     * @param maxDistance the maximum distance from the center.
     * @param timer       the time in seconds before the teleport.
     */
    void randomTeleport(@NotNull Player player, @NotNull Location center, int minDistance, int maxDistance, long timer);

    /**
     * Teleport a player to a random location.
     * Default value for the timer is 0.
     *
     * @param player      the player to teleport.
     * @param center      the center of the teleport area.
     * @param minDistance the minimum distance from the center.
     * @param maxDistance the maximum distance from the center.
     * @param postMessage the message to send to the player after the teleport.
     */
    void randomTeleport(@NotNull Player player, @NotNull Location center, int minDistance, int maxDistance, String postMessage);

    /**
     * Teleport a player to a random location.
     * Default value for the timer is 0.
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
     * @param player         the player to teleport.
     * @param world          the world.
     * @param xCenter        the x coordinate of the center.
     * @param zCenter        the z coordinate of the center.
     * @param minDistance    the minimum distance from the center.
     * @param maxDistance    the maximum distance from the center.
     * @param timer          the time in seconds before the teleport.
     * @param pendingMessage the message to send to the player instantly if the timer is greater than 0.
     * @param postMessage    the message to send to the player after the teleport.
     */
    void randomTeleport(@NotNull Player player, @NotNull World world, double xCenter, double zCenter, int minDistance, int maxDistance, long timer, String pendingMessage, String postMessage);

    /**
     * Teleport a player to a random location after some time.
     *
     * @param player      the player to teleport.
     * @param world       the world.
     * @param xCenter     the x coordinate of the center.
     * @param zCenter     the z coordinate of the center.
     * @param minDistance the minimum distance from the center.
     * @param maxDistance the maximum distance from the center.
     * @param timer       the time in seconds before the teleport.
     * @param postMessage the message to send to the player after the teleport.
     */
    void randomTeleport(@NotNull Player player, @NotNull World world, double xCenter, double zCenter, int minDistance, int maxDistance, long timer, String postMessage);

    /**
     * Teleport a player to a random location after some time.
     *
     * @param player      the player to teleport.
     * @param world       the world.
     * @param xCenter     the x coordinate of the center.
     * @param zCenter     the z coordinate of the center.
     * @param minDistance the minimum distance from the center.
     * @param maxDistance the maximum distance from the center.
     * @param timer       the time in seconds before the teleport.
     */
    void randomTeleport(@NotNull Player player, @NotNull World world, double xCenter, double zCenter, int minDistance, int maxDistance, long timer);

    /**
     * Teleport a player to a random location.
     *
     * @param player      the player to teleport.
     * @param world       the world.
     * @param xCenter     the x coordinate of the center.
     * @param zCenter     the z coordinate of the center.
     * @param minDistance the minimum distance from the center.
     * @param maxDistance the maximum distance from the center.
     * @param postMessage the message to send to the player after the teleport.
     */
    void randomTeleport(@NotNull Player player, @NotNull World world, double xCenter, double zCenter, int minDistance, int maxDistance, String postMessage);

    /**
     * Teleport a player to a random location.
     *
     * @param player      the player to teleport.
     * @param world       the world.
     * @param xCenter     the x coordinate of the center.
     * @param zCenter     the z coordinate of the center.
     * @param minDistance the minimum distance from the center.
     * @param maxDistance the maximum distance from the center.
     */
    void randomTeleport(@NotNull Player player, @NotNull World world, double xCenter, double zCenter, int minDistance, int maxDistance);

    /**
     * Teleport a player to a random location after some time.
     *
     * @param player         the player to teleport.
     * @param world          the world.
     * @param xCenter        the x coordinate of the center.
     * @param zCenter        the z coordinate of the center.
     * @param minDistance    the minimum distance from the center.
     * @param maxDistance    the maximum distance from the center.
     * @param timer          the time in seconds before the teleport.
     * @param pendingMessage the message to send to the player instantly if the timer is greater than 0.
     * @param postMessage    the message to send to the player after the teleport.
     */
    void randomTeleport(@NotNull Player player, @NotNull String world, double xCenter, double zCenter, int minDistance, int maxDistance, long timer, String pendingMessage, String postMessage);

    /**
     * Teleport a player to a random location after some time.
     *
     * @param player      the player to teleport.
     * @param world       the world.
     * @param xCenter     the x coordinate of the center.
     * @param zCenter     the z coordinate of the center.
     * @param minDistance the minimum distance from the center.
     * @param maxDistance the maximum distance from the center.
     * @param timer       the time in seconds before the teleport.
     * @param postMessage the message to send to the player after the teleport.
     */
    void randomTeleport(@NotNull Player player, @NotNull String world, double xCenter, double zCenter, int minDistance, int maxDistance, long timer, String postMessage);

    /**
     * Teleport a player to a random location after some time.
     *
     * @param player      the player to teleport.
     * @param world       the world.
     * @param xCenter     the x coordinate of the center.
     * @param zCenter     the z coordinate of the center.
     * @param minDistance the minimum distance from the center.
     * @param maxDistance the maximum distance from the center.
     * @param timer       the time in seconds before the teleport.
     */
    void randomTeleport(@NotNull Player player, @NotNull String world, double xCenter, double zCenter, int minDistance, int maxDistance, long timer);

    /**
     * Teleport a player to a random location.
     *
     * @param player      the player to teleport.
     * @param world       the world.
     * @param xCenter     the x coordinate of the center.
     * @param zCenter     the z coordinate of the center.
     * @param minDistance the minimum distance from the center.
     * @param maxDistance the maximum distance from the center.
     * @param postMessage the message to send to the player after the teleport.
     */
    void randomTeleport(@NotNull Player player, @NotNull String world, double xCenter, double zCenter, int minDistance, int maxDistance, String postMessage);

    /**
     * Teleport a player to a random location.
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
     * @param player         the player to teleport.
     * @param xCenter        the x coordinate of the center.
     * @param zCenter        the z coordinate of the center.
     * @param minDistance    the minimum distance from the center.
     * @param maxDistance    the maximum distance from the center.
     * @param timer          the time in seconds before the teleport.
     * @param pendingMessage the message to send to the player instantly if the timer is greater than 0.
     * @param postMessage    the message to send to the player after the teleport.
     */
    void randomTeleport(@NotNull Player player, double xCenter, double zCenter, int minDistance, int maxDistance, long timer, String pendingMessage, String postMessage);

    /**
     * Teleport a player to a random location after some time.
     *
     * @param player      the player to teleport.
     * @param xCenter     the x coordinate of the center.
     * @param zCenter     the z coordinate of the center.
     * @param minDistance the minimum distance from the center.
     * @param maxDistance the maximum distance from the center.
     * @param timer       the time in seconds before the teleport.
     * @param postMessage the message to send to the player after the teleport.
     */
    void randomTeleport(@NotNull Player player, double xCenter, double zCenter, int minDistance, int maxDistance, long timer, String postMessage);

    /**
     * Teleport a player to a random location after some time.
     *
     * @param player      the player to teleport.
     * @param xCenter     the x coordinate of the center.
     * @param zCenter     the z coordinate of the center.
     * @param minDistance the minimum distance from the center.
     * @param maxDistance the maximum distance from the center.
     * @param timer       the time in seconds before the teleport.
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
     * @param postMessage the message to send to the player after the teleport.
     */
    void randomTeleport(@NotNull Player player, double xCenter, double zCenter, int minDistance, int maxDistance, String postMessage);

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
     * @param player         the player to teleport.
     * @param minDistance    the minimum distance from the center.
     * @param maxDistance    the maximum distance from the center.
     * @param timer          the time in seconds before the teleport.
     * @param pendingMessage the message to send to the player instantly if the timer is greater than 0.
     * @param postMessage    the message to send to the player after the teleport.
     */
    void randomTeleport(@NotNull Player player, int minDistance, int maxDistance, long timer, String pendingMessage, String postMessage);

    /**
     * Teleport a player to a random location after some time.
     *
     * @param player      the player to teleport.
     * @param minDistance the minimum distance from the center.
     * @param maxDistance the maximum distance from the center.
     * @param timer       the time in seconds before the teleport.
     * @param postMessage the message to send to the player after the teleport.
     */
    void randomTeleport(@NotNull Player player, int minDistance, int maxDistance, long timer, String postMessage);

    /**
     * Teleport a player to a random location after some time.
     *
     * @param player      the player to teleport.
     * @param minDistance the minimum distance from the center.
     * @param maxDistance the maximum distance from the center.
     * @param timer       the time in seconds before the teleport.
     */
    void randomTeleport(@NotNull Player player, int minDistance, int maxDistance, long timer);

    /**
     * Teleport a player to a random location.
     *
     * @param player      the player to teleport.
     * @param minDistance the minimum distance from the center.
     * @param maxDistance the maximum distance from the center.
     * @param postMessage the message to send to the player after the teleport.
     */
    void randomTeleport(@NotNull Player player, int minDistance, int maxDistance, String postMessage);

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
     * @param message  the message to be sent to the player.
     */
    void cancelTeleport(@NotNull Teleport teleport, @NotNull Teleport.CancelReason reason, String message);

    /**
     * Cancel the teleport of a player.
     *
     * @param teleport the teleport to be cancelled.
     * @param reason   the reason of the cancellation.
     */
    void cancelTeleport(@NotNull Teleport teleport, @NotNull Teleport.CancelReason reason);

    /**
     * Cancel the teleport of a player.
     *
     * @param player  the player whose teleport to be cancelled.
     * @param reason  the reason of the cancellation.
     * @param message the message to be sent to the player.
     */
    void cancelTeleport(@NotNull Player player, @NotNull Teleport.CancelReason reason, String message);

    /**
     * Cancel the teleport of a player.
     *
     * @param player the player whose teleport to be cancelled.
     * @param reason the reason of the cancellation.
     */
    void cancelTeleport(@NotNull Player player, @NotNull Teleport.CancelReason reason);

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
     * Request a teleport to a player.
     *
     * @param request the request to be sent.
     */
    void requestTeleport(@NotNull TeleportRequest request);

    /**
     * Request a teleport to a player.
     *
     * @param requester        the player who request the teleport.
     * @param target           the player who will receive the request.
     * @param type             the type of the request.
     * @param timeout          the timeout in seconds of the request.
     * @param requesterMessage the message to send to the requester.
     * @param targetMessage    the message to send to the target.
     */
    void requestTeleport(@NotNull Player requester, @NotNull Player target, @NotNull TeleportRequest.RequestType type, long timeout, String requesterMessage, String targetMessage);

    /**
     * Request a teleport to a player.
     *
     * @param requester the player who request the teleport.
     * @param target    the player who will receive the request.
     * @param type      the type of the request.
     * @param timeout   the timeout in seconds of the request.
     */
    void requestTeleport(@NotNull Player requester, @NotNull Player target, @NotNull TeleportRequest.RequestType type, long timeout);

    /**
     * Cancel a teleport request.
     *
     * @param request                the request to be cancelled.
     * @param reason                 the reason of the cancellation.
     * @param cancelMessageRequester the message to send to the requester.
     * @param cancelMessageTarget    the message to send to the target.
     */
    void cancelTeleportRequest(@NotNull TeleportRequest request, @NotNull TeleportRequest.CancelReason reason, String cancelMessageRequester, String cancelMessageTarget);

    /**
     * Cancel a teleport request.
     *
     * @param request the request to be cancelled.
     * @param reason  the reason of the cancellation.
     */
    void cancelTeleportRequest(@NotNull TeleportRequest request, @NotNull TeleportRequest.CancelReason reason);

    /**
     * Cancel a teleport request.
     *
     * @param requester              the player who request the teleport.
     * @param reason                 the reason of the cancellation.
     * @param cancelMessageRequester the message to send to the requester.
     * @param cancelMessageTarget    the message to send to the target.
     */
    void cancelTeleportRequest(@NotNull Player requester, @NotNull TeleportRequest.CancelReason reason, String cancelMessageRequester, String cancelMessageTarget);

    /**
     * Cancel a teleport request.
     *
     * @param requester the player who request the teleport.
     * @param reason    the reason of the cancellation.
     */
    void cancelTeleportRequest(@NotNull Player requester, @NotNull TeleportRequest.CancelReason reason);

    /**
     * Check if a player has a teleport request as a requester.
     *
     * @param requester the player to check.
     * @return true if the player has a teleport request as a requester, false otherwise.
     */
    boolean hasRequesterTeleportRequest(@NotNull Player requester);

    /**
     * Check if a player has a teleport request as a target.
     *
     * @param target the player to check.
     * @return true if the player has a teleport request as a target, false otherwise.
     */
    boolean hasTargetTeleportRequest(@NotNull Player target);

    /**
     * Get the teleport request of a player as a requester.
     *
     * @param requester the player to check.
     * @return the teleport request of the player as a requester, null if the player has no teleport request as a requester.
     */
    @Nullable TeleportRequest getRequesterTeleportRequest(@NotNull Player requester);

    /**
     * Get all the teleport requests of a player as a target.
     *
     * @param target the player to check.
     * @return the teleport requests of the player as a target, an empty collection if the player has no teleport requests.
     */
    List<TeleportRequest> getTargetTeleportRequests(@NotNull Player target);

    /**
     * Get a map of all the teleport requests.
     *
     * @return a map of all the teleport requests.
     */
    Map<Player, TeleportRequest> getTeleportRequests();

    /**
     * Accept the teleport request.
     *
     * @param request the teleport request to accept.
     */
    void acceptTeleportRequest(@NotNull TeleportRequest request);

    /**
     * Deny the teleport request.
     *
     * @param request the teleport request to deny.
     */
    void denyTeleportRequest(@NotNull TeleportRequest request);
}
