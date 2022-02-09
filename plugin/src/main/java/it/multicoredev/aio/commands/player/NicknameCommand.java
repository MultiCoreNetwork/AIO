package it.multicoredev.aio.commands.player;

import it.multicoredev.aio.AIO;
import it.multicoredev.aio.api.User;
import it.multicoredev.aio.commands.PluginCommand;
import it.multicoredev.mbcore.spigot.Chat;
import it.multicoredev.mbcore.spigot.util.TabCompleterUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
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
public class NicknameCommand extends PluginCommand {
    private static final String CMD = "nickname";

    public NicknameCommand(AIO aio) {
        super(aio, CMD);
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (!preCommandProcess(sender, getName(), args)) return true;

        Player target;
        int offset = 0;

        if (isPlayer(sender)) {
            if (args.length < 2) {
                target = (Player) sender;
            } else {
                if (!hasSubPerm(sender, "other")) {
                    insufficientPerms(sender);
                    return true;
                }

                target = Bukkit.getPlayer(args[0]);
                offset = 1;
            }
        } else {
            if (args.length < 1) {
                Chat.send(localization.notPlayer, sender);
                return true;
            }

            target = Bukkit.getPlayer(args[0]);
            offset = 1;
        }

        if (target == null) {
            Chat.send(localization.playerNotFound, sender);
            return true;
        }

        User user = storage.getUser(target.getUniqueId());
        if (user == null) {
            Chat.send(localization.playerNotFound, sender);
            return true;
        }

        String nickname = args[offset];

        if (nickname.equalsIgnoreCase("off")) nickname = target.getName();

        if (config.nicknameSection.isBlacklisted(nickname)) {
            Chat.send(localization.invalidNickname, sender);
            return true;
        }

        if (Chat.getDiscolored(nickname).length() > config.nicknameSection.nicknameMaxLength) {
            Chat.send(localization.nicknameTooLong.replace("{LENGTH}", String.valueOf(config.nicknameSection.nicknameMaxLength)), sender);
            return true;
        }

        user.setNickname(nickname);
        storage.updateUser(user);

        nickname = Chat.getTranslated(nickname);
        target.setDisplayName(nickname);

        Chat.send(localization.nicknameSetSelf.replace("{DISPLAYNAME}", target.getDisplayName()), target);
        if (target != sender) Chat.send(localization.nicknameSet
                .replace("{NAME}", target.getName())
                .replace("{DISPLAYNAME}", target.getDisplayName()), sender);

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
