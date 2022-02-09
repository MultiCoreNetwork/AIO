package it.multicoredev.aio.listeners.player;

import it.multicoredev.aio.AIO;
import it.multicoredev.aio.listeners.PluginListenerExecutor;
import it.multicoredev.mbcore.spigot.Chat;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.Locale;

import static it.multicoredev.aio.AIO.VAULT;

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
public class PlayerCommandPreprocessListener extends PluginListenerExecutor<PlayerCommandPreprocessEvent> {

    public PlayerCommandPreprocessListener(Class<PlayerCommandPreprocessEvent> eventClass, AIO aio) {
        super(eventClass, aio);
    }

    @Override
    public void onEvent(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        String completeCommand = event.getMessage().substring(1).toLowerCase(Locale.ROOT);
        String command = completeCommand.contains(" ") ? completeCommand.split(" ")[0] : completeCommand;

        if (config.commandCooldown.cooldownEnabled) {
            int commandCooldown = aio.hasCommandCooldown(player, command);
            if (commandCooldown > 0) {
                event.setCancelled(true);
                Chat.send(aio.getPlaceholdersUtils().replacePlaceholders(localization.commandCooldown, "{TIME}", commandCooldown), player);
            }

            if (config.commandCooldown.hasCommandCooldown(command) && !aio.getCommandRegistry().getCommandNames().contains(command)) {
                aio.addCommandCooldown(player, command);
            }
        }

        if (config.commandCosts.costsEnabled && VAULT && !aio.getCommandRegistry().getCommandNames().contains(command) && config.commandCosts.hasCommandCost(command) && !player.hasPermission("aio.bypass.costs")) {
            int cost = Math.abs(config.commandCosts.getCommandCost(command));

            if (aio.getEconomy().has(player, cost)) {
                aio.getEconomy().withdrawPlayer(player, cost);
            } else {
                Chat.send(aio.getPlaceholdersUtils().replacePlaceholders(localization.insufficientCmdMoney, "{MONEY}", aio.getEconomy().format(cost)), player);
                event.setCancelled(true);
            }
        }
    }
}
