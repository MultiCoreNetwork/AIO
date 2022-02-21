package it.multicoredev.aio.storage.config.sections;

import com.google.gson.annotations.SerializedName;
import it.multicoredev.mclib.json.JsonConfig;

import java.util.List;
import java.util.Locale;

/**
 * Copyright © 2021 - 2022 by Lorenzo Magni
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
public class AfkSection extends JsonConfig {
    public static final int AFK_SECONDS_COOLDOWN = 5;

    public static final String BROADCAST_EVERYBODY = "everybody";
    public static final String BROADCAST_WITH_PERM = "perm";
    public static final String BROADCAST_NO = "no";

    @SerializedName("afk_cooldown")
    public Integer afkSecondsCooldown;
    public transient long afkMillisecondsCooldown;

    @SerializedName("afk_remove_on_message")
    public Boolean afkRemoveOnMessage;
    @SerializedName("afk_remove_on_movement")
    public Boolean afkRemoveOnMovement;
    @SerializedName("afk_remove_on_command")
    public Boolean afkRemoveOnCommand;

    @SerializedName("afk_invulnerability")
    public Boolean afkInvulnerability;

    @SerializedName("broadcast")
    public String broadcastType;
    public transient boolean doNotBroadcast;
    public transient boolean broadcastEverybody;

    @SerializedName("broadcast_permission")
    public String broadcastPermission;

    @SerializedName("afk_enter_commands")
    public List<String> afkEnterCommands;
    @SerializedName("afk_leave_commands")
    public List<String> afkLeaveCommands;

    public AfkSection() {
        init();
    }

    @Override
    protected void init() {
        if (afkSecondsCooldown == null) {
            afkSecondsCooldown = AFK_SECONDS_COOLDOWN;
        } else if (afkSecondsCooldown <= 0) {
            afkSecondsCooldown = 1;
        }

        afkMillisecondsCooldown = afkSecondsCooldown * 1000L;

        if (afkRemoveOnMessage == null) afkRemoveOnMessage = true;
        if (afkRemoveOnMovement == null) afkRemoveOnMovement = true;
        if (afkRemoveOnCommand == null) afkRemoveOnCommand = true;

        if (afkInvulnerability == null) afkInvulnerability = true;

        if (broadcastType == null) {
            broadcastType = BROADCAST_WITH_PERM; // Both doNotBroadcast and broadcastEverybody are already false
        } else {
            switch (broadcastType.toLowerCase(Locale.ROOT)) {
                case BROADCAST_EVERYBODY:
                    broadcastEverybody = true;
                    break;
                case BROADCAST_WITH_PERM:
                    // Both doNotBroadcast and broadcastEverybody are already false
                    break;
                case BROADCAST_NO:
                    doNotBroadcast = true;
                    break;
                default:
                    broadcastType = BROADCAST_WITH_PERM;
                    // Both doNotBroadcast and broadcastEverybody are already false
                    break;
            }
        }

        if (broadcastPermission == null || broadcastPermission.trim().isEmpty())
            broadcastPermission = "aio.afk.broadcast";

        if (afkEnterCommands == null) afkEnterCommands = List.of();
        if (afkLeaveCommands == null) afkLeaveCommands = List.of();
    }
}
