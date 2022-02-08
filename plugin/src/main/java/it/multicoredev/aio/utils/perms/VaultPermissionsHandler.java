package it.multicoredev.aio.utils.perms;

import it.multicoredev.aio.AIO;
import it.multicoredev.mbcore.spigot.Chat;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
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
public class VaultPermissionsHandler implements IPermissionsHandler {
    private final Permission vaultPerms;

    public VaultPermissionsHandler(@NotNull AIO aio) {
        RegisteredServiceProvider<Permission> serviceProvider = aio.getServer().getServicesManager().getRegistration(Permission.class);
        if (serviceProvider == null) {
            Chat.info("&eCannot establish hook in Vault Permissions.");
            throw new RuntimeException("Cannot establish hook in Vault Permissions.");
        }

        vaultPerms = serviceProvider.getProvider();
        Chat.info("&aEstablished hook in Vault Permissions.");
    }

    @Override
    public List<String> getGroups() {
        return Arrays.asList(vaultPerms.getGroups());
    }

    @Override
    public List<String> getPlayerGroups(@NotNull Player player) {
        return Arrays.stream(vaultPerms.getPlayerGroups(player)).map(group -> group.toLowerCase(Locale.ROOT)).collect(Collectors.toList());
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
