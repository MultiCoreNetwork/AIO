package it.multicoredev.aio.commands.player;

import it.multicoredev.aio.AIO;
import it.multicoredev.aio.commands.PluginCommand;
import it.multicoredev.mbcore.spigot.Chat;
import it.multicoredev.mbcore.spigot.util.TabCompleterUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
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
public class HatCommand extends PluginCommand {
    private static final String CMD = "hat";

    public HatCommand(AIO aio) {
        super(aio, CMD);
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (!preCommandProcess(sender, getName(), args)) return true;

        if (!isPlayer(sender)) {
            Chat.send(localization.notPlayer, sender);
            return true;
        }

        Player player = (Player) sender;
        PlayerInventory senderInventory = player.getInventory();
        ItemStack item = senderInventory.getItemInMainHand();

        if (item.getType() == Material.AIR) {
            Chat.send(localization.preventHat, sender);
            return true;
        }

        if (!hasSubPerm(player, "bypass-prevent")) {
            if (hasSubPerm(player, "prevent." + item.getType().getKey().getKey())) {
                Chat.send(localization.preventHat, sender);
                return true;
            }
        }

        if (args.length > 0) {
            Player target = Bukkit.getPlayer(args[0]);

            if (target == null) {
                Chat.send(localization.playerNotFound, sender);
                return true;
            }

            PlayerInventory targetInventory = target.getInventory();

            /*if (hasSpaceInInventory(target, item)) {
                Chat.send(localization.targetHasInventoryFull, sender);
                return true;
            }*/

            ItemStack oldHelmet = targetInventory.getHelmet();
            targetInventory.setHelmet(item);
            if (oldHelmet != null && oldHelmet.getType() != Material.AIR) targetInventory.addItem(oldHelmet);
            target.updateInventory();
        } else {
            ItemStack oldHelmet = senderInventory.getHelmet();
            senderInventory.setHelmet(item);
            if (oldHelmet != null && oldHelmet.getType() != Material.AIR) senderInventory.setItemInMainHand(oldHelmet);
            player.updateInventory();
        }

        Chat.send(localization.hatSuccess, sender);
        return true;
    }

    @Override
    public List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) {
        if (hasSubPerm(sender, "other") && args.length == 1) {
            return TabCompleterUtil.getPlayers(args[0], sender.hasPermission("pv.see"));
        }

        return new ArrayList<>();
    }
}