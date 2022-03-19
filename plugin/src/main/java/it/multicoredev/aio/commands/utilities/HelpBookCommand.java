package it.multicoredev.aio.commands.utilities;

import it.multicoredev.aio.AIO;
import it.multicoredev.aio.commands.PluginCommand;
import it.multicoredev.aio.api.models.HelpBook;
import it.multicoredev.mbcore.spigot.Chat;
import it.multicoredev.mbcore.spigot.util.TabCompleterUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

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
public class HelpBookCommand extends PluginCommand {
    private static final String CMD = "helpbook";

    public HelpBookCommand(AIO aio) {
        super(aio, CMD);
    }

    @Override
    public boolean run(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (!isPlayer(sender) && args.length < 2) {
            Chat.send(pu.replacePlaceholders(localization.notPlayer), sender);
            return false;
        }

        String id;
        Player target;

        if (args.length > 1) {
            id = args[0];
            target = Bukkit.getPlayer(args[1]);

            if (!hasSubPerm(sender, "other") && !sender.equals(target)) {
                insufficientPerms(sender);
                return false;
            }
        } else if (args.length == 1) {
            id = args[0];
            target = (Player) sender;
        } else {
            id = config.helpBookSection.defBook;
            target = (Player) sender;
        }

        HelpBook hb = aio.getHelpBook(id);

        if (target == null) {
            Chat.send(pu.replacePlaceholders(localization.playerNotFound), sender);
            return false;
        }

        if (hb == null) {
            Chat.send(pu.replacePlaceholders(localization.helpbookNotFound), sender);
            return false;
        }

        if (hb.permission && !hasSubPerm(sender, hb.id.toLowerCase(Locale.ROOT))) {
            insufficientPerms(sender);
            return false;
        }

        Inventory inventory = target.getInventory();
        if (!inventory.addItem(hb.getBook()).isEmpty()) {
            if (target != sender) Chat.send(pu.replacePlaceholders(
                    localization.inventoryFull,
                    new String[]{"{NAME}", "{DISPLAYNAME}"},
                    new Object[]{target.getName(), target.getDisplayName()}
            ), sender);
            else Chat.send(pu.replacePlaceholders(localization.inventoryFullSelf), sender);

            return false;
        } else {
            if (target != sender) Chat.send(pu.replacePlaceholders(
                    localization.helpbookGiven,
                    new String[]{"{BOOK}", "{NAME}", "{DISPLAYNAME}"},
                    new Object[]{hb.name, target.getName(), target.getDisplayName()}
            ), sender);
            else Chat.send(localization.inventoryFullSelf.replace("{BOOK}", hb.name), sender);
        }

        return true;
    }

    @Override
    public List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) {
        if (!hasCommandPerm(sender)) return new ArrayList<>();

        if (args.length == 1) {
            return TabCompleterUtil.getCompletions(args[0], aio.getHelpBooks()
                    .stream()
                    .filter(book -> book.permission == null || hasSubPerm(sender, book.id.toLowerCase(Locale.ROOT)))
                    .map(book -> book.id)
                    .collect(Collectors.toList()));
        } else if (hasSubPerm(sender, "other") && args.length == 2) {
            return TabCompleterUtil.getPlayers(args[1], sender.hasPermission("pv.see"));
        }

        return new ArrayList<>();
    }
}
