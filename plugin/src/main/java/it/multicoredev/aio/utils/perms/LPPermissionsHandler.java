package it.multicoredev.aio.utils.perms;

import it.multicoredev.aio.AIO;
import it.multicoredev.mbcore.spigot.Chat;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.group.GroupManager;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.NodeType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.OptionalInt;
import java.util.stream.Collectors;

/**
 * Copyright © 2022 by Lorenzo Magni
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
public class LPPermissionsHandler implements IPermissionsHandler {
    private final LuckPerms lp;

    public LPPermissionsHandler(@NotNull AIO aio) {
        RegisteredServiceProvider<LuckPerms> serviceProvider = aio.getServer().getServicesManager().getRegistration(LuckPerms.class);
        if (serviceProvider == null) {
            Chat.info("&eCannot establish hook in LuckPerms.");
            throw new RuntimeException("Cannot establish hook in LuckPerms.");
        }

        lp = serviceProvider.getProvider();
        Chat.info("&aEstablished hook in LuckPerms.");
    }

    @Override
    public List<String> getGroups() {
        List<Group> groups = new ArrayList<>(lp.getGroupManager().getLoadedGroups());
        groups.sort((g1, g2) -> {
            OptionalInt w1 = g1.getWeight();
            OptionalInt w2 = g2.getWeight();

            if (w1.isPresent() && w2.isPresent()) {
                return Integer.compare(w1.getAsInt(), w2.getAsInt());
            } else {
                if (w1.isPresent()) return 1;
                else if (w2.isPresent()) return -1;
                else return 0;
            }
        });

        return groups.stream().map(Group::getName).collect(Collectors.toList());
    }

    @Override
    public List<String> getPlayerGroups(@NotNull Player player) {
        User user = lp.getUserManager().getUser(player.getUniqueId());
        if (user == null) return new ArrayList<>();

        List<Group> groups = new ArrayList<>();
        List<String> groupNames = user.getNodes(NodeType.INHERITANCE)
                .stream()
                .map(group -> group.getGroupName().toLowerCase(Locale.ROOT))
                .collect(Collectors.toList());

        GroupManager groupManager = lp.getGroupManager();
        groupNames.forEach(name -> {
            Group group = groupManager.getGroup(name);
            if (group != null) groups.add(group);
        });

        groups.sort((o1, o2) -> {
            OptionalInt w1 = o1.getWeight();
            OptionalInt w2 = o2.getWeight();

            if (w1.isPresent() && w2.isPresent()) {
                return Integer.compare(w1.getAsInt(), w2.getAsInt());
            } else {
                if (w1.isPresent()) return 1;
                else if (w2.isPresent()) return -1;
                else return 0;
            }
        });

        return groups.stream().map(Group::getName).collect(Collectors.toList());
    }

    @Override
    public boolean isInGroup(@NotNull Player player, @NotNull String group) {
        List<String> playerGroups = getPlayerGroups(player);
        for (String g : playerGroups) {
            if (g.equalsIgnoreCase(group)) return true;
        }

        return false;
    }
}
