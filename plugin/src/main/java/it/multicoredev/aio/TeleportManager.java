package it.multicoredev.aio;

import com.google.common.base.Preconditions;
import it.multicoredev.aio.api.events.teleport.*;
import it.multicoredev.aio.api.tp.ITeleportManager;
import it.multicoredev.aio.api.tp.Teleport;
import it.multicoredev.aio.api.tp.TeleportRequest;
import it.multicoredev.aio.storage.config.modules.EconomyModule;
import it.multicoredev.aio.utils.Utils;
import it.multicoredev.mbcore.spigot.Chat;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

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
    private final AIO aio;
    private final EconomyModule economyModule;
    private final Map<Player, Teleport> pendingTeleports = new LinkedHashMap<>();
    private final Map<Player, TeleportRequest> teleportRequests = new LinkedHashMap<>();
    private final Map<Teleport, BukkitTask> runningTeleports = new LinkedHashMap<>();

    public TeleportManager(AIO aio) {
        this.aio = aio;
        this.economyModule = aio.getModuleManager().getModule(AIO.ECONOMY_MODULE);
    }

    private void teleportNow(@NotNull Teleport tp) {
        pendingTeleports.remove(tp.getPlayer());

        if (!tp.getPlayer().isOnline()) {
            cancelTp(tp, Teleport.CancelReason.NOT_ONLINE, null);
            return;
        }

        PlayerPreTeleportEvent preTpEvent = new PlayerPreTeleportEvent(tp);
        Bukkit.getPluginManager().callEvent(preTpEvent);

        if (preTpEvent.isCancelled()) {
            cancelTp(tp, preTpEvent.getCancelReason(), preTpEvent.getCancelMessage());
            return;
        }

        tp.getPlayer().teleport(tp.getTo());
        runningTeleports.remove(tp);

        PlayerPostTeleportEvent postTpEvent = new PlayerPostTeleportEvent(tp);
        Bukkit.getPluginManager().callEvent(postTpEvent);

        if (postTpEvent.getPostMessage() != null) {
            Chat.send(aio.getPlaceholdersUtils().replacePlaceholders(postTpEvent.getPostMessage()), tp.getPlayer());
        }
    }

    private void cancelTp(@NotNull Teleport tp, Teleport.CancelReason reason, String cancelMessage) {
        pendingTeleports.remove(tp.getPlayer());
        runningTeleports.remove(tp);

        if (runningTeleports.containsKey(tp)) {
            BukkitTask task = runningTeleports.get(tp);
            runningTeleports.remove(tp);

            if (!task.isCancelled()) task.cancel();
        }

        PlayerTeleportCancelledEvent tpCancEvent = new PlayerTeleportCancelledEvent(tp, reason, cancelMessage);
        Bukkit.getPluginManager().callEvent(tpCancEvent);

        if (tpCancEvent.getCancelMessage() != null) {
            Chat.send(aio.getPlaceholdersUtils().replacePlaceholders(tpCancEvent.getCancelMessage()), tp.getPlayer());
        }
    }

    private void cancelRequest(@NotNull TeleportRequest request, @NotNull TeleportRequest.CancelReason reason, String cancelMessageRequester, String cancelMessageTarget) {
        teleportRequests.remove(request.getRequester());

        PlayerTeleportRequestCancelledEvent tpReqCancEvent = new PlayerTeleportRequestCancelledEvent(request, reason, cancelMessageRequester, cancelMessageTarget);
        Bukkit.getPluginManager().callEvent(tpReqCancEvent);

        if (tpReqCancEvent.getCancelMessageRequester() != null) {
            Chat.send(aio.getPlaceholdersUtils().replacePlaceholders(tpReqCancEvent.getCancelMessageRequester()), request.getRequester());
        }

        if (tpReqCancEvent.getCancelMessageTarget() != null) {
            Chat.send(aio.getPlaceholdersUtils().replacePlaceholders(tpReqCancEvent.getCancelMessageTarget()), request.getTarget());
        }
    }

    @Override
    public void teleport(@NotNull Teleport teleport) {
        Preconditions.checkNotNull(teleport);

        pendingTeleports.put(teleport.getPlayer(), teleport);

        PlayerPendingTeleportEvent tpReqEvent = new PlayerPendingTeleportEvent(teleport);
        Bukkit.getPluginManager().callEvent(tpReqEvent);

        if (tpReqEvent.isCancelled()) {
            cancelTp(teleport, tpReqEvent.getCancelReason(), tpReqEvent.getCancelMessage());
            return;
        }

        if (tpReqEvent.getPendingMessage() != null && tpReqEvent.getTimer() > 0) {
            Chat.send(aio.getPlaceholdersUtils().replacePlaceholders(tpReqEvent.getPendingMessage()), teleport.getPlayer());
        }

        if (teleport.getTimer() <= 0) {
            teleportNow(teleport);
        } else {
            if (teleport.getPlayer().hasPermission("aio.teleport.instant")) teleportNow(teleport);
            else runningTeleports.put(teleport, Bukkit.getScheduler().runTaskLater(aio, () -> teleportNow(teleport), teleport.getTimer() * 20));
        }
    }

    @Override
    public void teleport(@NotNull Player player, @NotNull Location to, long timer, String pendingMessage, String postMessage) {
        Preconditions.checkNotNull(player);
        Preconditions.checkNotNull(to);

        Teleport tp = new Teleport(player, to, timer, pendingMessage, postMessage);
        teleport(tp);
    }

    @Override
    public void teleport(@NotNull Player player, @NotNull Location to, long timer, String postMessage) {
        teleport(player, to, timer, null, postMessage);
    }

    @Override
    public void teleport(@NotNull Player player, @NotNull Location to, long timer) {
        teleport(player, to, timer, null, null);
    }

    @Override
    public void teleport(@NotNull Player player, @NotNull Location to, String postMessage) {
        teleport(player, to, 0, null, postMessage);
    }

    @Override
    public void teleport(@NotNull Player player, @NotNull Location to) {
        teleport(player, to, 0, null, null);
    }

    @Override
    public void randomTeleport(@NotNull Player player, @NotNull Location center, int minDistance, int maxDistance, long timer, String pendingMessage, String postMessage) {
        Preconditions.checkNotNull(player);
        Preconditions.checkNotNull(center);

        Location to = Utils.getRandomLocation(center, minDistance, maxDistance);
        if (to == null) return;

        Bukkit.getScheduler().callSyncMethod(aio, () -> {
            teleport(player, to, timer, pendingMessage, postMessage);
            return true;
        });
    }

    @Override
    public void randomTeleport(@NotNull Player player, @NotNull Location center, int minDistance, int maxDistance, long timer, String postMessage) {
        randomTeleport(player, center, minDistance, maxDistance, timer, null, postMessage);
    }

    @Override
    public void randomTeleport(@NotNull Player player, @NotNull Location center, int minDistance, int maxDistance, long timer) {
        randomTeleport(player, center, minDistance, maxDistance, timer, null, null);
    }

    @Override
    public void randomTeleport(@NotNull Player player, @NotNull Location center, int minDistance, int maxDistance, String postMessage) {
        randomTeleport(player, center, minDistance, maxDistance, 0, null, postMessage);
    }

    @Override
    public void randomTeleport(@NotNull Player player, @NotNull Location center, int minDistance, int maxDistance) {
        randomTeleport(player, center, minDistance, maxDistance, 0, null, null);
    }

    @Override
    public void randomTeleport(@NotNull Player player, @NotNull World world, double xCenter, double zCenter, int minDistance, int maxDistance, long timer, String pendingMessage, String postMessage) {
        Preconditions.checkNotNull(player);
        Preconditions.checkNotNull(world);

        Location center = new Location(world, xCenter, 0, zCenter);
        randomTeleport(player, center, minDistance, maxDistance, timer, pendingMessage, postMessage);
    }

    @Override
    public void randomTeleport(@NotNull Player player, @NotNull World world, double xCenter, double zCenter, int minDistance, int maxDistance, long timer, String postMessage) {
        randomTeleport(player, world, xCenter, zCenter, minDistance, maxDistance, timer, null, postMessage);
    }

    @Override
    public void randomTeleport(@NotNull Player player, @NotNull World world, double xCenter, double zCenter, int minDistance, int maxDistance, long timer) {
        randomTeleport(player, world, xCenter, zCenter, minDistance, maxDistance, timer, null, null);
    }

    @Override
    public void randomTeleport(@NotNull Player player, @NotNull World world, double xCenter, double zCenter, int minDistance, int maxDistance, String postMessage) {
        randomTeleport(player, world, xCenter, zCenter, minDistance, maxDistance, 0, null, postMessage);
    }

    @Override
    public void randomTeleport(@NotNull Player player, @NotNull World world, double xCenter, double zCenter, int minDistance, int maxDistance) {
        randomTeleport(player, world, xCenter, zCenter, minDistance, maxDistance, 0, null, null);
    }

    @Override
    public void randomTeleport(@NotNull Player player, @NotNull String world, double xCenter, double zCenter, int minDistance, int maxDistance, long timer, String pendingMessage, String postMessage) {
        randomTeleport(player, Objects.requireNonNull(Bukkit.getWorld(world)), xCenter, zCenter, minDistance, maxDistance, timer, pendingMessage, postMessage);
    }

    @Override
    public void randomTeleport(@NotNull Player player, @NotNull String world, double xCenter, double zCenter, int minDistance, int maxDistance, long timer, String postMessage) {
        randomTeleport(player, Objects.requireNonNull(Bukkit.getWorld(world)), xCenter, zCenter, minDistance, maxDistance, timer, null, postMessage);
    }

    @Override
    public void randomTeleport(@NotNull Player player, @NotNull String world, double xCenter, double zCenter, int minDistance, int maxDistance, long timer) {
        randomTeleport(player, Objects.requireNonNull(Bukkit.getWorld(world)), xCenter, zCenter, minDistance, maxDistance, timer, null, null);
    }

    @Override
    public void randomTeleport(@NotNull Player player, @NotNull String world, double xCenter, double zCenter, int minDistance, int maxDistance, String postMessage) {
        randomTeleport(player, Objects.requireNonNull(Bukkit.getWorld(world)), xCenter, zCenter, minDistance, maxDistance, 0, null, postMessage);
    }

    @Override
    public void randomTeleport(@NotNull Player player, @NotNull String world, double xCenter, double zCenter, int minDistance, int maxDistance) {
        randomTeleport(player, Objects.requireNonNull(Bukkit.getWorld(world)), xCenter, zCenter, minDistance, maxDistance, 0, null, null);
    }

    @Override
    public void randomTeleport(@NotNull Player player, double xCenter, double zCenter, int minDistance, int maxDistance, long timer, String pendingMessage, String postMessage) {
        randomTeleport(player, player.getWorld(), xCenter, zCenter, minDistance, maxDistance, timer, pendingMessage, postMessage);
    }

    @Override
    public void randomTeleport(@NotNull Player player, double xCenter, double zCenter, int minDistance, int maxDistance, long timer, String postMessage) {
        randomTeleport(player, player.getWorld(), xCenter, zCenter, minDistance, maxDistance, timer, null, postMessage);
    }

    @Override
    public void randomTeleport(@NotNull Player player, double xCenter, double zCenter, int minDistance, int maxDistance, long timer) {
        randomTeleport(player, player.getWorld(), xCenter, zCenter, minDistance, maxDistance, timer, null, null);
    }

    @Override
    public void randomTeleport(@NotNull Player player, double xCenter, double zCenter, int minDistance, int maxDistance, String postMessage) {
        randomTeleport(player, player.getWorld(), xCenter, zCenter, minDistance, maxDistance, 0, null, postMessage);
    }

    @Override
    public void randomTeleport(@NotNull Player player, double xCenter, double zCenter, int minDistance, int maxDistance) {
        randomTeleport(player, player.getWorld(), xCenter, zCenter, minDistance, maxDistance, 0, null, null);
    }

    @Override
    public void randomTeleport(@NotNull Player player, int minDistance, int maxDistance, long timer, String pendingMessage, String postMessage) {
        randomTeleport(player, player.getLocation(), minDistance, maxDistance, timer, pendingMessage, postMessage);
    }

    @Override
    public void randomTeleport(@NotNull Player player, int minDistance, int maxDistance, long timer, String postMessage) {
        randomTeleport(player, player.getLocation(), minDistance, maxDistance, timer, null, postMessage);
    }

    @Override
    public void randomTeleport(@NotNull Player player, int minDistance, int maxDistance, long timer) {
        randomTeleport(player, player.getLocation(), minDistance, maxDistance, timer);
    }

    @Override
    public void randomTeleport(@NotNull Player player, int minDistance, int maxDistance, String postMessage) {
        randomTeleport(player, player.getLocation(), minDistance, maxDistance, 0, null, postMessage);
    }

    @Override
    public void randomTeleport(@NotNull Player player, int minDistance, int maxDistance) {
        randomTeleport(player, player.getLocation(), minDistance, maxDistance, 0);
    }

    @Override
    public void cancelTeleport(@NotNull Teleport teleport, @NotNull Teleport.CancelReason reason, String message) {
        Preconditions.checkNotNull(teleport);
        Preconditions.checkNotNull(reason);

        cancelTp(teleport, reason, message);
    }

    @Override
    public void cancelTeleport(@NotNull Teleport teleport, @NotNull Teleport.CancelReason reason) {
        Preconditions.checkNotNull(teleport);
        Preconditions.checkNotNull(reason);

        cancelTp(teleport, reason, null);
    }

    @Override
    public void cancelTeleport(@NotNull Player player, @NotNull Teleport.CancelReason reason, String message) {
        Preconditions.checkNotNull(player);
        Preconditions.checkNotNull(reason);

        if (hasPendingTeleport(player)) cancelTp(Objects.requireNonNull(getPendingTeleport(player)), reason, message);
    }

    @Override
    public void cancelTeleport(@NotNull Player player, @NotNull Teleport.CancelReason reason) {
        Preconditions.checkNotNull(player);
        Preconditions.checkNotNull(reason);

        cancelTeleport(player, reason, null);
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

    @Override
    public void requestTeleport(@NotNull TeleportRequest request) {
        Preconditions.checkNotNull(request);

        teleportRequests.put(request.getRequester(), request);

        PlayerTeleportRequestEvent tpReqEvent = new PlayerTeleportRequestEvent(request);
        Bukkit.getPluginManager().callEvent(tpReqEvent);

        if (tpReqEvent.isCancelled()) {
            cancelRequest(request, tpReqEvent.getCancelReason(), tpReqEvent.getCancelMessageRequester(), tpReqEvent.getCancelMessageTarget());
            return;
        }

        if (request.getRequesterMessage() != null) {
            Chat.send(aio.getPlaceholdersUtils().replacePlaceholders(request.getRequesterMessage()), request.getRequester());
        }
        if (request.getTargetMessage() != null) {
            Chat.send(aio.getPlaceholdersUtils().replacePlaceholders(request.getTargetMessage()), request.getTarget());
        }
    }

    @Override
    public void requestTeleport(@NotNull Player requester, @NotNull Player target, TeleportRequest.@NotNull RequestType type, long timeout, String requesterMessage, String targetMessage) {
        requestTeleport(new TeleportRequest(requester, target, type, timeout, requesterMessage, targetMessage));
    }

    @Override
    public void requestTeleport(@NotNull Player requester, @NotNull Player target, TeleportRequest.@NotNull RequestType type, long timeout) {
        requestTeleport(requester, target, type, timeout, null, null);
    }

    @Override
    public void cancelTeleportRequest(@NotNull TeleportRequest request, TeleportRequest.@NotNull CancelReason reason, String cancelMessageRequester, String cancelMessageTarget) {
        Preconditions.checkNotNull(request);
        Preconditions.checkNotNull(reason);

        cancelRequest(request, reason, cancelMessageRequester, cancelMessageTarget);
    }

    @Override
    public void cancelTeleportRequest(@NotNull TeleportRequest request, TeleportRequest.@NotNull CancelReason reason) {
        cancelRequest(request, reason, null, null);
    }

    @Override
    public void cancelTeleportRequest(@NotNull Player requester, TeleportRequest.@NotNull CancelReason reason, String cancelMessageRequester, String cancelMessageTarget) {
        Preconditions.checkNotNull(requester);
        Preconditions.checkNotNull(reason);

        if (hasRequesterTeleportRequest(requester))
            cancelRequest(Objects.requireNonNull(getRequesterTeleportRequest(requester)), reason, cancelMessageRequester, cancelMessageTarget);
    }

    @Override
    public void cancelTeleportRequest(@NotNull Player requester, TeleportRequest.@NotNull CancelReason reason) {
        cancelTeleportRequest(requester, reason, null, null);
    }

    @Override
    public boolean hasRequesterTeleportRequest(@NotNull Player requester) {
        return teleportRequests.containsKey(requester);
    }

    @Override
    public boolean hasTargetTeleportRequest(@NotNull Player target) {
        return teleportRequests.values().stream().anyMatch(r -> r.getTarget().equals(target));
    }

    @Override
    public @Nullable TeleportRequest getRequesterTeleportRequest(@NotNull Player requester) {
        return teleportRequests.get(requester);
    }

    @Override
    public List<TeleportRequest> getTargetTeleportRequests(@NotNull Player target) {
        return teleportRequests.values().stream().filter(r -> r.getTarget().equals(target)).collect(Collectors.toList());
    }

    @Override
    public Map<Player, TeleportRequest> getTeleportRequests() {
        return Collections.unmodifiableMap(teleportRequests);
    }

    /*@Override
    public void executeRequest(@NotNull TeleportRequest request, boolean accept) {
        teleportRequests.remove(request);

        Localization localization = aio.getLocalization();
        IPlaceholdersUtils pu = aio.getPlaceholdersUtils();
        IEconomy economy = aio.getEconomy();

        Player requester = request.getRequester();
        Player target = request.getTarget();

        if (accept) {
            long delay = aio.getConfiguration().teleportRequestDelay;
            String seconds = String.valueOf(delay / 20);
            TeleportRequestType type = request.getType();
            double cost = economyModconfig.commandCosts.getCommandCost(type.name().toLowerCase(Locale.ROOT)) / 2f;

            if (config.commandCosts.costsEnabled && VAULT && config.commandCosts.hasCommandCost(type.name().toLowerCase(Locale.ROOT)) && !requester.hasPermission("aio.bypass.costs")) {
                if (!economy.has(requester, cost / 2)) {
                    Chat.send(localization.insufficientCmdMoney, requester);
                    return;
                }

                economy.withdrawPlayer(requester, cost);
            }

            if (type == TeleportRequestType.TPA) {
                Chat.send(pu.replacePlaceholders(localization.teleportRequestAccepted, new String[]{"{NAME}", "{DISPLAYNAME}", "{SECONDS}"}, new Object[]{target.getName(), target.getDisplayName(), seconds}), target);
                Chat.send(pu.replacePlaceholders(localization.targetAcceptedRequest + " " + localization.stayStillFor, new String[]{"{NAME}", "{DISPLAYNAME}", "{SECONDS}"}, new Object[]{target.getName(), target.getDisplayName(), seconds}), requester);
                teleport(requester, target.getLocation(), delay);
            } else if (type == TeleportRequestType.TPAHERE) {
                Chat.send(pu.replacePlaceholders(localization.teleportRequestAccepted + " " + localization.stayStillFor, new String[]{"{NAME}", "{DISPLAYNAME}", "{SECONDS}"}, new Object[]{target.getName(), target.getDisplayName(), seconds}), target);
                Chat.send(pu.replacePlaceholders(localization.targetAcceptedRequest, new String[]{"{NAME}", "{DISPLAYNAME}", "{SECONDS}"}, new Object[]{target.getName(), target.getDisplayName(), seconds}), requester);
                teleport(target, requester.getLocation(), delay);
            }

        } else {
            Chat.send(localization.teleportRequestRejected, target);
            Chat.send(pu.replacePlaceholders(localization.targetRejectedRequest, new String[]{"{NAME}", "{DISPLAYNAME}"}, new Object[]{target.getName(), target.getDisplayName()}), requester);
        }
    }*/
}
