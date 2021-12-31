package it.multicoredev.aio.listeners.player;

import it.multicoredev.aio.AIO;
import it.multicoredev.aio.api.User;
import it.multicoredev.aio.listeners.PluginListenerExecutor;
import it.multicoredev.aio.storage.config.modules.JoinQuitModule;
import it.multicoredev.aio.utils.Utils;
import it.multicoredev.mbcore.spigot.Chat;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Date;

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
public class PlayerQuitListener extends PluginListenerExecutor<PlayerQuitEvent> {
    private final JoinQuitModule joinAndQuitModule;

    public PlayerQuitListener(AIO aio) {
        super(aio);
        this.joinAndQuitModule = aio.getModuleManager().getModule(JoinQuitModule.class);
    }

    @Override
    public void onEvent(@NotNull PlayerQuitEvent event) {
        Player player = event.getPlayer();

        if (storage.userExists(player)) {
            User user = storage.getUser(player);
            user.setLogoutLocation(player.getLocation());
            user.setLogin(null);

            user.addPlayTime(new Date().getTime() - user.getLogin());

            storage.updateUserAsync(user);
        }

        if (config.modules.get("join_and_quit")) {
            if (!joinAndQuitModule(event)) return;
        }
    }

    private boolean joinAndQuitModule(PlayerQuitEvent event) {
        if (!joinAndQuitModule.overrideQuitMessage) return true;
        event.setQuitMessage(null);

        Player player = event.getPlayer();

        sendQuitMessage(player, localization.quitMsg);

        return true;
    }

    private void sendQuitMessage(Player player, String msg) {
        msg = msg.replace("{DISPLAYNAME}", player.getDisplayName()).replace("{NAME}", player.getName());
        if (AIO.PAPI) PlaceholderAPI.setPlaceholders(player, msg);

        if (Utils.isVanished(player)) Chat.broadcast(Chat.getTranslated(msg), "pv.see");
        else Chat.broadcast(Chat.getTranslated(msg));
    }
}
