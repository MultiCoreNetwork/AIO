package it.multicoredev.aio;

import it.multicoredev.aio.api.IEconomy;
import it.multicoredev.aio.api.IStorage;
import it.multicoredev.aio.api.User;
import it.multicoredev.aio.storage.config.modules.EconomyModule;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.OfflinePlayer;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
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
public class AIOEconomy implements IEconomy {
    private final AIO aio;
    private final IStorage storage;
    private final EconomyModule economyModule;
    private NumberFormat numberFormat;

    public AIOEconomy(AIO aio) {
        this.aio = aio;
        storage = aio.getStorage();
        economyModule = aio.getModuleManager().getModule(EconomyModule.class);

        try {
            numberFormat = NumberFormat.getCurrencyInstance(Locale.forLanguageTag(aio.getLocalization().localization));
            if (numberFormat == null) throw new Exception();
        } catch (Exception ignore) {
            numberFormat = NumberFormat.getCurrencyInstance(Locale.ROOT);
        }
    }

    @Override
    public boolean isEnabled() {
        return AIO.VAULT && economyModule.enabled;
    }

    @Override
    public String getName() {
        return aio.getName();
    }

    @Override
    public boolean hasBankSupport() {
        return false;
    }

    @Override
    public int fractionalDigits() {
        return 2;
    }

    @Override
    public String format(double amount) {
        return numberFormat.format(amount);
    }

    @Override
    public String formatWithCurrency(double amount) {
        if (amount == 1) {
            return format(amount) + currencyNameSingular();
        } else {
            return format(amount) + currencyNamePlural();
        }
    }

    @Override
    public String currencyNamePlural() {
        return aio.getLocalization().currencyPlural;
    }

    @Override
    public String currencyNameSingular() {
        return aio.getLocalization().currencySingular;
    }

    @Override
    public boolean hasAccount(String playerName) {
        return storage.userExists(playerName);
    }

    @Override
    public boolean hasAccount(OfflinePlayer offlinePlayer) {
        return storage.userExists(offlinePlayer);
    }

    @Override
    public boolean hasAccount(String playerName, String worldName) {
        return storage.userExists(playerName);
    }

    @Override
    public boolean hasAccount(OfflinePlayer offlinePlayer, String worldName) {
        return storage.userExists(offlinePlayer);
    }

    @Override
    public double getBalance(UUID uuid) {
        return storage.getUser(uuid).getMoney();
    }

    @Override
    public double getBalance(String playerName) {
        return storage.getUser(playerName).getMoney();
    }

    @Override
    public double getBalance(OfflinePlayer offlinePlayer) {
        return storage.getUser(offlinePlayer).getMoney();
    }

    @Override
    public double getBalance(String playerName, String world) {
        return storage.getUser(playerName).getMoney();
    }

    @Override
    public double getBalance(OfflinePlayer offlinePlayer, String world) {
        return storage.getUser(offlinePlayer).getMoney();
    }

    @Override
    public boolean has(String playerName, double amount) {
        return storage.getUser(playerName).getMoney() >= amount;
    }

    @Override
    public boolean has(OfflinePlayer offlinePlayer, double amount) {
        return storage.getUser(offlinePlayer).getMoney() >= amount;
    }

    @Override
    public boolean has(String playerName, String worldName, double amount) {
        return storage.getUser(playerName).getMoney() >= amount;
    }

    @Override
    public boolean has(OfflinePlayer offlinePlayer, String worldName, double amount) {
        return storage.getUser(offlinePlayer).getMoney() >= amount;
    }

    @Override
    public EconomyResponse setPlayerMoney(UUID uuid, double amount) {
        User user = storage.getUser(uuid);
        user.setMoney(amount);
        boolean result = storage.updateUser(user);
        if (result) return new EconomyResponse(amount, amount, EconomyResponse.ResponseType.SUCCESS, aio.getLocalization().moneySet);
        else return new EconomyResponse(amount, user.getMoney(), EconomyResponse.ResponseType.FAILURE, aio.getLocalization().moneyNotSet);
    }

    @Override
    public EconomyResponse withdrawPlayer(User user, double amount) {
        double newMoney = user.getMoney() - amount;

        if (newMoney < economyModule.minMoney) return new EconomyResponse(
                amount,
                user.getMoney(),
                EconomyResponse.ResponseType.FAILURE,
                aio.getLocalization().insufficientMoney);

        user.withdrawMoney(amount);
        boolean result = storage.updateUser(user);
        if (result) return new EconomyResponse(amount, newMoney, EconomyResponse.ResponseType.SUCCESS, aio.getLocalization().moneyWithdrawn);
        else return new EconomyResponse(amount, user.getMoney(), EconomyResponse.ResponseType.FAILURE, aio.getLocalization().moneyNotWithdrawn);
    }

    @Override
    public EconomyResponse withdrawPlayer(UUID uuid, double amount) {
        return withdrawPlayer(storage.getUser(uuid), amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(String playerName, double amount) {
        return withdrawPlayer(storage.getUser(playerName), amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, double amount) {
        return withdrawPlayer(storage.getUser(offlinePlayer), amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(String playerName, String worldName, double amount) {
        return withdrawPlayer(storage.getUser(playerName), amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, String worldName, double amount) {
        return withdrawPlayer(storage.getUser(offlinePlayer), amount);
    }

    @Override
    public EconomyResponse depositPlayer(User user, double amount) {
        double newMoney = user.getMoney() + amount;

        if (newMoney > economyModule.maxMoney) return new EconomyResponse(
                amount,
                user.getMoney(),
                EconomyResponse.ResponseType.FAILURE,
                aio.getLocalization().maxMoneyReached
        );

        user.depositMoney(amount);
        boolean result = storage.updateUser(user);
        if (result) return new EconomyResponse(amount, newMoney, EconomyResponse.ResponseType.SUCCESS, aio.getLocalization().moneyDeposited);
        else return new EconomyResponse(amount, user.getMoney(), EconomyResponse.ResponseType.FAILURE, aio.getLocalization().moneyNotDeposited);
    }

    @Override
    public EconomyResponse depositPlayer(UUID uuid, double amount) {
        return depositPlayer(storage.getUser(uuid), amount);
    }

    @Override
    public EconomyResponse depositPlayer(String playerName, double amount) {
        return depositPlayer(storage.getUser(playerName), amount);
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer offlinePlayer, double amount) {
        return depositPlayer(storage.getUser(offlinePlayer), amount);
    }

    @Override
    public EconomyResponse depositPlayer(String playerName, String worldName, double amount) {
        return depositPlayer(storage.getUser(playerName), amount);
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer offlinePlayer, String worldName, double amount) {
        return depositPlayer(storage.getUser(offlinePlayer), amount);
    }

    private EconomyResponse notImplemented() {
        return new EconomyResponse(-1, -1, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Not implemented");
    }

    @Override
    public EconomyResponse createBank(String name, String player) {
        return notImplemented();
    }

    @Override
    public EconomyResponse createBank(String name, OfflinePlayer offlinePlayer) {
        return notImplemented();
    }

    @Override
    public EconomyResponse deleteBank(String name) {
        return notImplemented();
    }

    @Override
    public EconomyResponse bankBalance(String name) {
        return notImplemented();
    }

    @Override
    public EconomyResponse bankHas(String name, double amount) {
        return notImplemented();
    }

    @Override
    public EconomyResponse bankWithdraw(String name, double amount) {
        return notImplemented();
    }

    @Override
    public EconomyResponse bankDeposit(String name, double amount) {
        return notImplemented();
    }

    @Override
    public EconomyResponse isBankOwner(String name, String playerName) {
        return notImplemented();
    }

    @Override
    public EconomyResponse isBankOwner(String s, OfflinePlayer offlinePlayer) {
        return notImplemented();
    }

    @Override
    public EconomyResponse isBankMember(String name, String playerName) {
        return notImplemented();
    }

    @Override
    public EconomyResponse isBankMember(String name, OfflinePlayer offlinePlayer) {
        return notImplemented();
    }

    @Override
    public List<String> getBanks() {
        return new ArrayList<>();
    }

    @Override
    public boolean createPlayerAccount(String playerName) {
        return true;
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer offlinePlayer) {
        return true;
    }

    @Override
    public boolean createPlayerAccount(String playerName, String worldName) {
        return true;
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer offlinePlayer, String worldName) {
        return true;
    }
}
