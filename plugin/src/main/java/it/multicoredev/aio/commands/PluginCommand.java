package it.multicoredev.aio.commands;

import it.multicoredev.aio.AIO;
import it.multicoredev.aio.api.BasePluginCommand;
import it.multicoredev.aio.api.IStorage;
import it.multicoredev.aio.api.models.CommandData;
import it.multicoredev.aio.api.utils.IPlaceholdersUtils;
import it.multicoredev.aio.storage.config.Config;
import it.multicoredev.aio.storage.config.Localization;
import it.multicoredev.mbcore.spigot.Chat;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static it.multicoredev.aio.AIO.VAULT;

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
public abstract class PluginCommand extends BasePluginCommand {
    protected final AIO aio;
    protected final Config config;
    protected final Localization localization;
    protected final IStorage storage;
    protected final CommandData commandData;
    protected final IPlaceholdersUtils placeholdersUtils;

    public PluginCommand(AIO aio, String name, CommandData commandData) {
        super(name, commandData);
        this.aio = aio;
        this.config = aio.getConfiguration();
        this.localization = aio.getLocalization();
        this.storage = aio.getStorage();
        this.commandData = commandData;
        this.placeholdersUtils = aio.getPlaceholdersUtils();
    }

    public PluginCommand(AIO aio, String name) {
        this(aio, name, aio.getCommandData(name));
    }

    @Override
    public List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) {
        return new ArrayList<>();
    }

    @Override
    public boolean commandPreProcess(@NotNull CommandSender sender, @NotNull String command, @NotNull String[] args) {
        if (!hasCommandPerm(sender)) {
            insufficientPerms(sender);
            return false;
        }

        if (config.commandCosts.costsEnabled && VAULT && isPlayer(sender) && config.commandCosts.hasCommandCost(getName()) && !sender.hasPermission("aio.bypass.costs")) {
            Player player = (Player) sender;
            int cost = Math.abs(config.commandCosts.getCommandCost(getName()));

            if (!aio.getEconomy().has(player, cost)) {
                Chat.send(placeholdersUtils.replacePlaceholders(
                        localization.insufficientCmdMoney,
                        "{MONEY}",
                        aio.getEconomy().format(cost)
                ), player);
                return false;
            }
        }

        return true;
    }

    @Override
    public void commandPostProcess(@NotNull CommandSender sender, @NotNull String command, @NotNull String[] args, boolean success) {
    }

    protected boolean hasCommandPerm(CommandSender sender) {
        return sender.hasPermission("aio." + getName());
    }

    protected boolean hasSubPerm(CommandSender sender, String subperm) {
        return sender.hasPermission("aio." + getName() + "." + subperm) || sender.hasPermission("aio." + getName() + "." + "*");
    }

    protected void incorrectUsage(CommandSender sender, String key) {
        sendIncorrectUsage(sender, commandData.getUsages(key));
    }

    protected void incorrectUsage(CommandSender sender) {
        sendIncorrectUsage(sender, commandData.getUsages());
    }

    protected void insufficientPerms(CommandSender sender) {
        Chat.send(placeholdersUtils.replacePlaceholders(localization.insufficientPerms), sender);
    }

    protected void notImplemented(CommandSender sender) {
        Chat.send(placeholdersUtils.replacePlaceholders(localization.notImplemented), sender);
    }

    private void sendIncorrectUsage(CommandSender sender, List<String> usages) {
        StringBuilder builder = new StringBuilder();
        for (String usage : usages) builder.append(usage).append("\n");
        Chat.send(aio.getPlaceholdersUtils().replacePlaceholders(localization.incorrectUsage, new String[]{"{USAGE}", "{ALIAS}"}, new String[]{builder.toString(), Arrays.toString(commandData.getAlias().toArray())}), sender);
    }
}
