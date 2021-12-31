package it.multicoredev.aio.commands.player;

import it.multicoredev.aio.AIO;
import it.multicoredev.aio.commands.PluginCommand;
import it.multicoredev.aio.utils.Utils;
import it.multicoredev.mbcore.spigot.Chat;
import it.multicoredev.mbcore.spigot.util.TabCompleterUtil;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
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
public class DisenchantCommand extends PluginCommand {
    private static final String CMD = "disenchant";

    public DisenchantCommand(AIO aio) {
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

        String name = args[offset].toLowerCase(Locale.ROOT);
        Enchantment enchantment = Enchantment.getByKey(name.startsWith("minecraft:") ? NamespacedKey.fromString(name) : NamespacedKey.minecraft(name));

        if (enchantment == null) {
            Chat.send(localization.invalidEnchant, sender);
            return true;
        }

        ItemStack item = target.getInventory().getItemInMainHand();

        if (item.getType().isAir()) {
            Chat.send(localization.noItemInHand, sender);
            return true;
        }

        item.removeEnchantment(enchantment);

        Chat.send(localization.itemDisenchantedSelf
                .replace("{NAME}", target.getName())
                .replace("{DISPLAYNAME}", target.getDisplayName())
                .replace("{ITEM}", Utils.capitalize(item.getType().name()))
                .replace("{ENCHANTMENT}", enchantment.getKey().toString()), target);

        if (target != sender) Chat.send(localization.itemDisenchanted
                .replace("{NAME}", target.getName())
                .replace("{DISPLAYNAME}", target.getDisplayName())
                .replace("{ITEM}", Utils.capitalize(item.getType().name()))
                .replace("{ENCHANTMENT}", enchantment.getKey().toString()), sender);
        return true;
    }

    @Override
    public List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) {
        if (!hasCommandPerm(sender)) return new ArrayList<>();

        if (args.length == 1) {
            if (hasSubPerm(sender, "other")) return TabCompleterUtil.getPlayers(args[0], sender.hasPermission("pv.see"));
            else return TabCompleterUtil.getCompletions(args[0], getEnchantments());
        } else if (args.length == 2) {
            if (hasSubPerm(sender, "other")) return TabCompleterUtil.getCompletions(args[1], getEnchantments());
        }

        return new ArrayList<>();
    }

    private List<String> getEnchantments() {
        List<String> enchantments = new ArrayList<>();
        Arrays.stream(Enchantment.values()).forEach(enchantment -> {
            enchantments.add(enchantment.getKey().getKey());
            enchantments.add(enchantment.getKey().toString());
        });

        return enchantments;
    }
}
