package it.multicoredev.aio.commands;

import it.multicoredev.aio.AIO;
import it.multicoredev.aio.api.BasePluginCommand;
import it.multicoredev.aio.api.IStorage;
import it.multicoredev.aio.storage.config.Config;
import it.multicoredev.aio.storage.config.Localization;
import it.multicoredev.mbcore.spigot.Chat;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

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
public abstract class PluginCommand extends BasePluginCommand {
    protected final AIO aio;
    protected final Config config;
    protected final Localization localization;
    protected final IStorage storage;

    public PluginCommand(AIO aio, String name, String description, String usageMessage, List<String> alias) {
        super(name, description, usageMessage, alias != null ? alias.stream().map(a -> a.toLowerCase(Locale.ROOT)).collect(Collectors.toList()) : new ArrayList<>());
        this.aio = aio;
        this.config = aio.getConfiguration();
        this.localization = aio.getLocalization();
        this.storage = aio.getStorage();
    }

    public PluginCommand(AIO aio, String name, List<String> alias) {
        this(
                aio,
                name,
                aio.getLocalization().commandDescriptions.get(name),
                aio.getLocalization().commandUsages.get(name),
                alias
        );
    }

    public PluginCommand(AIO aio, String name) {
        this(
                aio,
                name,
                aio.getLocalization().commandDescriptions.get(name),
                aio.getLocalization().commandUsages.get(name),
                aio.getConfiguration().commands.get(name).alias
        );
    }

    public abstract boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args);

    public List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) {
        return new ArrayList<>();
    }

    protected boolean preprocessCheck(CommandSender sender) {
        if (!isPlayer(sender)) return true;

        Player player = (Player) sender;

        if (!hasCommandPerm(player)) {
            insufficientPerms(player);
            return false;
        }

        if (config.commandsCooldown.hasCommandCooldown(getName())) {
            if (aio.hasCommandCooldown(player, getName())) return false;
            else aio.addCommandCooldown(player, getName());
            //TODO Possibly move this to event listener
        }

        return true;
    }

    protected boolean hasCommandPerm(CommandSender sender) {
        return sender.hasPermission("aio." + getName());
    }

    protected boolean hasSubPerm(CommandSender sender, String subperm) {
        return sender.hasPermission("aio." + getName() + "." + subperm) || sender.hasPermission("aio." + getName() + "." + "*");
    }

    protected void incorrectUsage(CommandSender sender) {
        Chat.send(localization.incorrectUsage
                .replace("{USAGE}", usageMessage)
                .replace("{ALIAS}", Arrays.toString(getAliases().toArray())), sender); //TODO Use Placeholders Utils
    }

    protected void insufficientPerms(CommandSender sender) {
        Chat.send(localization.insufficientPerms, sender);
    }
}
