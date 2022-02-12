package it.multicoredev.aio;

import com.google.common.base.Preconditions;
import it.multicoredev.aio.api.tp.ITeleportManager;
import it.multicoredev.aio.api.tp.Teleport;
import it.multicoredev.aio.api.tp.TeleportRequest;
import it.multicoredev.aio.api.tp.TeleportRequestType;
import it.multicoredev.aio.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

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
public class TeleportManager implements ITeleportManager {
    private final Map<Player, Teleport> pendingTeleports = new HashMap<>();
    private final Map<TeleportRequest, Date> teleportRequests = new HashMap<>();

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

        Bukkit.getScheduler().callSyncMethod(AIO.getInstance(), () -> {
            teleport(player, to, timer);
            return true;
        });
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

    @Override
    public void requestTeleport(@NotNull TeleportRequest request) {
        Preconditions.checkNotNull(request);

        TeleportRequest previousRequest = getRequesterTeleportRequest(request.getRequester());
        if (previousRequest != null) {
            cancelTeleportRequest(previousRequest); //TODO notify
        } else {
            if (previousRequest.getTarget().equals(request.getTarget()) && previousRequest.getType().equals(request.getType())) return;

            teleportRequests.put(request, new Date());
        }
    }

    @Override
    public void requestTeleport(@NotNull TeleportRequestType type, @NotNull Player requester, @NotNull Player target) {
        Preconditions.checkNotNull(type);
        Preconditions.checkNotNull(requester);
        Preconditions.checkNotNull(target);

        TeleportRequest previousRequest = getRequesterTeleportRequest(requester);
        if (previousRequest != null) {
            cancelTeleportRequest(previousRequest); //TODO notify
        } else {
            if (previousRequest.getTarget().equals(target) && previousRequest.getType().equals(type)) return;

            teleportRequests.put(new TeleportRequest(type, requester, target), new Date());
        }
    }

    @Override
    public void cancelTeleportRequest(@NotNull TeleportRequest request, String reason, boolean notify) {
        Preconditions.checkNotNull(request);

        teleportRequests.remove(request);
        //TODO notify
    }

    @Override
    public void cancelTeleportRequest(@NotNull TeleportRequest request, String reason) {
        Preconditions.checkNotNull(request);

        teleportRequests.remove(request);
        //TODO notify
    }

    @Override
    public void cancelTeleportRequest(@NotNull TeleportRequest request, boolean notify) {
        Preconditions.checkNotNull(request);

        teleportRequests.remove(request);
        //TODO notify
    }

    @Override
    public void cancelTeleportRequest(@NotNull TeleportRequest request) {
        Preconditions.checkNotNull(request);

        teleportRequests.remove(request);
        //TODO notify
    }

    @Override
    public void cancelTeleportRequest(@NotNull Player requester, String reason, boolean notify) {
        Preconditions.checkNotNull(requester);

        TeleportRequest request = getRequesterTeleportRequest(requester);
        if (request != null) {
            teleportRequests.remove(request);
            //TODO notify
        }
    }

    @Override
    public void cancelTeleportRequest(@NotNull Player requester, String reason) {
        Preconditions.checkNotNull(requester);

        TeleportRequest request = getRequesterTeleportRequest(requester);
        if (request != null) {
            teleportRequests.remove(request);
            //TODO notify
        }
    }

    @Override
    public void cancelTeleportRequest(@NotNull Player requester, boolean notify) {
        Preconditions.checkNotNull(requester);

        TeleportRequest request = getRequesterTeleportRequest(requester);
        if (request != null) {
            teleportRequests.remove(request);
            //TODO notify
        }
    }

    @Override
    public void cancelTeleportRequest(@NotNull Player requester) {
        Preconditions.checkNotNull(requester);

        TeleportRequest request = getRequesterTeleportRequest(requester);
        if (request != null) {
            teleportRequests.remove(request);
            //TODO notify
        }
    }

    @Override
    public boolean hasRequesterTeleportRequest(@NotNull Player requester) {
        return getRequesterTeleportRequest(requester) != null;
    }

    @Override
    public boolean hasTargetTeleportRequest(@NotNull Player target) {
        return !getTargetTeleportRequests(target).isEmpty();
    }

    @Override
    public @Nullable TeleportRequest getRequesterTeleportRequest(@NotNull Player requester) {
        Preconditions.checkNotNull(requester);

        for (TeleportRequest request : teleportRequests.keySet()) {
            if (request.getRequester().equals(requester)) {
                return request;
            }
        }

        return null;
    }

    @Override
    public List<TeleportRequest> getTargetTeleportRequests(@NotNull Player target) {
        Preconditions.checkNotNull(target);

        List<TeleportRequest> requests = new ArrayList<>();

        for (TeleportRequest request : teleportRequests.keySet()) {
            if (request.getTarget().equals(target)) {
                requests.add(request);
            }
        }

        return requests;
    }

    @Override
    public Map<TeleportRequest, Date> getTeleportRequests() {
        return Collections.unmodifiableMap(teleportRequests);
    }

    @Override
    public @Nullable Date getTeleportRequestTimestamp(@NotNull TeleportRequest request) {
        Preconditions.checkNotNull(request);

        return teleportRequests.get(request);
    }
}
