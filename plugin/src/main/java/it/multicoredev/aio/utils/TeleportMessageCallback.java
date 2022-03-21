package it.multicoredev.aio.utils;

import it.multicoredev.aio.AIO;
import it.multicoredev.aio.api.tp.Teleport;
import it.multicoredev.aio.api.utils.ITeleportCallback;
import it.multicoredev.mbcore.spigot.Chat;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Copyright © 2022 by Lorenzo Magni
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
public class TeleportMessageCallback implements ITeleportCallback {
    private final AIO aio;
    private final CommandSender sender;
    private final String message;

    public TeleportMessageCallback(AIO aio, CommandSender sender, String message) {
        this.aio = aio;
        this.sender = sender;
        this.message = message;
    }

    @Override
    public void call(@NotNull Teleport teleport, boolean success, Teleport.CancelReason cancelReason) {
        if ((sender instanceof Player) && !((Player) sender).isOnline()) return;

        Chat.send(aio.getPlaceholdersUtils().replacePlaceholders(
                message,
                new String[]{"{NAME}", "{DISPLAYNAME}"},
                new Object[]{teleport.getPlayer().getName(), teleport.getPlayer().getDisplayName()}
        ), sender);
    }
}
