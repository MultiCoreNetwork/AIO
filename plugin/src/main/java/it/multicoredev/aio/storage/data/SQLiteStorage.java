package it.multicoredev.aio.storage.data;

import it.multicoredev.aio.api.IStorage;
import it.multicoredev.aio.api.models.User;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

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
public class SQLiteStorage implements IStorage {
    @Override
    public boolean userExists(@NotNull OfflinePlayer player) {
        return false;
    }

    @Override
    public boolean userExists(@NotNull Player player) {
        return false;
    }

    @Override
    public boolean userExists(@NotNull UUID uuid) {
        return false;
    }

    @Override
    public boolean userExists(@NotNull String name) {
        return false;
    }

    @Override
    public @Nullable User getUser(@NotNull OfflinePlayer player) {
        return null;
    }

    @Override
    public @Nullable User getUser(@NotNull Player player) {
        return null;
    }

    @Override
    public @Nullable User getUser(@NotNull UUID uuid) {
        return null;
    }

    @Override
    public @Nullable User getUser(@NotNull String name) {
        return null;
    }

    @Override
    public @Nullable User searchUser(@NotNull String name) {
        return null;
    }

    @Override
    public void updateUserAsync(@NotNull User user) {

    }

    @Override
    public boolean updateUser(@NotNull User user) {
        return false;
    }

    @Override
    public void registerUserAsync(@NotNull User user) {

    }

    @Override
    public boolean registerUser(@NotNull User user) {
        return false;
    }
}
