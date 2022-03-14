package it.multicoredev.aio.commands.teleport.tp;

import it.multicoredev.aio.AIO;
import it.multicoredev.aio.api.tp.ITeleportManager;
import it.multicoredev.aio.api.tp.TeleportRequest;
import it.multicoredev.aio.commands.PluginCommand;
import it.multicoredev.mbcore.spigot.Chat;
import it.multicoredev.mbcore.spigot.util.TabCompleterUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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
        ITeleportManager teleportManager = aio.getTeleportManager();

        if (!teleportManager.hasTargetTeleportRequest(target)) {
            Chat.send(pu.replacePlaceholders(localization.noTpRequest), target);
            return false;
        }

        TeleportRequest request;
        if (args.length > 0) {
            Player requester = Bukkit.getPlayer(args[0]);
            if (requester == null) {
                Chat.send(pu.replacePlaceholders(localization.noTpRequestFound), target);
                return false;
            }

            request = teleportManager.getRequesterTeleportRequest(requester);
            if (request == null || !request.getTarget().equals(target)) {
                Chat.send(pu.replacePlaceholders(localization.noTpRequestFound), target);
                return false;
            }
        } else {
            List<TeleportRequest> requests = teleportManager.getTargetTeleportRequests(target);
            Collections.sort(requests);
            request = requests.get(0);
        }

        if (request == null) {
            Chat.send(pu.replacePlaceholders(localization.commandException), target);
            Chat.warning("&6tpno command returned a null request.");
            return false;
        }

        teleportManager.denyTeleportRequest(request);
        return true;
    }

    @Override
    public List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) {
        if (isPlayer(sender) && hasCommandPerm(sender) && args.length == 1) {
            return TabCompleterUtil.getCompletions(
                    args[0],
                    aio.getTeleportManager()
                            .getTargetTeleportRequests((Player) sender)
                            .stream()
                            .map(r -> r.getRequester().getName())
                            .collect(Collectors.toList()));
        }

        return new ArrayList<>();
    }
}
