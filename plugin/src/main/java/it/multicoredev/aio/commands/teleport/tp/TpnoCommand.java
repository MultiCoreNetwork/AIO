package it.multicoredev.aio.commands.teleport.tp;

import it.multicoredev.aio.AIO;
import it.multicoredev.aio.api.tp.ITeleportManager;
import it.multicoredev.aio.api.tp.TeleportRequest;
import it.multicoredev.aio.api.utils.IPlaceholdersUtils;
import it.multicoredev.aio.commands.PluginCommand;
import it.multicoredev.mbcore.spigot.Chat;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright © 2022 by Daniele Patella. All rights reserved.
 * This file is part of AIO.
 * <p>
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING,
 * BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY,
 * OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */
public class TpnoCommand extends PluginCommand {
    private static final String CMD = "tpno";

    public TpnoCommand(AIO aio) {
        super(aio, CMD);
    }

    @Override
    public boolean run(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (!isPlayer(sender)) {
            Chat.send(pu.replacePlaceholders(localization.notPlayer), sender);
            return false;
        }

        Player target = (Player) sender;

        if (args.length > 1) {
            incorrectUsage(sender);
            return false;
        }

        ITeleportManager teleportManager = aio.getTeleportManager();
        IPlaceholdersUtils pu = aio.getPlaceholdersUtils();

        if (!teleportManager.hasTargetTeleportRequest(target)) {
            Chat.send(pu.replacePlaceholders(localization.noPendingTeleportRequest), target);
            return false;
        }

        TeleportRequest request;
        Player requester;

        if (args.length == 0) {
            List<TeleportRequest> targetList = teleportManager.getTargetTeleportRequests(target);
            request = targetList.get(targetList.size() - 1);
        } else {
            requester = Bukkit.getPlayer(args[0]);

            if (requester == null) {
                Chat.send(pu.replacePlaceholders(localization.playerNotFound), target);
                return false;
            }

            if (requester.getUniqueId() == target.getUniqueId()) {
                Chat.send(localization.noRequestFromYourSelf, sender);
                return true;
            }

            TeleportRequest requesterRequest = teleportManager.getRequesterTeleportRequest(requester);

            if (requesterRequest == null || requesterRequest.getTarget() != target) {
                Chat.send(pu.replacePlaceholders(localization.teleportRequestNotFound), target);
                return false;
            }

            request = requesterRequest;
        }

        //TODO Implement Economy Chance
        teleportManager.executeRequest(request, false);
        return true;
    }

    @Override
    public List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) {
        if (hasCommandPerm(sender) && args.length == 1) {
            ITeleportManager teleportManager = aio.getTeleportManager();
            return teleportManager.getRequesterNames((Player) sender);
        }

        return new ArrayList<>();
    }
}
