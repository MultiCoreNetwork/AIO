package it.multicoredev.aio.commands.utilities;

import it.multicoredev.aio.AIO;
import it.multicoredev.aio.commands.PluginCommand;
import it.multicoredev.aio.models.HelpBook;
import it.multicoredev.mbcore.spigot.Chat;
import it.multicoredev.mbcore.spigot.util.TabCompleterUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.*;
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
    private final Map<UUID, Map<String, Date>> cooldowns = new HashMap<>();

    public HelpBookCommand(AIO aio) {
        super(aio, CMD);
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (!preCommandProcess(sender, getName(), args)) return true;

        String id;
        Player target;

        if (!isPlayer(sender) && args.length < 1) {
            Chat.send(localization.notPlayer, sender);
            return true;
        }

        if (args.length > 1) {
            id = args[0];
            target = Bukkit.getPlayer(args[1]);

            if (!hasSubPerm(sender, "other") && target != sender) {
                insufficientPerms(sender);
                return true;
            }
        } else if (args.length == 1) {
            id = args[0];

            if (!config.helpBookSection.containsBook(id)) {
                target = Bukkit.getPlayer(id);
                id = config.helpBookSection.defBook;
            } else {
                target = (Player) sender;
            }
        } else {
            incorrectUsage(sender);
            return true;
        }

        if (!config.helpBookSection.containsBook(id)) {
            Chat.send(localization.helpbookNotFound, sender);
            return true;
        }

        if (target == null) {
            Chat.send(localization.playerNotFound, sender);
            return true;
        }

        HelpBook hb = config.helpBookSection.getBook(id);

        if (hb.permission != null && !hasSubPerm(sender, hb.permission.toLowerCase(Locale.ROOT))) {
            insufficientPerms(sender);
            return true;
        }

        if (!hasSubPerm(sender, "no-cooldown") && isPlayer(sender)) {
            UUID uuid = ((Player) sender).getUniqueId();
            if (cooldowns.containsKey(uuid)) {
                Map<String, Date> books = cooldowns.get(uuid);
                if (books.containsKey(hb.id)) {
                    Date last = books.get(hb.id);
                    Date now = new Date();

                    long difference = (now.getTime() - last.getTime()) / 1000;
                    if (difference < config.helpBookSection.getBookCooldown) {
                        Chat.send(localization.helpbookCooldown.replace("{TIME}", String.valueOf(difference)), sender);
                        return true;
                    }
                }

                books.put(hb.id, new Date());
            } else {
                Map<String, Date> books = new HashMap<>();
                books.put(hb.id, new Date());
                cooldowns.put(uuid, books);
            }
        }

        Inventory inventory = target.getInventory();
        if (!inventory.addItem(hb.getBook()).isEmpty()) {
            if (target != sender) Chat.send(localization.inventoryFull
                    .replace("{NAME}", target.getName())
                    .replace("{DISPLAYNAME}", target.getDisplayName()), sender);
            else Chat.send(localization.inventoryFullSelf, sender);
        } else {
            if (target != sender) Chat.send(localization.helpbookGiven
                    .replace("{BOOK}", hb.name)
                    .replace("{NAME}", target.getName())
                    .replace("{DISPLAYNAME}", target.getDisplayName()), sender);
            else Chat.send(localization.inventoryFullSelf.replace("{BOOK}", hb.name), sender);
        }

        return true;
    }

    @Override
    public List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) {
        if (!hasCommandPerm(sender)) return new ArrayList<>();

        if (args.length == 1) {
            return TabCompleterUtil.getCompletions(args[0], config.helpBookSection.books
                    .stream()
                    .filter(book -> book.permission == null || hasSubPerm(sender, book.permission))
                    .map(book -> book.id)
                    .collect(Collectors.toList()));
        } else if (hasSubPerm(sender, "other") && args.length == 2) {
            return TabCompleterUtil.getPlayers(args[1], sender.hasPermission("pv.see"));
        }

        return new ArrayList<>();
    }
}
