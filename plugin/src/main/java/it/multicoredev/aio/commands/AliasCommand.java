package it.multicoredev.aio.commands;

import it.multicoredev.aio.AIO;
import it.multicoredev.mbcore.spigot.Chat;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
public class AliasCommand extends PluginCommand {
    private final String command;
    private final String permission;
    private final String registeredCommand;
    private final boolean addCompletions;

    public AliasCommand(AIO aio, List<String> alias, String command, String permission, String description, String usage, boolean addCompletions) {
        super(
                aio,
                alias.get(0),
                description,
                usage,
                alias.size() > 1 ? alias.subList(0, alias.size()) : null
        );

        this.command = command;
        this.permission = permission;
        this.addCompletions = addCompletions;

        if (command.contains(" ")) registeredCommand = command.substring(0, command.indexOf(" ")).toLowerCase(Locale.ROOT);
        else registeredCommand = command.toLowerCase(Locale.ROOT);
    }

    @Override
    protected void incorrectUsage(CommandSender sender) {
        Chat.send(usageMessage, sender);
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (!preprocessCheck(sender)) return true;

        if (!sender.hasPermission(permission)) {
            insufficientPerms(sender);
            return true;
        }

        Bukkit.dispatchCommand(sender, command);

        return true;
    }

    @Override
    public List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) {
        if (addCompletions) {
            org.bukkit.command.PluginCommand cmd = aio.getCommand(registeredCommand);
            if (cmd == null) return new ArrayList<>();
            if (cmd.getTabCompleter() == null) return new ArrayList<>();

            List<String> completions = cmd.getTabCompleter().onTabComplete(sender, cmd, alias, args);
            if (completions == null) return new ArrayList<>();
            return completions;
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    protected boolean preprocessCheck(CommandSender sender) {
        if (!(sender instanceof Player)) return true;

        Player player = (Player) sender;

        if (permission != null && !player.hasPermission(permission)) {
            insufficientPerms(player);
            return false;
        }

        if (config.commandsCooldown.hasCommandCooldown(getName())) {
            if (aio.hasCommandCooldown(player, getName())) return false;
            else aio.addCommandCooldown(player, getName());
        }

        return true;
    }
}
