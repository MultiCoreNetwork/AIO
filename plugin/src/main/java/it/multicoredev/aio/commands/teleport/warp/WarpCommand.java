package it.multicoredev.aio.commands.teleport.warp;

import it.multicoredev.aio.AIO;
import it.multicoredev.aio.api.tp.ITeleportManager;
import it.multicoredev.aio.commands.PluginCommand;
import it.multicoredev.aio.models.Warp;
import it.multicoredev.aio.storage.data.WarpStorage;
import it.multicoredev.mbcore.spigot.Chat;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

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
public class WarpCommand extends PluginCommand {
    private static final String CMD = "warp";

    public WarpCommand(AIO aio) {
        super(aio, CMD);
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (!preprocessCheck(sender)) return true;

        Player target;

        if (isPlayer(sender)) {
            if (args.length < 2) {
                target = (Player) sender;
            } else {
                if (!hasSubPerm(sender, "other")) {
                    insufficientPerms(sender);
                    return true;
                }

                target = Bukkit.getPlayer(args[0]);
            }
        } else {
            if (args.length < 2) {
                Chat.send(localization.notPlayer, sender);
                return true;
            }

            target = Bukkit.getPlayer(args[1]);
        }

        if (target == null) {
            Chat.send(localization.playerNotFound, sender);
            return true;
        }

        String warpName = args[0];
        WarpStorage warpStorage = aio.getWarpStorage();

        if (!warpStorage.existsWarp(warpName)) {
            Chat.send(localization.warpNotFound, sender);
            return true;
        }

        if (!sender.hasPermission("aio.warp." + warpName)) {
            insufficientPerms(sender);
            return true;
        }

        Warp warp = warpStorage.getWarp(warpName);
        Location location = warp.getLocation();

        if (!warp.isGlobal()) {
            if (!sender.hasPermission("aio.warp.local.bypass")) {
                World world = location.getWorld();

                if (world == null) {
                    Chat.send(localization.warpNotFound, sender);
                    return true;
                }

                if (!target.getLocation().getWorld().equals(world)) {
                    Chat.send(localization.warpNotGlobal.replace("{WORLD}", world.getName()), sender);
                    return true;
                }
            }
        }

        ITeleportManager teleportManager = aio.getTeleportManager();
        teleportManager.teleport(target, location, config.warpTeleportDelay);
        return true;
    }

    @Override
    public List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) {
        return aio.getWarpStorage().warpList(sender);
    }
}
