package it.multicoredev.aio.commands;

import it.multicoredev.aio.AIO;
import it.multicoredev.mbcore.spigot.Chat;
import it.multicoredev.mbcore.spigot.util.TabCompleterUtil;
import it.multicoredev.mbcore.spigot.util.chat.RawMessage;
import it.multicoredev.mbcore.spigot.util.chat.TextComponentBuilder;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
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
public class AIOCommand extends PluginCommand {
    private static final String CMD = "aio";

    public AIOCommand(AIO aio) {
        super(aio, CMD);
    }

    @Override
    public boolean run(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (args.length < 1) {
            Chat.send("&6&lAIO &f(&eAll In One&f) &bby &g&lMultiCore &h&lNetwork", sender);
            if (isPlayer(sender)) {
                RawMessage msg = new RawMessage();
                msg.append(new TextComponentBuilder("Visit our website ").setColor(ChatColor.AQUA).get());
                msg.append(new TextComponentBuilder("https://multicore.network").setColor(ChatColor.BLUE).setUnderlined(true).setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://multicore.network")).get());
                Chat.sendRaw(msg, (Player) sender);
            } else {
                Chat.send("&bVisit our website &9&nhttps://multicore.network", sender);
            }

            StringBuilder builder = new StringBuilder();
            for (String usage : commandData.getUsages()) builder.append("&b").append(usage).append("\n");
            Chat.send(builder.toString(), sender);
            return false;
        }

        String subcommand = args[0];
        if (subcommand.equalsIgnoreCase("reload")) {
            if (!hasSubPerm(sender, "reload")) {
                insufficientPerms(sender);
                return false;
            }

            aio.onDisable();
            aio.onEnable();
        }

        return true;
    }

    @Override
    public List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 1) {
            List<String> completions = new ArrayList<>();
            if (hasSubPerm(sender, "reload")) completions.add("reload");
            return TabCompleterUtil.getCompletions(args[0], completions);
        }

        return new ArrayList<>();
    }
}
