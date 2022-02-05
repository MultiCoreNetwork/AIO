package it.multicoredev.aio.commands.economy;

import it.multicoredev.aio.AIO;
import it.multicoredev.aio.api.IEconomy;
import it.multicoredev.aio.commands.PluginCommand;
import it.multicoredev.aio.storage.config.modules.EconomyModule;
import it.multicoredev.mbcore.spigot.Chat;
import it.multicoredev.mbcore.spigot.util.TabCompleterUtil;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
public class EconomyCommand extends PluginCommand {
    private static final String CMD = "economy";
    private final IEconomy economy;
    private final EconomyModule economyModule;

    public EconomyCommand(AIO aio) {
        super(aio, CMD);

        economy = aio.getEconomy();
        economyModule = aio.getModuleManager().getModule(EconomyModule.class);
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (!super.execute(sender, label, args)) return true;

        if (args.length < 1) {
            incorrectUsage(sender);
            return true;
        }

        String subcommand = args[0];
        if (subcommand.equalsIgnoreCase("give")) give(sender, args);
        else if (subcommand.equalsIgnoreCase("take")) take(sender, args);
        else if (subcommand.equalsIgnoreCase("set")) set(sender, args);
        else if (subcommand.equalsIgnoreCase("reset")) reset(sender, args);
        else incorrectUsage(sender);

        return true;
    }

    private void give(CommandSender sender, String[] args) {
        if (args.length < 2) {
            incorrectUsage(sender);
            return;
        }

        double amount;
        try {
            amount = Math.abs(Double.parseDouble(args[1]));
        } catch (NumberFormatException ignored) {
            Chat.send(localization.invalidNumber, sender);
            return;
        }

        UUID targetUuid;
        if (isPlayer(sender)) {
            if (args.length > 2) {
                targetUuid = aio.getUserUUID(args[2]);
            } else {
                targetUuid = ((Player) sender).getUniqueId();
            }
        } else {
            if (args.length > 2) {
                targetUuid = aio.getUserUUID(args[2]);
            } else {
                Chat.send(localization.notPlayer, sender);
                return;
            }
        }

        if (targetUuid == null || !storage.userExists(targetUuid)) {
            Chat.send(localization.playerNotFound, sender);
            return;
        }

        EconomyResponse response = economy.depositPlayer(targetUuid, amount);
        if (response.type.equals(EconomyResponse.ResponseType.SUCCESS)) {
            if (isPlayer(sender) && ((Player) sender).getUniqueId().equals(targetUuid)) {
                Chat.send(localization.moneyDepositedReceiver
                        .replace("{MONEY}", economy.format(amount))
                        .replace("{BALANCE}", economy.format(economy.getBalance(targetUuid))), sender);
            } else {
                Chat.send(localization.moneyDepositedSender, sender);
                Player receiver = Bukkit.getPlayer(targetUuid);
                if (receiver != null) Chat.send(localization.moneyDepositedReceiver, receiver);
            }
        } else if (response.type.equals(EconomyResponse.ResponseType.FAILURE)) {
            amount = economyModule.maxMoney - economy.getBalance(targetUuid);
            response = economy.depositPlayer(targetUuid, amount);
            if (response.type.equals(EconomyResponse.ResponseType.SUCCESS)) {
                if (isPlayer(sender) && ((Player) sender).getUniqueId().equals(targetUuid)) {
                    Chat.send(localization.moneyDepositedReceiver, sender);
                } else {
                    Chat.send(localization.moneyDepositedSender, sender);
                    Player receiver = Bukkit.getPlayer(targetUuid);
                    if (receiver != null) Chat.send(localization.moneyDepositedReceiver, receiver);
                }
            } else if (response.type.equals(EconomyResponse.ResponseType.FAILURE)) {
                Chat.send(localization.commandException, sender);
                Chat.warning(String.format("&eFailed to deposit %s%s to %s's account. Reason: %s", economy.format(amount), localization.getCurrency(amount), targetUuid, response.errorMessage));
            }
        }

        //TODO Add ability to set it to multiple players
    }

    private void take(CommandSender sender, String[] args) {
        if (args.length < 2) {
            incorrectUsage(sender);
            return;
        }

        double amount;
        try {
            amount = Math.abs(Double.parseDouble(args[1]));
        } catch (NumberFormatException ignored) {
            Chat.send(localization.invalidNumber, sender);
            return;
        }

        UUID targetUuid;
        if (isPlayer(sender)) {
            if (args.length > 2) {
                targetUuid = aio.getUserUUID(args[2]);
            } else {
                targetUuid = ((Player) sender).getUniqueId();
            }
        } else {
            if (args.length > 2) {
                targetUuid = aio.getUserUUID(args[2]);
            } else {
                Chat.send(localization.notPlayer, sender);
                return;
            }
        }

        if (targetUuid == null || !storage.userExists(targetUuid)) {
            Chat.send(localization.playerNotFound, sender);
            return;
        }

        EconomyResponse response = economy.withdrawPlayer(targetUuid, amount);
        if (response.type.equals(EconomyResponse.ResponseType.SUCCESS)) {
            if (isPlayer(sender) && ((Player) sender).getUniqueId().equals(targetUuid)) {
                Chat.send(localization.moneyWithdrawnReceiver, sender);
            } else {
                Chat.send(localization.moneyWithdrawnSender, sender);
                Player receiver = Bukkit.getPlayer(targetUuid);
                if (receiver != null) Chat.send(localization.moneyWithdrawnReceiver, receiver);
            }
        } else if (response.type.equals(EconomyResponse.ResponseType.FAILURE)) {
            double balance = economy.getBalance(targetUuid);
            amount = economyModule.minMoney < 0 ? balance + Math.abs(economyModule.minMoney) : balance - economyModule.minMoney;
            response = economy.withdrawPlayer(targetUuid, amount);
            if (response.type.equals(EconomyResponse.ResponseType.SUCCESS)) {
                if (isPlayer(sender) && ((Player) sender).getUniqueId().equals(targetUuid)) {
                    Chat.send(localization.moneyWithdrawnReceiver, sender);
                } else {
                    Chat.send(localization.moneyWithdrawnSender, sender);
                    Player receiver = Bukkit.getPlayer(targetUuid);
                    if (receiver != null) Chat.send(localization.moneyWithdrawnReceiver, receiver);
                }
            } else if (response.type.equals(EconomyResponse.ResponseType.FAILURE)) {
                Chat.send(localization.commandException, sender);
                Chat.warning(String.format("&eFailed to withdraw %s%s to %s's account. Reason: %s", economy.format(amount), localization.getCurrency(amount), targetUuid, response.errorMessage));
            }
        }

        //TODO Add ability to set it to multiple players
    }

    private void set(CommandSender sender, String[] args) {
        if (args.length < 2) {
            incorrectUsage(sender);
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(args[1]);
        } catch (NumberFormatException ignored) {
            Chat.send(localization.invalidNumber, sender);
            return;
        }

        UUID targetUuid;
        if (isPlayer(sender)) {
            if (args.length > 2) {
                targetUuid = aio.getUserUUID(args[2]);
            } else {
                targetUuid = ((Player) sender).getUniqueId();
            }
        } else {
            if (args.length > 2) {
                targetUuid = aio.getUserUUID(args[2]);
            } else {
                Chat.send(localization.notPlayer, sender);
                return;
            }
        }

        if (targetUuid == null || !storage.userExists(targetUuid)) {
            Chat.send(localization.playerNotFound, sender);
            return;
        }

        if (amount < economyModule.minMoney) amount = economyModule.minMoney;
        else if (amount > economyModule.maxMoney) amount = economyModule.maxMoney;

        EconomyResponse response = economy.setPlayerMoney(targetUuid, amount);
        if (response.type.equals(EconomyResponse.ResponseType.SUCCESS)) {
            if (isPlayer(sender) && ((Player) sender).getUniqueId().equals(targetUuid)) {
                Chat.send(localization.moneySetReceiver, sender);
            } else {
                Chat.send(localization.moneySetSender, sender);
                Player receiver = Bukkit.getPlayer(targetUuid);
                if (receiver != null) Chat.send(localization.moneySetReceiver, receiver);
            }
        } else if (response.type.equals(EconomyResponse.ResponseType.FAILURE)) {
            Chat.send(localization.commandException, sender);
            Chat.warning(String.format("&eFailed to set %s%s to %s's account. Reason: %s", economy.format(amount), localization.getCurrency(amount), targetUuid, response.errorMessage));
        }

        //TODO Add ability to set it to multiple players
    }

    private void reset(CommandSender sender, String[] args) {
        UUID targetUuid;
        if (isPlayer(sender)) {
            if (args.length > 2) {
                targetUuid = aio.getUserUUID(args[1]);
            } else {
                targetUuid = ((Player) sender).getUniqueId();
            }
        } else {
            if (args.length > 2) {
                targetUuid = aio.getUserUUID(args[1]);
            } else {
                Chat.send(localization.notPlayer, sender);
                return;
            }
        }

        if (targetUuid == null || !storage.userExists(targetUuid)) {
            Chat.send(localization.playerNotFound, sender);
            return;
        }

        double amount = economyModule.startingBalance;
        EconomyResponse response = economy.setPlayerMoney(targetUuid, amount);
        if (response.type.equals(EconomyResponse.ResponseType.SUCCESS)) {
            if (isPlayer(sender) && ((Player) sender).getUniqueId().equals(targetUuid)) {
                Chat.send(localization.moneyResetReceiver, sender);
            } else {
                Chat.send(localization.moneyResetSender, sender);
                Player receiver = Bukkit.getPlayer(targetUuid);
                if (receiver != null) Chat.send(localization.moneyResetReceiver, receiver);
            }
        } else if (response.type.equals(EconomyResponse.ResponseType.FAILURE)) {
            Chat.send(localization.commandException, sender);
            Chat.warning(String.format("&eFailed to set %s%s to %s's account. Reason: %s", economy.format(amount), localization.getCurrency(amount), targetUuid, response.errorMessage));
        }

        //TODO Add ability to set it to multiple players
    }

    @NotNull
    @Override
    public List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) {
        if (!hasCommandPerm(sender)) return new ArrayList<>();

        if (args.length == 1) {
            return TabCompleterUtil.getCompletions(args[0], "give", "take", "set", "reset");
        } else if (args.length == 2) {
            String subcommand = args[0];

            if (subcommand.equalsIgnoreCase("give") ||
                    subcommand.equalsIgnoreCase("take") ||
                    subcommand.equalsIgnoreCase("set")) {
                return TabCompleterUtil.getCompletions(args[1], "1", "2", "10", "100");
            } else if (subcommand.equalsIgnoreCase("reset")) {
                return TabCompleterUtil.getPlayers(args[1], sender.hasPermission("pv.see"));
            }
        } else if (args.length == 3) {
            String subcommand = args[0];

            if (subcommand.equalsIgnoreCase("give") ||
                    subcommand.equalsIgnoreCase("take") ||
                    subcommand.equalsIgnoreCase("set")) {
                return TabCompleterUtil.getPlayers(args[2], sender.hasPermission("pv.see"));
            }
        }

        return new ArrayList<>();
    }
}
