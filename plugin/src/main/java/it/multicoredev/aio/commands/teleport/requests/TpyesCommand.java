package it.multicoredev.aio.commands.teleport.requests;

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
public class TpyesCommand extends PluginCommand {
    private static final String CMD = "tpyes";

    public TpyesCommand(AIO aio) {
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
            Chat.warning("&6tpyes command returned a null request.");
            return false;
        }

        teleportManager.acceptTeleportRequest(request);
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
