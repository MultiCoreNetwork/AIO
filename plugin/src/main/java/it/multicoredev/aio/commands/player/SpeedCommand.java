package it.multicoredev.aio.commands.player;

import it.multicoredev.aio.AIO;
import it.multicoredev.aio.commands.PluginCommand;
import it.multicoredev.mbcore.spigot.Chat;
import it.multicoredev.mbcore.spigot.util.TabCompleterUtil;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
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
public class SpeedCommand extends PluginCommand {
    private static final String CMD = "speed";

    public SpeedCommand(AIO aio) {
        super(aio, CMD);
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (!preprocessCheck(sender)) return true;

        if (args.length < 1) {
            incorrectUsage(sender);
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        int offset = 1;

        if (target == null) {
            if (!isPlayer(sender)) {
                Chat.send(localization.playerNotFound, sender);
                return true;
            }

            target = (Player) sender;
            offset = 0;
        }

        if (isPlayer(sender) && target != sender && !hasSubPerm(sender, "other")) {
            insufficientPerms(sender);
            return true;
        }

        float speed;

        try {
            speed = Float.parseFloat(args[offset]);
        } catch (NumberFormatException ignored) {
            Chat.send(localization.invalidNumber, sender);
            return true;
        }

        if (speed < 0) speed = 0;
        if (speed > 10) speed = 10;

        boolean flying;

        if (offset == 0 && args.length > 1 || offset == 1 && args.length > 2) {
            if (args[offset + 1].equalsIgnoreCase("fly")) {
                flying = true;
            } else if (args[offset + 1].equalsIgnoreCase("walk")) {
                flying = false;
            } else {
                incorrectUsage(sender);
                return true;
            }
        } else {
            flying = target.isFlying();
        }

        if (flying) {
            target.setFlySpeed(speed / 10);
            Chat.send(localization.flySpeedSetSelf.replace("{SPEED}", String.valueOf(speed)), target);
            if (target != sender) Chat.send(localization.flySpeedSet
                    .replace("{NAME}", target.getName())
                    .replace("{DISPLAYNAME}", target.getDisplayName())
                    .replace("{SPEED}", String.valueOf(speed)), sender);
        } else {
            target.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(speed / 10);
            Chat.send(localization.walkSpeedSetSelf.replace("{SPEED}", String.valueOf(speed)), target);
            if (target != sender) Chat.send(localization.walkSpeedSet
                    .replace("{NAME}", target.getName())
                    .replace("{DISPLAYNAME}", target.getDisplayName())
                    .replace("{SPEED}", String.valueOf(speed)), sender);
        }

        return true;
    }

    @Override
    public List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) {
        if (!hasCommandPerm(sender)) return new ArrayList<>();

        if (args.length == 1) {
            if (hasSubPerm(sender, "other")) return TabCompleterUtil.getPlayers(args[0], sender.hasPermission("pv.see"));
            else return TabCompleterUtil.getCompletions(args[0], "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10");
        } else if (args.length == 2) {
            if (hasSubPerm(sender, "other")) return TabCompleterUtil.getCompletions(args[0], "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10");
            else return TabCompleterUtil.getCompletions(args[2], "fly", "walk");
        } else if (args.length == 3) {
            if (hasSubPerm(sender, "other")) return TabCompleterUtil.getCompletions(args[2], "fly", "walk");
        }

        return new ArrayList<>();
    }
}
