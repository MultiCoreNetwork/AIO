package it.multicoredev.aio.storage.config.sections;

import com.google.gson.annotations.SerializedName;
import it.multicoredev.mclib.json.JsonConfig;

import java.util.HashMap;
import java.util.Map;

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
public class CommandCooldownSection extends JsonConfig {
    @SerializedName("commands_cooldown_enabled")
    public Boolean cooldownEnabled;
    @SerializedName("commands_cooldown")
    public Map<String, Integer> commandsCooldown;

    public CommandCooldownSection() {
        init();
    }

    @Override
    protected void init() {
        if (cooldownEnabled == null) cooldownEnabled = false;
        if (commandsCooldown == null) commandsCooldown = new HashMap<>();
    }

    public int getCommandCooldown(String command) {
        for (Map.Entry<String, Integer> cmd : commandsCooldown.entrySet()) {
            if (cmd.getKey().equalsIgnoreCase(command) && cmd.getValue() > 0) return cmd.getValue();
        }

        return 0;
    }

    public boolean hasCommandCooldown(String command) {
        for (Map.Entry<String, Integer> cmd : commandsCooldown.entrySet()) {
            if (cmd.getKey().equalsIgnoreCase(command) && cmd.getValue() > 0) return true;
        }

        return false;
    }
}
