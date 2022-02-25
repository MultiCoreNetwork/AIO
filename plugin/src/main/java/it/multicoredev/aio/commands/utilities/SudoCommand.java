package it.multicoredev.aio.commands.utilities;

import it.multicoredev.aio.AIO;
import it.multicoredev.aio.CommandRegistry;
import it.multicoredev.aio.commands.PluginCommand;
import it.multicoredev.mbcore.spigot.Chat;
import it.multicoredev.mbcore.spigot.util.TabCompleterUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

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
public class SudoCommand extends PluginCommand {
    private static final String CMD = "sudo";

    public SudoCommand(AIO aio) {
        super(aio, CMD);
    }

    @Override
    public boolean run(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (args.length < 2) {
            incorrectUsage(sender);
            return false;
        }

        List<CommandSender> targets = new ArrayList<>(parsePlayers(sender, args[0]));
        if (targets.isEmpty()) {
            if (args[0].equalsIgnoreCase("console")) {
                if (!hasSubPerm(sender, "console")) {
                    insufficientPerms(sender);
                    return false;
                }

                targets.add(Bukkit.getConsoleSender());
            } else {
                Chat.send(placeholdersUtils.replacePlaceholders(localization.playerNotFound), sender);
                return false;
            }
        }

        targets.removeIf(target -> isPlayer(target) && hasSubPerm(target, "prevent"));

        String input = Chat.builder(args, 1);
        if (input.startsWith("/")) {
            targets.forEach(target -> Bukkit.dispatchCommand(target, input.substring(1)));
        } else {
            boolean success = false;
            for (CommandSender target : targets) {
                if (isPlayer(target)) {
                    ((Player) target).chat(input);
                    success = true;
                } else {
                    Chat.send(placeholdersUtils.replacePlaceholders(
                            localization.sudoFailed,
                            new String[]{"{NAME}", "{DISPLAYNAME}"},
                            new Object[]{target.getName(), isPlayer(target) ? target.getName() : ((Player) target).getDisplayName()}
                    ), target);
                }
            }

            if (!success) {
                return false;
            }
        }

        Chat.send(placeholdersUtils.replacePlaceholders(localization.sudoSuccess), sender);
        
        return true;
    }

    @Override
    public List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) {
        if (!hasCommandPerm(sender)) return new ArrayList<>();

        if (args.length == 1) {
            return TabCompleterUtil.getPlayers(args[0], sender.hasPermission("pv.see"));
        } else if (args.length == 2) {
            String arg = args[1];
            if (arg.startsWith("/")) {
                List<String> completions = TabCompleterUtil.getCompletions(arg.substring(1), ((CommandRegistry) aio.getCommandRegistry()).getAllCommandNames());
                return getFormattedCompletions(completions);
            }
        } else if (args.length > 2 && args[1].startsWith("/")) {
            org.bukkit.command.PluginCommand cmd = aio.getCommand(args[1].substring(1));
            if (cmd == null) return new ArrayList<>();
            if (cmd.getTabCompleter() == null) return new ArrayList<>();

            List<String> completions = cmd.getTabCompleter().onTabComplete(sender, cmd, alias, args);
            if (completions == null) return new ArrayList<>();
            return getFormattedCompletions(completions);
        }

        return new ArrayList<>();
    }

    private List<String> getFormattedCompletions(List<String> completions) {
        List<String> formattedCompletions = new ArrayList<>();

        for (String completion : completions) {
            formattedCompletions.add("/" + completion);
        }

        return formattedCompletions;
    }
}
