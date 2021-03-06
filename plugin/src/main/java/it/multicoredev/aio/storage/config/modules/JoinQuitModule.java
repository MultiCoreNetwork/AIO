package it.multicoredev.aio.storage.config.modules;

import com.google.gson.annotations.SerializedName;
import it.multicoredev.aio.api.models.Module;
import it.multicoredev.mbcore.spigot.Chat;
import org.bukkit.Sound;

import java.util.Arrays;
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
public class JoinQuitModule extends Module {
    @SerializedName("motd_delay")
    public Long motdDelay;
    @SerializedName("override_join_message")
    public Boolean overrideJoinMessage;
    @SerializedName("override_quit_message")
    public Boolean overrideQuitMessage;
    @SerializedName("show_first_join_message")
    public Boolean showFirstJoinMessage;
    @SerializedName("send_welcome_message")
    public Boolean sendWelcomeMessage;
    @SerializedName("send_motd")
    public Boolean sendMotd;
    @SerializedName("play_jingle_on_first_join")
    public Boolean playJingleOnFirstJoin;
    public List<String> jingle;
    @SerializedName("jingle_sound")
    public String jingleSound;
    @SerializedName("jingle_volume")
    public Float jingleVolume;

    public JoinQuitModule() {
        super("join_quit");
    }

    @Override
    public void init() {
        if (motdDelay == null) motdDelay = 3L;
        if (overrideJoinMessage == null) overrideJoinMessage = true;
        if (overrideQuitMessage == null) overrideQuitMessage = true;
        if (showFirstJoinMessage == null) showFirstJoinMessage = true;
        if (sendMotd == null) sendMotd = true;
        if (sendWelcomeMessage == null) sendWelcomeMessage = true;
        if (playJingleOnFirstJoin == null) playJingleOnFirstJoin = false;
        if (jingle == null) jingle = Arrays.asList(
                "p:1.4",
                "s:250",
                "p:1.1",
                "s:250",
                "p:1.2",
                "s:250",
                "p:0.8",
                "s:800",
                "p:0.8",
                "s:250",
                "p:1.2",
                "s:250",
                "p:1.4",
                "s:250",
                "p:1.1");
        if (jingleSound == null) jingleSound = "BLOCK_NOTE_BLOCK_BELL";
        if (jingleVolume == null) jingleVolume = 1.0f;
    }

    @Override
    public boolean isValid() {
        try {
            Sound.valueOf(jingleSound);
        } catch (Exception ignored) {
            jingleSound = Sound.BLOCK_NOTE_BLOCK_BELL.name();
            Chat.warning("&6Jingle sound is not valid! https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/Sound.html");
            return false;
        }

        return true;
    }
}
