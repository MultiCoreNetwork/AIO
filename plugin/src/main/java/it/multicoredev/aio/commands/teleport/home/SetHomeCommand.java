package it.multicoredev.aio.commands.teleport.home;

import it.multicoredev.aio.AIO;
import it.multicoredev.aio.api.User;
import it.multicoredev.aio.api.models.Home;
import it.multicoredev.aio.commands.PluginCommand;
import it.multicoredev.mbcore.spigot.Chat;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.jetbrains.annotations.NotNull;

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
public class SetHomeCommand extends PluginCommand {
    private static final String CMD = "sethome";

    public SetHomeCommand(AIO aio) {
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

        if (args.length == 0) {
            incorrectUsage(sender);
            return true;
        }

        int homesAmount = getHomesAmount(player);
        int homesLimit = getHomesLimit(player);

        if (homesLimit != 0) {
            if (homesAmount >= homesLimit && !hasSubPerm(player, "limit.bypass")) {
                Chat.send(localization.homeLimitExceeded, sender);
                return true;
            }
        }

        createHome(player, args[0]);
        Chat.send(localization.homeCreated, sender);
        return true;
    }

    private void createHome(Player player, String homeName) {
        User user = storage.getUser(player);
        if (user == null) return;

        List<Home> homes = user.getHomes();

        for (Home h : homes) {
            if (h.getName().equals(homeName)) {
                Chat.send(localization.homeAlreadyExists, player);
                return;
            }
        }

        Home home = new Home(homeName, player.getLocation());
        user.addHome(home);

        storage.updateUser(user);
    }

    private int getHomesAmount(Player player) {
        User user = storage.getUser(player);
        if (user == null) return 0;

        return user.getHomes().size();
    }

    private int getHomesLimit(Player player) {
        int limit = 0;
        boolean useDefault = true;

        for (PermissionAttachmentInfo permission : player.getEffectivePermissions()) {
            String perm = permission.getPermission();

            if (!perm.startsWith("aio.sethome.limit")) continue;
            if (!perm.contains(".")) continue;

            useDefault = false;

            String[] parts = perm.split("\\.");

            try {
                limit = Math.max(Integer.parseInt(parts[parts.length - 1]), limit);
            } catch (NumberFormatException ignored) {
            }
        }

        if (limit == 0 && useDefault) {
            return config.defaultHomeLimit;
        }

        return limit;
    }
}
