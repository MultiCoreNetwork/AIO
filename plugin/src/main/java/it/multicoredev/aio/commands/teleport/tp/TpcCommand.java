package it.multicoredev.aio.commands.teleport.tp;

import it.multicoredev.aio.AIO;
import it.multicoredev.aio.api.tp.ITeleportManager;
import it.multicoredev.aio.api.tp.TeleportRequest;
import it.multicoredev.aio.commands.PluginCommand;
import it.multicoredev.mbcore.spigot.Chat;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

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
public class TpcCommand extends PluginCommand {
    private static final String CMD = "tpc";

    public TpcCommand(AIO aio) {
        super(aio, CMD);
    }

    @Override
    public boolean run(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (!isPlayer(sender)) {
            Chat.send(pu.replacePlaceholders(localization.notPlayer), sender);
            return false;
        }

        Player requester = (Player) sender;

        ITeleportManager teleportManager = aio.getTeleportManager();

        if (!teleportManager.hasRequesterTeleportRequest(requester)) {
            Chat.send(pu.replacePlaceholders(localization.noTpRequest), requester);
            return false;
        }

        Player target = Objects.requireNonNull(teleportManager.getRequesterTeleportRequest(requester)).getTarget();

        teleportManager.cancelTeleportRequest(
                requester,
                TeleportRequest.CancelReason.CANCELLED,
                pu.replacePlaceholders(
                        localization.tpRequestCancelledRequester,
                        new String[]{"{REQUESTER_NAME}", "{REQUESTER_DISPLAYNAME}", "{TARGET_NAME}", "{TARGET_DISPLAYNAME}"},
                        new Object[]{requester.getName(), requester.getDisplayName(), target.getName(), target.getDisplayName()}),
                pu.replacePlaceholders(
                        localization.tpRequestCancelledTarget,
                        new String[]{"{REQUESTER_NAME}", "{REQUESTER_DISPLAYNAME}", "{TARGET_NAME}", "{TARGET_DISPLAYNAME}"},
                        new Object[]{requester.getName(), requester.getDisplayName(), target.getName(), target.getDisplayName()})
        );

        return true;
    }
}
