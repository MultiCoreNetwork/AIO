package it.multicoredev.aio.commands.player;

import it.multicoredev.aio.AIO;
import it.multicoredev.aio.commands.PluginCommand;
import it.multicoredev.aio.utils.Utils;
import it.multicoredev.mbcore.spigot.Chat;
import it.multicoredev.mbcore.spigot.util.TabCompleterUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
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
public class RepairCommand extends PluginCommand {
    private static final String CMD = "repair";

    public RepairCommand(AIO aio) {
        super(aio, CMD);
    }

    @Override
    public boolean run(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        Player target = Bukkit.getPlayer(args[0]);
        int offset = 1;

        if (target == null) {
            if (!isPlayer(sender)) {
                Chat.send(pu.replacePlaceholders(localization.playerNotFound), sender);
                return false;
            }

            target = (Player) sender;
            offset = 0;
        }

        if (isPlayer(sender) && target != sender && !hasSubPerm(sender, "other")) {
            insufficientPerms(sender);
            return false;
        }

        if (args.length - 1 >= offset) {
            if (args[offset].equalsIgnoreCase("hand")) {
                repairHand(target, sender);
            } else if (args[offset].equalsIgnoreCase("all")) {
                repairAll(target, sender);
            } else {
                repairHand(target, sender);
            }
        } else {
            repairHand(target, sender);
        }

        return true;
    }

    private boolean repairHand(Player target, CommandSender sender) {
        ItemStack item = target.getInventory().getItemInMainHand();

        if (item.getType().isAir()) {
            Chat.send(pu.replacePlaceholders(localization.noItemInHand), sender);
            return false;
        }

        if (repair(item)) {
            Chat.send(pu.replacePlaceholders(
                    localization.itemRepairedSelf,
                    new String[]{"{NAME}", "{DISPLAYNAME}", "{ITEM}"},
                    new Object[]{target.getName(), target.getDisplayName(), Utils.capitalize(item.getType().name())}
            ), target);
            if (target != sender)
                Chat.send(pu.replacePlaceholders(localization.itemRepaired, "{ITEM}", Utils.capitalize(item.getType().name())), sender);
        } else {
            Chat.send(pu.replacePlaceholders(
                    localization.itemNotRepairedSelf,
                    new String[]{"{NAME}", "{DISPLAYNAME}", "{ITEM}"},
                    new Object[]{target.getName(), target.getDisplayName(), Utils.capitalize(item.getType().name())}
            ), target);
            if (target != sender)
                Chat.send(pu.replacePlaceholders(localization.itemNotRepaired, "{ITEM}", Utils.capitalize(item.getType().name())), sender);
        }

        return true;
    }

    private boolean repairAll(Player target, CommandSender sender) {
        Inventory inventory = target.getInventory();
        int repairedItems = 0;

        for (ItemStack item : inventory.getContents()) {
            if (repair(item)) repairedItems++;
        }

        if (repairedItems > 0) {
            Chat.send(pu.replacePlaceholders(
                    localization.itemsRepairedSelf,
                    new String[]{"{NAME}", "{DISPLAYNAME}", "{ITEMS}"},
                    new Object[]{target.getName(), target.getDisplayName(), repairedItems}
            ), target);
            if (target != sender) Chat.send(pu.replacePlaceholders(localization.itemsRepaired, "{ITEMS}", String.valueOf(repairedItems)), sender);
        } else {
            Chat.send(pu.replacePlaceholders(
                    localization.itemsNotRepairedSelf,
                    new String[]{"{NAME}", "{DISPLAYNAME}"},
                    new Object[]{target.getName(), target.getDisplayName()}
            ), target);
            if (target != sender) Chat.send(pu.replacePlaceholders(localization.itemsNotRepaired), sender);
        }

        return true;
    }

    private boolean repair(ItemStack item) {
        if (item.getType().isBlock() || !(item.getItemMeta() instanceof Damageable)) return false;
        Damageable meta = (Damageable) item.getItemMeta();
        if (meta == null) return false;

        meta.setDamage(0);
        item.setItemMeta((ItemMeta) meta);
        return true;
    }

    @Override
    public List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) {
        if (!hasCommandPerm(sender)) return new ArrayList<>();

        if (args.length == 1) {
            if (hasSubPerm(sender, "other")) return TabCompleterUtil.getPlayers(args[0], sender.hasPermission("pv.see"));
            else return TabCompleterUtil.getCompletions(args[0], "hand", "all");
        } else if (args.length == 2) {
            if (hasSubPerm(sender, "other")) return TabCompleterUtil.getCompletions(args[1], "hand", "all");
        }

        return new ArrayList<>();
    }
}
