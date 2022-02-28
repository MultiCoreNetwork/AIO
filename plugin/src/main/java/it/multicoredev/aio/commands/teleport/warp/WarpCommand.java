package it.multicoredev.aio.commands.teleport.warp;

import it.multicoredev.aio.AIO;
import it.multicoredev.aio.api.models.Warp;
import it.multicoredev.aio.api.tp.ITeleportManager;
import it.multicoredev.aio.commands.PluginCommand;
import it.multicoredev.aio.storage.data.WarpStorage;
import it.multicoredev.mbcore.spigot.Chat;
import it.multicoredev.mbcore.spigot.util.TabCompleterUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
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
public class WarpCommand extends PluginCommand {
    private static final String CMD = "warp";

    public WarpCommand(AIO aio) {
        super(aio, CMD);
    }

    @Override
    public boolean run(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            incorrectUsage(sender);
            return false;
        }

        Player target;

        if (isPlayer(sender)) {
            if (args.length < 2) {
                target = (Player) sender;
            } else {
                if (!hasSubPerm(sender, "other")) {
                    insufficientPerms(sender);
                    return false;
                }

                target = Bukkit.getPlayer(args[0]);
            }
        } else {
            if (args.length < 2) {
                Chat.send(pu.replacePlaceholders(localization.notPlayer), sender);
                return false;
            }

            target = Bukkit.getPlayer(args[1]);
        }

        if (target == null) {
            Chat.send(pu.replacePlaceholders(localization.playerNotFound), sender);
            return false;
        }

        String warpName = args[0];
        WarpStorage warpStorage = aio.getWarpStorage();

        if (!warpStorage.existsWarp(warpName)) {
            Chat.send(pu.replacePlaceholders(localization.warpNotFound), sender);
            return false;
        }

        if (!sender.hasPermission("aio.warp." + warpName)) {
            insufficientPerms(sender);
            return false;
        }

        Warp warp = warpStorage.getWarp(warpName);
        Location location = warp.getLocation();

        if (!warp.isGlobal()) {
            if (!sender.hasPermission("aio.warp.local.bypass")) {
                World world = location.getWorld();

                if (world == null) {
                    Chat.send(pu.replacePlaceholders(localization.warpNotFound), sender);
                    return false;
                }

                if (!Objects.equals(target.getLocation().getWorld(), world)) {
                    Chat.send(pu.replacePlaceholders(localization.warpNotGlobal, "{WORLD}", world.getName()), sender);
                    return false;
                }
            }
        }

        ITeleportManager teleportManager = aio.getTeleportManager();
        teleportManager.teleport(target, location, config.warpTeleportDelay);
        return true;
    }

    @Override
    public List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 1) return aio.getWarpStorage().getWarpNames(sender);
        else if (args.length == 2) return TabCompleterUtil.getPlayers(args[1], sender.hasPermission("pv.see"));
        else return new ArrayList<>();
    }
}
