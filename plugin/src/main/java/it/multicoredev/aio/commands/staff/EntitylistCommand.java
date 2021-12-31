package it.multicoredev.aio.commands.staff;

import it.multicoredev.aio.AIO;
import it.multicoredev.aio.commands.PluginCommand;
import it.multicoredev.aio.utils.Utils;
import it.multicoredev.mbcore.spigot.Chat;
import it.multicoredev.mbcore.spigot.util.TabCompleterUtil;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;

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
public class EntitylistCommand extends PluginCommand {
    private static final String CMD = "entitylist";

    public EntitylistCommand(AIO aio) {
        super(aio, CMD);
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (!preprocessCheck(sender)) return true;

        if (!(sender instanceof Player) && args.length < 1) {
            incorrectUsage(sender);
            return true;
        }

        World world;

        if (args.length < 1) world = ((Player) sender).getWorld();
        else world = Bukkit.getWorld(args[0]);

        if (world == null) {
            Chat.send(localization.worldNotFound, sender);
            return true;
        }

        List<Entity> entities = world.getEntities();
        Map<String, Integer> entitiesMap = new HashMap<>();

        entities.forEach(entity -> {
            String type = Utils.capitalize(entity.getType().name().toLowerCase(Locale.ROOT));
            if (entitiesMap.containsKey(type)) entitiesMap.put(type, entitiesMap.get(type) + 1);
            else entitiesMap.put(type, 1);
        });

        Chat.send(localization.entityList.replace("{ENTITIES}", String.valueOf(entities.size())).replace("{WORLD}", world.getName()), sender);
        for (Map.Entry<String, Integer> entity : entitiesMap.entrySet()) {
            Chat.send(localization.entityListFormat.replace("{ENTITY}", entity.getKey()).replace("{AMOUNT}", String.valueOf(entity.getValue())), sender);
        }

        return true;
    }

    @Override
    public List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) {
        if (sender.hasPermission("aio.entitylist") && args.length == 1) {
            return TabCompleterUtil.getWorlds(args[0]);
        }

        return new ArrayList<>();
    }
}
