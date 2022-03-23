package it.multicoredev.aio.utils.placeholders;

import it.multicoredev.aio.AIO;
import it.multicoredev.aio.api.IStorage;
import it.multicoredev.aio.api.models.User;
import it.multicoredev.aio.storage.config.Localization;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.Nullable;

/**
 * Copyright © 2022 by Lorenzo Magni
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
public class PAPIPlaceholderHook extends PlaceholderExpansion {
    public static final String IDENTIFIER = "aio";

    public static final String AFK_PLACEHOLDER = "afk";

    private final String authors;
    private final String version;
    private final IStorage storage;
    private final Localization localization;

    public PAPIPlaceholderHook(AIO aio) {
        this.authors = '[' + String.join(", ", aio.getDescription().getAuthors()) + ']';
        this.version = aio.getDescription().getVersion();
        this.storage = aio.getStorage();
        this.localization = aio.getLocalization();
    }

    @Override
    public String getAuthor() {
        return authors;
    }

    @Override
    public String getIdentifier() {
        return IDENTIFIER;
    }

    @Override
    public String getVersion() {
        return version;
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onRequest(OfflinePlayer player, String params) {
        if (player != null) {
            User user = storage.getUser(player);

            if (user != null) {
                if (params.equalsIgnoreCase(AFK_PLACEHOLDER)) return user.isAfk() ? localization.afkPlaceholder : "";
            }
        }

        return null; //TODO Maybe change this to empty string
    }
}
