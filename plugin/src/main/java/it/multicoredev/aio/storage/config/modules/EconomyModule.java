package it.multicoredev.aio.storage.config.modules;

import com.google.gson.annotations.SerializedName;
import it.multicoredev.aio.AIO;
import it.multicoredev.aio.api.models.Module;
import org.bukkit.command.CommandSender;

import java.util.Locale;
import java.util.TreeMap;

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
public class EconomyModule extends Module {
    @SerializedName("starting_balance")
    public Double startingBalance;
    @SerializedName("command_costs_enabled")
    public Boolean commandCostsEnabled;
    @SerializedName("command_costs")
    public TreeMap<String, Double> commandCosts;
    @SerializedName("max_money")
    public Double maxMoney;
    @SerializedName("min_money")
    public Double minMoney;
    @SerializedName("log_transactions")
    public Boolean logTransactions;
    @SerializedName("minimum_pay_amount")
    public Double minPayAmount;
    @SerializedName("currency_singular")
    public String currencySingular;
    @SerializedName("currency_plural")
    public String currencyPlural;

    public EconomyModule() {
        super("economy");
    }

    @Override
    public void init() {
        if (startingBalance == null) startingBalance = 0d;
        if (commandCostsEnabled == null) commandCostsEnabled = false;
        if (commandCosts == null) commandCosts = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        if (maxMoney == null) maxMoney = (double) Integer.MAX_VALUE;
        if (minMoney == null) minMoney = 0d;
        if (logTransactions == null) logTransactions = true;
        if (minPayAmount == null) minPayAmount = 0.01;
        if (currencySingular == null) currencySingular = "$";
        if (currencyPlural == null) currencyPlural = "$";
    }

    @Override
    public boolean isValid() {
        return true;
    }

    public String getCurrency(double amount) {
        if (amount == 1) return currencySingular;
        else return currencyPlural;
    }

    @Override
    public boolean isEnabled() {
        return super.isEnabled() && AIO.VAULT;
    }

    public boolean hasCommandCost(String cmd, CommandSender sender) {
        if (!isEnabled()) return false;
        if (!commandCostsEnabled) return false;
        if (sender.hasPermission("aio.bypass.command_costs")) return false;
        return commandCosts.containsKey(cmd); //TODO Test if it's case insensitive
    }

    public double getCommandCost(String cmd) {
        return Math.abs(commandCosts.get(cmd));
    }
}
