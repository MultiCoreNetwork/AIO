package it.multicoredev.aio.commands.utilities;

import it.multicoredev.aio.AIO;
import it.multicoredev.aio.commands.PluginCommand;
import it.multicoredev.aio.utils.Utils;
import it.multicoredev.mbcore.spigot.Chat;
import it.multicoredev.mbcore.spigot.util.TabCompleterUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Copyright © 2021 - 2022 by Lorenzo Magni
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
public class RunLaterCommand extends PluginCommand {
    private static final String CMD = "runlater";

    public RunLaterCommand(AIO aio) {
        super(aio, CMD);
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (!preprocessCheck(sender)) return true;

        if (args.length < 2) {
            incorrectUsage(sender);
            return true;
        }

        Long delay = Utils.parseTime(args[0]);
        if (delay == null) {
            incorrectUsage(sender);
            return true;
        }

        String command = Chat.builder(args, 1);

        Bukkit.getScheduler().runTaskLater(aio, () -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command), delay * 20);

        Chat.send(localization.runLaterScheduled.replace("{DELAY}", Utils.formatDelay(delay, localization)), sender);
        return true;
    }

    @Override
    public List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) {
        if (!hasCommandPerm(sender)) return new ArrayList<>();

        if (args.length == 1) {
            return TabCompleterUtil.getCompletions(args[0], "1s", "5m", "2h", "6M", "1y");
        } else if (args.length == 2) {
            return TabCompleterUtil.getCompletions(args[1], new ArrayList<>(aio.getAllCommands()));
        } else if (args.length > 2) {
            String cmd = args[1];
            org.bukkit.command.PluginCommand command = aio.getCommand(cmd);
            if (command == null) return new ArrayList<>();
            if (command.getTabCompleter() == null) return new ArrayList<>();

            List<String> completions = command.getTabCompleter().onTabComplete(Bukkit.getConsoleSender(), command, command.getName(), Arrays.copyOfRange(args, 3, args.length));
            if (completions == null) return new ArrayList<>();
            return completions;
        }

        return super.tabComplete(sender, alias, args);
    }
}
