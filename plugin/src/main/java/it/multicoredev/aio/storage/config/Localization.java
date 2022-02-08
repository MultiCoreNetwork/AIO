package it.multicoredev.aio.storage.config;

import com.google.gson.annotations.SerializedName;
import it.multicoredev.mclib.json.JsonConfig;

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
public class Localization extends JsonConfig {
    @SerializedName("localization")
    public String localization;
    @SerializedName("command_cooldown")
    public String commandCooldown;
    @SerializedName("command_exception")
    public String commandException;
    @SerializedName("currency_singular")
    public String currencySingular;
    @SerializedName("currency_plural")
    public String currencyPlural;
    public String day;
    public String days;
    public String hour;
    public String hours;
    @SerializedName("incorrect_usage")
    public String incorrectUsage;
    @SerializedName("insufficient_permissions")
    public String insufficientPerms;
    @SerializedName("invalid_enchant")
    public String invalidEnchant;
    @SerializedName("invalid_number")
    public String invalidNumber;
    @SerializedName("inventory_full")
    public String inventoryFull;
    @SerializedName("inventory_full_self")
    public String inventoryFullSelf;
    public String minute;
    public String minutes;
    public String month;
    public String months;
    public String motd;
    @SerializedName("not_implemented")
    public String notImplemented;
    @SerializedName("no_item_in_hand")
    public String noItemInHand;
    @SerializedName("not_a_player")
    public String notPlayer;
    @SerializedName("player_not_found")
    public String playerNotFound;
    public String second;
    public String seconds;
    @SerializedName("teleport_cancelled")
    public String teleportCancelled;
    @SerializedName("teleport_with_delay")
    public String teleportWithDelay;
    @SerializedName("world_not_found")
    public String worldNotFound;
    public String year;
    public String years;

    @SerializedName("back_teleport")
    public String backTeleport;
    @SerializedName("back_teleport_self")
    public String backTeleportSelf;
    @SerializedName("location_not_available")
    public String locationNotAvailable;

    @SerializedName("chat_cleaned")
    public String chatCleaned;

    @SerializedName("item_disenchanted")
    public String itemDisenchanted;
    @SerializedName("item_disenchanted_self")
    public String itemDisenchantedSelf;

    @SerializedName("item_enchanted")
    public String itemEnchanted;
    @SerializedName("item_enchanted_self")
    public String itemEnchantedSelf;

    @SerializedName("insufficient_money")
    public String insufficientMoney;
    @SerializedName("max_money_reached")
    public String maxMoneyReached;
    @SerializedName("money_deposited")
    public String moneyDeposited;
    @SerializedName("money_not_deposited")
    public String moneyNotDeposited;
    @SerializedName("money_not_withdrawn")
    public String moneyNotWithdrawn;
    @SerializedName("money_withdrawn")
    public String moneyWithdrawn;
    @SerializedName("money_set")
    public String moneySet;
    @SerializedName("money_not_set")
    public String moneyNotSet;

    @SerializedName("money_deposited_sender")
    public String moneyDepositedSender;
    @SerializedName("money_deposited_receiver")
    public String moneyDepositedReceiver;
    @SerializedName("money_withdrawn_sender")
    public String moneyWithdrawnSender;
    @SerializedName("money_withdrawn_receiver")
    public String moneyWithdrawnReceiver;
    @SerializedName("money_set_sender")
    public String moneySetSender;
    @SerializedName("money_set_receiver")
    public String moneySetReceiver;
    @SerializedName("money_reset_sender")
    public String moneyResetSender;
    @SerializedName("money_reset_receiver")
    public String moneyResetReceiver;

    @SerializedName("entity_list")
    public String entityList;
    @SerializedName("entity_list_format")
    public String entityListFormat;

    @SerializedName("feed")
    public String feed;
    @SerializedName("feed_self")
    public String feedSelf;

    @SerializedName("fly_enabled")
    public String flyEnabled;
    @SerializedName("fly_enabled_self")
    public String flyEnabledSelf;
    @SerializedName("fly_disabled")
    public String flyDisabled;
    @SerializedName("fly_disabled_self")
    public String flyDisabledSelf;

    @SerializedName("invalid_gamemode")
    public String invalidGamemode;
    @SerializedName("gamemode_set")
    public String gamemodeSet;
    @SerializedName("gamemode_set_self")
    public String gamemodeSetSelf;

    @SerializedName("god_enabled")
    public String godEnabled;
    @SerializedName("god_enabled_self")
    public String godEnabledSelf;
    @SerializedName("god_disabled")
    public String godDisabled;
    @SerializedName("god_disabled_self")
    public String godDisabledSelf;

    @SerializedName("hat_success")
    public String hatSuccess;
    @SerializedName("prevent_hat")
    public String preventHat;

    @SerializedName("available_homes")
    public String availableHomes;
    @SerializedName("home_already_exists")
    public String homeAlreadyExists;
    @SerializedName("home_deleted")
    public String homeDeleted;
    @SerializedName("home_limit_exceeded")
    public String homeLimitExceeded;
    @SerializedName("home_not_found")
    public String homeNotFound;

    @SerializedName("heal")
    public String heal;
    @SerializedName("heal_self")
    public String healSelf;

    @SerializedName("helpbook_not_found")
    public String helpbookNotFound;
    @SerializedName("helpbook_cooldown")
    public String helpbookCooldown;
    @SerializedName("helpbook_given")
    public String helpbookGiven;
    @SerializedName("helpbook_given_self")
    public String helpbookGivenSelf;

    @SerializedName("kit_empty")
    public String kitEmpty;
    @SerializedName("kit_not_found")
    public String kitNotFound;
    @SerializedName("kit_no_perms")
    public String kitNoPerms;
    @SerializedName("kit_no_space")
    public String kitNoSpace;
    @SerializedName("kit_success")
    public String kitSuccess;
    @SerializedName("invalid_kit")
    public String invalidKit;

    @SerializedName("invalid_nickname")
    public String invalidNickname;
    @SerializedName("nickname_set")
    public String nicknameSet;
    @SerializedName("nickname_set_self")
    public String nicknameSetSelf;
    @SerializedName("nickname_too_long")
    public String nicknameTooLong;

    @SerializedName("item_repaired")
    public String itemRepaired;
    @SerializedName("item_repaired_self")
    public String itemRepairedSelf;
    @SerializedName("item_not_repaired")
    public String itemNotRepaired;
    @SerializedName("item_not_repaired_self")
    public String itemNotRepairedSelf;
    @SerializedName("items_repaired")
    public String itemsRepaired;
    @SerializedName("items_repaired_self")
    public String itemsRepairedSelf;
    @SerializedName("items_not_repaired")
    public String itemsNotRepaired;
    @SerializedName("items_not_repaired_self")
    public String itemsNotRepairedSelf;

    @SerializedName("lightning_summon_failed")
    public String lightningSummonFailed;

    @SerializedName("max_rtp_exceeded")
    public String maxRtpExceeded;
    @SerializedName("rtp_teleport")
    public String rtpTeleport;
    @SerializedName("rtp_teleport_self")
    public String rtpTeleportSelf;

    @SerializedName("run_later_scheduled")
    public String runLaterScheduled;

    @SerializedName("spawn_set")
    public String spawnSet;
    @SerializedName("spawn_not_set")
    public String spawnNotSet;
    @SerializedName("spawn_teleport")
    public String spawnTeleport;
    @SerializedName("spawn_teleport_self")
    public String spawnTeleportSelf;

    @SerializedName("sudo_failed")
    public String sudoFailed;
    @SerializedName("sudo_prevent")
    public String sudoPrevent;
    @SerializedName("sudo_success")
    public String sudoSuccess;

    @SerializedName("fly_speed_set")
    public String flySpeedSet;
    @SerializedName("fly_speed_set_self")
    public String flySpeedSetSelf;
    @SerializedName("walk_speed_set")
    public String walkSpeedSet;
    @SerializedName("walk_speed_set_self")
    public String walkSpeedSetSelf;

    @SerializedName("time_set_day")
    public String timeSetDay;
    @SerializedName("time_set_night")
    public String timeSetNight;

    @SerializedName("tpall")
    public String tpall;
    @SerializedName("tpall_self")
    public String tpallSelf;

    public String trash;

    @SerializedName("available_warps")
    public String availableWarps;
    @SerializedName("invalid_warp_mode")
    public String invalidWarpMode;
    @SerializedName("no_warps")
    public String noWarps;
    @SerializedName("warp_already_existing")
    public String warpAlreadyExist;
    @SerializedName("warp_created")
    public String warpCreated;
    @SerializedName("warp_deleted")
    public String warpDeleted;
    @SerializedName("warp_not_found")
    public String warpNotFound;
    @SerializedName("warp_not_global")
    public String warpNotGlobal;

    @SerializedName("weather_set_rain")
    public String weatherSetRain;
    @SerializedName("weather_set_sun")
    public String weatherSetSun;
    @SerializedName("weather_set_thunder")
    public String weatherSetThunder;

    @SerializedName("join_message")
    public String joinMsg;
    @SerializedName("quit_message")
    public String quitMsg;
    @SerializedName("first_join_message")
    public String firstJoinMsg;
    @SerializedName("welcome_message")
    public String welcomeMsg;

    public Localization() {
        init();
    }

    @Override
    protected void init() {
        if (localization == null) localization = "us";
        if (commandCooldown == null) commandCooldown = "&cYou have to wait {TIME} to use this command again.";
        if (commandException == null) commandException = "&cThis command throw an exception! Check the console for more info.";
        if (currencySingular == null) currencySingular = "$";
        if (currencyPlural == null) currencyPlural = "$";
        if (day == null) day = "day";
        if (days == null) days = "days";
        if (hour == null) hour = "hour";
        if (hours == null) hours = "hours";
        if (incorrectUsage == null) incorrectUsage = "&cIncorrect usage! Usage:\n&e{USAGE}\n&cAlias: &e{ALIAS}";
        if (insufficientPerms == null) insufficientPerms = "&cInsufficient permissions!";
        if (invalidEnchant == null) invalidEnchant = "&cEnchantment not found.";
        if (invalidNumber == null) invalidNumber = "&cInvalid number!";
        if (inventoryFull == null) inventoryFull = "&c{DISPLAYNAME}'s inventory is full.";
        if (inventoryFullSelf == null) inventoryFullSelf = "&cYour inventory is full.";
        if (minute == null) minute = "minute";
        if (minutes == null) minutes = "minutes";
        if (month == null) month = "month";
        if (months == null) months = "months";
        if (motd == null) motd = "&hWelcome {DISPLAYNAME} to the server!";
        if (notImplemented == null) notImplemented = "&4This command is not implemented yet!";
        if (noItemInHand == null) noItemInHand = "&cYou must hold an item in your main hand.";
        if (notPlayer == null) notPlayer = "&cYou must be a player to execute this command!";
        if (playerNotFound == null) playerNotFound = "&cPlayer not found.";
        if (second == null) second = "second";
        if (seconds == null) seconds = "seconds";
        if (teleportCancelled == null) teleportCancelled = "&cYour teleport has been cancelled. &e{REASON}";
        if (teleportWithDelay == null) teleportWithDelay = "&hYou will be teleported in #2196f3{DELAY} &hseconds.";
        if (worldNotFound == null) worldNotFound = "&cWorld not found!";
        if (year == null) year = "year";
        if (years == null) years = "years";

        if (backTeleport == null) backTeleport = "&h{DISPLAYNAME} has been teleported to the last location.";
        if (backTeleportSelf == null) backTeleportSelf = "&hYou have been teleported to your last location.";
        if (locationNotAvailable == null) locationNotAvailable = "&cLocation not available.";

        if (chatCleaned == null) chatCleaned = "&hChat cleaned by the staff.";

        if (itemDisenchanted == null) itemDisenchanted = "&hYou removed &e{ENCHANTMENT} &hform &e{DISPLAYNAME}&h's &e{ITEM} &h.";
        if (itemDisenchantedSelf == null) itemDisenchantedSelf = "&hYou removed &e{ENCHANTMENT} &hfrom &e{ITEM} &h.";

        if (itemEnchanted == null) itemEnchanted = "&hYou enchanted &e{DISPLAYNAME}&h's &e{ITEM} &hwith &e{ENCHANTMENT} {LEVEL}&h.";
        if (itemEnchantedSelf == null) itemEnchantedSelf = "&hYour &e{ITEM} &hhas been enchanted with &e{ENCHANTMENT} {LEVEL}&h.";

        if (insufficientMoney == null) insufficientMoney = "Insufficient money";
        if (maxMoneyReached == null) maxMoneyReached = "Max money reached";
        if (moneyDeposited == null) moneyDeposited = "Money deposited";
        if (moneyNotDeposited == null) moneyNotDeposited = "Money not deposited";
        if (moneyNotWithdrawn == null) moneyNotWithdrawn = "Money not withdrawn";
        if (moneyWithdrawn == null) moneyWithdrawn = "Money withdrawn";
        if (moneySet == null) moneySet = "Money set";
        if (moneyNotSet == null) moneyNotSet = "Money not set";

        if (moneyDepositedSender == null) moneyDepositedSender = "&e{MONEY}&h deposited to &e{DISPLAYNAME}&h's balance.";
        if (moneyDepositedReceiver == null) moneyDepositedReceiver = "&e{MONEY}&h has been deposited to your balance.";
        if (moneyWithdrawnSender == null) moneyWithdrawnSender = "&e{MONEY}&h withdrawn from &e{DISPLAYNAME}&h's balance.";
        if (moneyWithdrawnReceiver == null) moneyWithdrawnReceiver = "&e{MONEY}&h has been withdrawn from your balance.";
        if (moneySetSender == null) moneySetSender = "&e{DISPLAYNAME}&h's balance set to &e{MONEY}&h.";
        if (moneySetReceiver == null) moneySetReceiver = "&hYour balance has been set to &e{MONEY}&h.";
        if (moneyResetSender == null) moneyResetSender = "&e{DISPLAYNAME}&h's balance reset to &e{MONEY}&h.";
        if (moneyResetReceiver == null) moneyResetReceiver = "&hYour balance has been reset to &e{MONEY}&h.";

        if (entityList == null) entityList = "&hThere are {ENTITIES} in world {WORLD}.";
        if (entityListFormat == null) entityListFormat = "#2196f3{ENTITY} &e{AMOUNT}";

        if (feed == null) feed = "&h{DISPLAYNAME} has been fed.";
        if (feedSelf == null) feedSelf = "&hYou have been fed.";

        if (flyEnabled == null) flyEnabled = "&hYou have enabled flight for {DISPLAYNAME}.";
        if (flyEnabledSelf == null) flyEnabledSelf = "&hYou have enabled flight.";
        if (flyDisabled == null) flyDisabled = "&hYou have disabled flight for {DISPLAYNAME}.";
        if (flyDisabledSelf == null) flyDisabledSelf = "&hYou have disabled flight.";

        if (invalidGamemode == null) invalidGamemode = "&cInvalid gamemode.";
        if (gamemodeSet == null) gamemodeSet = "&f{DISPLAYNAME} gamemode set to {GAMEMODE}.";
        if (gamemodeSetSelf == null) gamemodeSetSelf = "&fGamemode set to {GAMEMODE}.";

        if (godEnabled == null) godEnabled = "&hYou have enabled god mode for {DISPLAYNAME}.";
        if (godEnabledSelf == null) godEnabledSelf = "&hYou have enabled god mode.";
        if (godDisabled == null) godDisabled = "&hYou have disabled god mode for {DISPLAYNAME}.";
        if (godDisabledSelf == null) godDisabledSelf = "&hYou have disabled god mode.";

        if (hatSuccess == null) hatSuccess = "&hYou wore your hat!";
        if (preventHat == null) preventHat = "&hYou can't wear as hat this item!";

        if (availableHomes == null) availableHomes = "&hAvailable homes: {HOMES}";
        if (homeAlreadyExists == null) homeAlreadyExists = "&cAn home with this name already exists!";
        if (homeDeleted == null) homeDeleted = "&hHome deleted!";
        if (homeLimitExceeded == null) homeLimitExceeded = "&cYou can't set another home! Limit exceeded!";
        if (homeNotFound == null) homeNotFound = "&cHome not found!";

        if (heal == null) heal = "&h{DISPLAYNAME} has been healed.";
        if (healSelf == null) healSelf = "&hYou have been healed.";

        if (helpbookNotFound == null) helpbookNotFound = "&cBook not found.";
        if (helpbookCooldown == null) helpbookCooldown = "&cYou have to wait {TIME} seconds to get this book.";
        if (helpbookGiven == null) helpbookGiven = "&hYou gave &e{book} &hto {DISPLAYNAME}.";
        if (helpbookGivenSelf == null) helpbookGivenSelf = "&hYou received &e{book}&h.";

        if (invalidKit == null) invalidKit = "&eSome items described in the kit {KIT} are invalid: {INVALID}";
        if (kitEmpty == null) kitEmpty = "&cThis kit is empty.";
        if (kitNotFound == null) kitNotFound = "&cKit not found.";
        if (kitNoPerms == null) kitNoPerms = "&cYou don't have permissions for use this kit.";
        if (kitNoSpace == null) kitNoSpace = "&cThere is not enough inventory space.";
        if (kitSuccess == null) kitSuccess = "&hKit given.";

        if (lightningSummonFailed == null) lightningSummonFailed = "&cLightning summon failed.";

        if (invalidNickname == null) invalidNickname = "&cThis nickname is not allowed.";
        if (nicknameSet == null) nicknameSet = "&h{NAME}'s nickname is now {DISPLAYNAME}.";
        if (nicknameSetSelf == null) nicknameSetSelf = "&hYour nickname is now {DISPLAYNAME}.";
        if (nicknameTooLong == null) nicknameTooLong = "&cThis nickname is too long. (Max length {LENGTH})";

        if (itemRepaired == null) itemRepaired = "&hYou repaired {DISPLAYNAME}'s {ITEM}.";
        if (itemRepairedSelf == null) itemRepairedSelf = "&hYour {ITEM} has been repaired.";
        if (itemNotRepaired == null) itemNotRepaired = "&c{DISPLAYNAME}'s {ITEM} not repaired.";
        if (itemNotRepairedSelf == null) itemNotRepairedSelf = "&cYour {ITEM} has not been repaired.";
        if (itemsRepaired == null) itemsRepaired = "&hYou repaired {DISPLAYNAME}'s {ITEMS}.";
        if (itemsRepairedSelf == null) itemsRepairedSelf = "&hYour {ITEMS} items have been repaired.";
        if (itemsNotRepaired == null) itemsNotRepaired = "&cNone of {DISPLAYNAME}'s items has been repaired.";
        if (itemsNotRepairedSelf == null) itemsNotRepairedSelf = "&cNone of your items has been repaired.";

        if (maxRtpExceeded == null) maxRtpExceeded = "&cYou exceeded the max amount of RTPs.";
        if (rtpTeleport == null) rtpTeleport = "&h{DISPLAYNAME} has been teleported to a random location.";
        if (rtpTeleportSelf == null) rtpTeleportSelf = "&hYou have been teleported to a random location.";

        if (runLaterScheduled == null) runLaterScheduled = "&hCommand was scheduled to be run in {DELAY}.";

        if (spawnSet == null) spawnSet = "&hServer spawn set to your location.";
        if (spawnNotSet == null) spawnNotSet = "&cServer spawn is not set yet!";
        if (spawnTeleport == null) spawnTeleport = "&h{DISPLAYNAME} has been teleported to the spawn.";
        if (spawnTeleportSelf == null) spawnTeleportSelf = "&hYou have been teleported to the spawn.";

        if (sudoFailed == null) sudoFailed = "&cSudo failed!";
        if (sudoPrevent == null) sudoPrevent = "&cYou can't use sudo as this player.";
        if (sudoSuccess == null) sudoSuccess = "&hSudo success!";

        if (flySpeedSet == null) flySpeedSet = "&h{DISPLAYNAME}'s flying speed set to {SPEED}.";
        if (flySpeedSetSelf == null) flySpeedSetSelf = "&hFlying speed set to {SPEED}.";
        if (walkSpeedSet == null) walkSpeedSet = "&h{DISPLAYNAME}'s walking speed set to {SPEED}.";
        if (walkSpeedSetSelf == null) walkSpeedSetSelf = "&hWalking speed set to {SPEED}.";

        if (timeSetDay == null) timeSetDay = "&eTime set to day in world {WORLD}.";
        if (timeSetNight == null) timeSetNight = "&hTime set to night in world {WORLD}.";

        if (tpall == null) tpall = "&hYou have been teleported!";
        if (tpallSelf == null) tpallSelf = "&hYou teleported everyone to you.";

        if (trash == null) trash = "Trash Can";

        if (availableWarps == null) availableWarps = "&hWarps available are: {WARPS}";
        if (invalidWarpMode == null) invalidWarpMode ="&cInvalid warp mode! Only global or local are allowed!";
        if (noWarps == null) noWarps = "&hNo warps available.";
        if (warpAlreadyExist == null) warpAlreadyExist = "&cWarp already exists!";
        if (warpCreated == null) warpCreated = "&hWarp created!";
        if (warpDeleted == null) warpDeleted = "&hWarp deleted!";
        if (warpNotFound == null) warpNotFound = "&cWarp not found!";
        if (warpNotGlobal == null) warpNotGlobal = "&cYou can teleport to this warp only from world: {WORLD}!";

        if (weatherSetRain == null) weatherSetRain = "&gWeather set to rain in world {WORLD}.";
        if (weatherSetSun == null) weatherSetSun = "&eWeather set to sunny in world {WORLD}.";
        if (weatherSetThunder == null) weatherSetThunder = "&hWeather set to storm in world {WORLD}.";

        if (joinMsg == null) joinMsg = "&2{DISPLAYNAME}&2 joined the game!";
        if (quitMsg == null) quitMsg = "&4{DISPLAYNAME}&4 left the game!";
        if (firstJoinMsg == null) firstJoinMsg = "&9{DISPLAYNAME}&9 joined the game for the first time!";
        if (welcomeMsg == null) welcomeMsg = "&bWelcome to the server!";
    }

    public String getCurrency(double amount) {
        if (amount == 1) return currencySingular;
        else return currencyPlural;
    }
}
