package it.multicoredev.aio.commands.teleport;

import it.multicoredev.aio.AIO;
import it.multicoredev.aio.api.User;
import it.multicoredev.aio.commands.PluginCommand;
import it.multicoredev.mbcore.spigot.Chat;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
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
public class RTPCommand extends PluginCommand {
    private static final String CMD = "rtp";

    public RTPCommand(AIO aio) {
        super(aio, CMD);
    }

    @Override
    public boolean run(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        Player target;

        if (isPlayer(sender)) {
            if (args.length < 1) {
                target = (Player) sender;
            } else {
                target = Bukkit.getPlayer(args[0]);

                if (!hasSubPerm(sender, "other") && !sender.equals(target)) {
                    insufficientPerms(sender);
                    return false;
                }
            }
        } else {
            if (args.length < 1) {
                Chat.send(pu.replacePlaceholders(localization.notPlayer), sender);
                return false;
            }

            target = Bukkit.getPlayer(args[0]);
        }

        if (target == null) {
            Chat.send(pu.replacePlaceholders(localization.playerNotFound), sender);
            return false;
        }

        if (config.rtpSection.isWorldBlacklisted(target.getWorld())) {
            Chat.send(pu.replacePlaceholders(localization.rtpBlacklistedWorld), sender);
            return false;
        }

        if (config.rtpSection.maxRtp > 0) {
            if (!hasSubPerm(sender, "bypass-limits")) {
                User user = storage.getUser(((Player) sender).getUniqueId());
                if (user == null) {
                    Chat.send(pu.replacePlaceholders(localization.playerNotFound), sender);
                    return false;
                }

                if (user.getRTP() > config.rtpSection.maxRtp) {
                    Chat.send(pu.replacePlaceholders(localization.maxRtpExceeded), sender);
                    return false;
                }
            }
        }

        Bukkit.getScheduler().runTaskAsynchronously(aio, () -> aio.getTeleportManager().randomTeleport(
                target,
                config.rtpSection.centerX,
                config.rtpSection.centerZ,
                config.rtpSection.spreadDistance,
                config.rtpSection.maxRange,
                config.rtpSection.rtpTeleportDelay
        ));

        if (target.equals(sender)) {
            Chat.send(pu.replacePlaceholders(localization.rtpTeleportSelf), target);
        } else {
            Chat.send(pu.replacePlaceholders(localization.rtpTeleportSelf), target);
            Chat.send(pu.replacePlaceholders(
                    localization.rtpTeleport,
                    new String[]{"{NAME}", "{DISPLAYNAME}"},
                    new Object[]{target.getName(), target.getDisplayName()}
            ), sender);
        }

        return true;
    }
}
