package it.multicoredev.aio.listeners.player;

import it.multicoredev.aio.AIO;
import it.multicoredev.aio.api.models.User;
import it.multicoredev.aio.listeners.PluginListenerExecutor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.jetbrains.annotations.NotNull;

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
public class PlayerTeleportListener extends PluginListenerExecutor<PlayerTeleportEvent> {

    public PlayerTeleportListener(Class<PlayerTeleportEvent> eventClass, AIO aio) {
        super(eventClass, aio);
    }


    @Override
    public void onEvent(@NotNull PlayerTeleportEvent event) {
        Location from = event.getFrom();
        Player player = event.getPlayer();

        if (event.getCause().equals(PlayerTeleportEvent.TeleportCause.ENDER_PEARL) ||
                event.getCause().equals(PlayerTeleportEvent.TeleportCause.NETHER_PORTAL) ||
                event.getCause().equals(PlayerTeleportEvent.TeleportCause.END_PORTAL) ||
                event.getCause().equals(PlayerTeleportEvent.TeleportCause.SPECTATE) ||
                event.getCause().equals(PlayerTeleportEvent.TeleportCause.END_GATEWAY) ||
                event.getCause().equals(PlayerTeleportEvent.TeleportCause.CHORUS_FRUIT))
            return;

        User user = aio.getStorage().getUser(player.getUniqueId());
        if (user != null) {
            user.setLastLocation(from);
            aio.getStorage().updateUserAsync(user);
        }
    }
}
