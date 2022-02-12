package it.multicoredev.aio.commands.player.kits;

import it.multicoredev.aio.AIO;
import it.multicoredev.aio.commands.PluginCommand;
import it.multicoredev.aio.models.ItemObject;
import it.multicoredev.aio.models.Kit;
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
public class KitCommand extends PluginCommand {
    private static final String CMD = "kit";

    public KitCommand(AIO aio) {
        super(aio, CMD);
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (!preCommandProcess(sender, getName(), args)) return true;

        if (!isPlayer(sender) && args.length < 2) {
            Chat.send(localization.notPlayer, sender);
            return true;
        }

        if (args.length == 0) {
            incorrectUsage(sender);
            return true;
        }

        String id;
        Player target;

        if (args.length > 1) {
            id = args[0];
            target = Bukkit.getPlayer(args[1]);

            if (!hasSubPerm(sender, "other")) {
                insufficientPerms(sender);
                return true;
            }
        } else {
            id = args[0];
            target = (Player) sender;
        }

        if (target == null || !target.isOnline()) {
            Chat.send(localization.playerNotFound, sender);
            return true;
        }

        Kit kit = aio.getKitStorage().getKitByName(id);

        if (kit == null) {
            Chat.send(localization.kitNotFound, sender);
            return true;
        }

        if (!hasSubPerm(sender, id)) {
            Chat.send(localization.kitNoPerms, sender);
            return true;
        }

        List<ItemObject> itemObjects = kit.getItems();

        if (itemObjects.isEmpty()) {
            Chat.send(localization.kitEmpty, sender);
            return true;
        }

        List<ItemStack> itemStacks = new ArrayList<>();
        List<String> invalidMaterials = new ArrayList<>();

        for (ItemObject itemObject : itemObjects) {
            String materialName = itemObject.getMaterial();
            Material material = Material.matchMaterial(materialName);

            if (material == null) {
                invalidMaterials.add(materialName);
                continue;
            }

            if (material == Material.AIR) continue;

            ItemStack itemStack = new ItemStack(material, itemObject.getAmount());
            String nbt = itemObject.getNbtString();

            //TODO Try
            if (!nbt.isEmpty()) itemStack = Bukkit.getUnsafe().modifyItemStack(itemStack, itemObject.getNbtString());
            itemStacks.add(itemStack);
        }

        if (!invalidMaterials.isEmpty()) {
            //TODO Try
            Chat.severe(placeholdersUtils.replacePlaceholders(localization.invalidKit, new String[]{"{KIT}", "{INVALID}"}, new String[]{kit.getName(), String.join(", ", invalidMaterials)}));
            return true;
        }

        PlayerInventory playerInventory = target.getInventory();

        /*if (hasSpaceInInventory(target, itemStacks)) {
            Chat.send(localization.kitNoSpace, sender);
            return true;
        }*/

        for (ItemStack itemStack : itemStacks) {
            if (itemStack != null) playerInventory.addItem(itemStack);
        }

        Chat.send(localization.kitSuccess, sender);
        return true;
    }

    @Override
    public List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 1) return aio.getKitStorage().getKitNames(sender);
        else if (args.length == 2) return TabCompleterUtil.getPlayers(args[1], sender.hasPermission("pv.see"));
        else return new ArrayList<>();
    }
}