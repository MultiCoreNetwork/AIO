package it.multicoredev.aio.commands.player;

import it.multicoredev.aio.AIO;
import it.multicoredev.aio.commands.PluginCommand;
import it.multicoredev.mbcore.spigot.Chat;
import it.multicoredev.mbcore.spigot.util.TabCompleterUtil;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
public class GamemodeCommand extends PluginCommand {
    private static final String CMD = "gamemode";

    public GamemodeCommand(AIO aio) {
        super(aio, CMD);
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (!super.execute(sender, label, args)) return true;

        if (args.length < 1) {
            incorrectUsage(sender);
            return true;
        }

        if (!(sender instanceof Player) && args.length < 2) {
            incorrectUsage(sender);
            return true;
        }

        Player target;

        if (isPlayer(sender)) {
            if (args.length < 2) {
                target = (Player) sender;
            } else {
                if (!hasSubPerm(sender, "other")) {
                    insufficientPerms(sender);
                    return true;
                }

                target = Bukkit.getPlayer(args[1]);
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

        GameMode gamemode;
        String gm = args[0];
        if (gm.equalsIgnoreCase("survival") || gm.equalsIgnoreCase("s") || gm.equals("0")) {
            gamemode = GameMode.SURVIVAL;
        } else if (gm.equalsIgnoreCase("creative") || gm.equalsIgnoreCase("c") || gm.equals("1")) {
            gamemode = GameMode.CREATIVE;
        } else if (gm.equalsIgnoreCase("adventure") || gm.equalsIgnoreCase("a") || gm.equals("2")) {
            gamemode = GameMode.ADVENTURE;
        } else if (gm.equalsIgnoreCase("spectator") || gm.equalsIgnoreCase("g") || gm.equals("3")) {
            gamemode = GameMode.SPECTATOR;
        } else {
            Chat.send(localization.invalidGamemode, sender);
            return true;
        }

        if (!hasPermission(sender, gamemode.name().toLowerCase(Locale.ROOT))) {
            insufficientPerms(sender);
            return true;
        }

        target.setGameMode(gamemode);

        Chat.send(localization.gamemodeSetSelf.replace("{GAMEMODE}", gamemode.name().toLowerCase(Locale.ROOT)), target);
        if (target != sender) Chat.send(localization.gamemodeSet
                .replace("{NAME}", target.getName())
                .replace("{DISPLAYNAME}", target.getDisplayName())
                .replace("{GAMEMODE}", gamemode.name().toLowerCase(Locale.ROOT)), sender);

        return true;
    }

    @Override
    public List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) {
        if (!hasCommandPerm(sender)) return new ArrayList<>();

        if (args.length == 1) {
            List<String> completions = new ArrayList<>();

            if (hasPermission(sender, GameMode.SURVIVAL.name().toLowerCase(Locale.ROOT))) {
                completions.add("survival");
                completions.add("s");
                completions.add("0");
            }
            if (hasPermission(sender, GameMode.CREATIVE.name().toLowerCase(Locale.ROOT))) {
                completions.add("creative");
                completions.add("c");
                completions.add("1");
            }
            if (hasPermission(sender, GameMode.ADVENTURE.name().toLowerCase(Locale.ROOT))) {
                completions.add("adventure");
                completions.add("a");
                completions.add("2");
            }
            if (hasPermission(sender, GameMode.SPECTATOR.name().toLowerCase(Locale.ROOT))) {
                completions.add("spectator");
                completions.add("g");
                completions.add("3");
            }

            return TabCompleterUtil.getCompletions(args[0], completions);
        } else if (args.length == 2) {
            return TabCompleterUtil.getPlayers(args[1], sender.hasPermission("pv.see"));
        }

        return new ArrayList<>();
    }

    private boolean hasPermission(CommandSender sender, String gm) {
        return hasSubPerm(sender, gm) || hasSubPerm(sender, "*");
    }
}
