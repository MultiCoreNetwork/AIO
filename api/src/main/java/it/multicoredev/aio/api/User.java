package it.multicoredev.aio.api;

import com.google.gson.annotations.SerializedName;
import it.multicoredev.aio.api.events.AfkEvent;
import it.multicoredev.aio.api.models.Home;
import it.multicoredev.mclib.json.JsonConfig;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
public class User extends JsonConfig {
    private final UUID uuid;
    private String name;
    private String nickname;
    private Integer logins;
    @SerializedName("play_time")
    private Long playTime;
    private Double money;
    @SerializedName("last_location")
    private Location lastLocation;
    @SerializedName("logout_location")
    private Location logoutLocation;
    @SerializedName("death_location")
    private Location deathLocation;
    private List<Home> homes;
    private Long login;
    private Integer rtp;
    private Boolean god;
    private Boolean fly;
    private transient boolean afk;
    private transient long afkCooldownTimestamp = -1; // Initialised with an invalid cooldown
    private transient Location afkLastLocation;

    private volatile Long lastSave = null;

    /**
     * A container of all the data stored of a player.
     *
     * @param player the owner of the data.
     */
    public User(Player player) {
        this.uuid = player.getUniqueId();
        this.name = player.getName();
        this.nickname = player.getDisplayName();
        init();
    }

    @Override
    protected void init() {
        if (nickname == null) nickname = name;
        if (logins == null) logins = 1;
        if (playTime == null) playTime = 0L;
        if (money == null) money = 0d;
        if (homes == null) homes = new ArrayList<>();
        if (rtp == null) rtp = 0;
        if (god == null) god = false;
        if (fly == null) fly = false;
    }

    /**
     * Check if data stored is valid.
     *
     * @return true if is valid, otherwise false
     */
    public boolean validate() {
        return uuid != null && name != null;
    }

    /**
     * Get the player UUID.
     *
     * @return the UUID of the player.
     */
    public UUID getUniqueId() {
        return uuid;
    }

    /**
     * Get the player name.
     *
     * @return the name of the player.
     */
    public String getName() {
        return name;
    }

    /**
     * Set the player name.
     *
     * @param name the name of the player.
     * @return this object.
     */
    public User setName(String name) {
        this.name = name;
        return this;
    }

    /**
     * Get the player nickname.
     *
     * @return the nickname of the player.
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * Set the player nickname.
     *
     * @param nickname the nickname of the player.
     * @return this object.
     */
    public User setNickname(String nickname) {
        this.nickname = nickname;
        return this;
    }

    /**
     * Get the number of times the player has logged in.
     *
     * @return the number of times the player has logged in.
     */
    public int getLogins() {
        return logins;
    }

    /**
     * Set the number of times the player has logged in.
     *
     * @param logins the number of times the player has logged in.
     * @return this object.
     */
    public User setLogins(Integer logins) {
        this.logins = logins;
        return this;
    }

    /**
     * Increment the number of times the player has logged in.
     *
     * @return this object.
     */
    public User incrementLogins() {
        this.logins++;
        return this;
    }

    /**
     * Get the last time the player has been saved.
     *
     * @return the last time the player has been saved.
     */
    public Long getLastSave() {
        return lastSave;
    }

    /**
     * Set the last time the player has been saved.
     *
     * @param lastSave the last time the player has been saved.
     */
    public void setLastSave(long lastSave) {
        this.lastSave = lastSave;
    }

    /**
     * Increase the number of times the player has logged in.
     *
     * @return this object.
     */
    public User addLogin() {
        this.logins++;
        return this;
    }

    /**
     * Get the player play time.
     *
     * @return the play time of the player.
     */
    public Long getPlayTime() {
        return playTime;
    }

    /**
     * Set the player play time.
     *
     * @param playTime the play time of the player.
     * @return this object.
     */
    public User setPlayTime(Long playTime) {
        this.playTime = playTime;
        return this;
    }

    /**
     * Increase the player play time.
     *
     * @param playTime the play time to add.
     * @return this object.
     */
    public User addPlayTime(long playTime) {
        this.playTime += playTime;
        return this;
    }

    /**
     * Get the player money.
     *
     * @return the money of the player.
     */
    public double getMoney() {
        return money != null ? money : 0;
    }

    /**
     * Set the player money.
     *
     * @param money the money of the player.
     * @return this object.
     */
    public User setMoney(double money) {
        this.money = money;
        return this;
    }

    /**
     * Deposit money to player account.
     *
     * @param amount the amount to deposit.
     * @return this object.
     */
    public User depositMoney(double amount) {
        money += amount;
        return this;
    }

    /**
     * Withdraw money from player account.
     *
     * @param amount the amount to withdraw.
     * @return this object.
     */
    public User withdrawMoney(double amount) {
        money -= amount;
        return this;
    }

    /**
     * Get the last location of the player.
     *
     * @return the last location of the player.
     */
    public Location getLastLocation() {
        return lastLocation;
    }

    /**
     * Set the last location of the player.
     *
     * @param lastLocation the last location of the player.
     * @return this object.
     */
    public User setLastLocation(@NotNull Location lastLocation) {
        this.lastLocation = lastLocation;
        return this;
    }

    /**
     * get the logout location of the player.
     *
     * @return the logout location of the player.
     */
    public Location getLogoutLocation() {
        return logoutLocation;
    }

    /**
     * Set the logout location of the player.
     *
     * @param logoutLocation the logout location of the player.
     * @return this object.
     */
    public User setLogoutLocation(@NotNull Location logoutLocation) {
        this.logoutLocation = logoutLocation;
        return this;
    }

    /**
     * Get the player death location.
     *
     * @return the death location of the player.
     */
    public Location getDeathLocation() {
        return deathLocation;
    }

    /**
     * Set the death location of the player.
     *
     * @param deathLocation the death location of the player.
     * @return this object.
     */
    public User setDeathLocation(@NotNull Location deathLocation) {
        this.deathLocation = deathLocation;
        return this;
    }

    /**
     * Get the list of player's homes.
     *
     * @return the list of player's homes.
     */
    public List<Home> getHomes() {
        return homes;
    }

    /**
     * Set the list of player's homes.
     *
     * @param homes the list of player's homes.
     * @return this object.
     */
    public User setHomes(List<Home> homes) {
        this.homes = homes;
        return this;
    }

    /**
     * Add {@link Home} to player's homes.
     *
     * @param home the home to add.
     * @return this object.
     */
    public User addHome(Home home) {
        this.homes.add(home);
        return this;
    }

    /**
     * Remove {@link Home} from player's homes.
     *
     * @param home the home to remove.
     * @return this object.
     */
    public User removeHome(Home home) {
        this.homes.remove(home);
        return this;
    }

    /**
     * Get {@link Home} by name
     *
     * @param name the name of the home.
     * @return this object.
     */
    @Nullable
    public Home getHome(String name) {
        if (homes.isEmpty()) return null;

        for (Home home : homes) {
            if (home.getName().equals(name)) return home;
        }

        return null;
    }

    /**
     * Get home names.
     *
     * @return home names
     */
    public List<String> getHomeNames() {
        if (homes == null || homes.isEmpty()) return new ArrayList<>();
        return homes.stream().map(Home::getName).collect(Collectors.toList());
    }

    /**
     * Get the login timestamp.
     *
     * @return the login timestamp.
     */
    public Long getLogin() {
        return login;
    }

    /**
     * Set the login timestamp.
     *
     * @param login the login timestamp.
     * @return this object.
     */
    public User setLogin(Long login) {
        this.login = login;
        return this;
    }

    /**
     * Get how many times the player has used the /rtp command.
     *
     * @return the number of times the player has used the /rtp command.
     */
    public Integer getRTP() {
        return rtp;
    }

    /**
     * Increase the number of times the player has used the /rtp command.
     *
     * @return this object.
     */
    public User addRTP() {
        this.rtp += 1;
        return this;
    }

    /**
     * Check if the player has god enabled.
     *
     * @return true if the player has god enabled, false otherwise.
     */
    public boolean hasGod() {
        return god != null && god;
    }

    /**
     * Set if the player has god enabled.
     *
     * @param god true if the player has god enabled, false otherwise.
     * @return this object.
     */
    public User setGod(Boolean god) {
        this.god = god;
        return this;
    }

    /**
     * Check if the player has fly enabled.
     *
     * @return true if the player has fly enabled, false otherwise.
     */
    public boolean hasFly() {
        return fly != null && fly;
    }

    /**
     * Set if the player has fly enabled.
     *
     * @param fly true if the player has fly enabled, false otherwise.
     * @return this object.
     */
    public User setFly(Boolean fly) {
        this.fly = fly;
        return this;
    }

    /**
     * Check if the player is AFK.
     *
     * @return true if the player is AFk, false otherwise.
     */
    public boolean isAfk() {
        return afk;
    }

    /**
     * Set if the player is AFK.
     * This method also updates afkLastLocation and afkCooldownTimestamp.
     *
     * @param afk true if the player is AFK, false otherwise.
     * @return this object.
     */
    public User setAfk(boolean afk) {
        if (this.afk && !afk) {
            // If player is leaving AFK mode, then resets afkLastLocation and afkCooldownTimestamp
            this.afkLastLocation = null;
            this.afkCooldownTimestamp = -1;
            Player player = Bukkit.getPlayer(uuid);
            if (player != null) { // Just to be sure
                Bukkit.getPluginManager().callEvent(new AfkEvent(player, false));
            }
            this.afk = afk;
        } else if (!this.afk && afk) {
            // Player is going AFK
            Player player = Bukkit.getPlayer(uuid);
            if (player != null) { // Just to be sure
                this.afkLastLocation = player.getLocation();
                this.afkCooldownTimestamp = System.currentTimeMillis();
                Bukkit.getPluginManager().callEvent(new AfkEvent(player, true));
            }
            this.afk = afk;
        }
        return this;
    }

    /**
     * Get the AFK cooldown timestamp.
     *
     * @return the AFK cooldown timestamp, or -1 if invalid.
     */
    public long getAfkCooldownTimestamp() {
        return afkCooldownTimestamp;
    }

    /**
     * Set the AFK cooldown timestamp.
     *
     * @param afkCooldownTimestamp the AFK cooldown timestamp.
     * @return this object.
     */
    public User setAfkCooldownTimestamp(long afkCooldownTimestamp) {
        this.afkCooldownTimestamp = afkCooldownTimestamp;
        return this;
    }

    /**
     * Get the last location of the player while they're AFK.
     * @return the last location of the player while they're AFK, or null if the player was not AFK
     */
    @Nullable
    public Location getAfkLastLocation() {
        return afkLastLocation;
    }

    /**
     * Set the last location of the player while they're AFK.
     * @param afkLastLocation the last location of the player while they're AFK, or null if invalid.
     * @return this object.
     */
    public User setAfkLastLocation(@NotNull Location afkLastLocation) {
        this.afkLastLocation = afkLastLocation;
        return this;
    }
}
