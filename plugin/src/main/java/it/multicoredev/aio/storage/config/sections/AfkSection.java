package it.multicoredev.aio.storage.config.sections;

import com.google.gson.annotations.SerializedName;
import it.multicoredev.mclib.json.JsonConfig;

import java.util.ArrayList;
import java.util.List;

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
    @SerializedName("afk_cooldown")
    private Integer afkCooldown;

    @SerializedName("afk_remove_on_movement")
    public Boolean afkRemoveOnMovement;
    @SerializedName("afk_remove_on_click")
    public Boolean afkRemoveOnInteract;
    @SerializedName("afk_remove_on_message")
    public Boolean afkRemoveOnMessage;
    @SerializedName("afk_remove_on_command")
    public Boolean afkRemoveOnCommand;
    @SerializedName("afk_remove_on_damage")
    public Boolean afkRemoveOnDamage;

    @SerializedName("afk_invulnerability")
    public Boolean afkInvulnerability;
    @SerializedName("show_in_display_name")
    public Boolean modifyDisplayName;
    @SerializedName("afk_remove_command_blacklist")
    private List<String> afkRemoveCommandBlacklist;

    @Override
    public void init() {
        if (afkCooldown == null) afkCooldown = 60;

        if (afkRemoveOnMovement == null) afkRemoveOnMovement = true;
        if (afkRemoveOnInteract == null) afkRemoveOnInteract = true;
        if (afkRemoveOnMessage == null) afkRemoveOnMessage = true;
        if (afkRemoveOnCommand == null) afkRemoveOnCommand = true;
        if (afkRemoveOnDamage == null) afkRemoveOnDamage = false;

        if (afkInvulnerability == null) afkInvulnerability = false;
        if (modifyDisplayName == null) modifyDisplayName = false;
        if (afkRemoveCommandBlacklist == null) afkRemoveCommandBlacklist = new ArrayList<>();
    }

    public boolean hasAfkCooldown() {
        return afkCooldown > 0;
    }

    public Integer getAfkCooldown() {
        return afkCooldown;
    }

    public boolean isCommandBlacklisted(String cmd) {
        return afkRemoveCommandBlacklist.stream().anyMatch(cmd::equalsIgnoreCase);
    }
}
