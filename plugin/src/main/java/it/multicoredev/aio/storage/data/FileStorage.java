package it.multicoredev.aio.storage.data;

import com.google.common.base.Preconditions;
import it.multicoredev.aio.AIO;
import it.multicoredev.aio.api.IStorage;
import it.multicoredev.aio.api.User;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

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
public class FileStorage implements IStorage {
    private final AIO aio;
    private final File usersDir;

    public FileStorage(@NotNull AIO aio) throws Exception {
        this.aio = aio;
        usersDir = new File(aio.getDataFolder(), "users");

        if (!usersDir.exists() || !usersDir.isDirectory()) {
            if (!usersDir.mkdirs()) throw new IOException("Failed to create users directory.");
        }
    }

    @Override
    public boolean userExists(@NotNull OfflinePlayer player) {
        Preconditions.checkNotNull(player);

        return userExists(player.getUniqueId());
    }

    @Override
    public boolean userExists(@NotNull Player player) {
        Preconditions.checkNotNull(player);

        return userExists(player.getUniqueId());
    }

    @Override
    public boolean userExists(@NotNull UUID uuid) {
        Preconditions.checkNotNull(uuid);

        if (aio.getUserFromCache(uuid) != null) return true;

        File file = new File(usersDir, uuid + ".json");
        if (!file.exists() || !file.isFile()) return false;

        try {
            User user = AIO.GSON.load(file, User.class);
            return user.validate();
        } catch (Exception e) {
            if (AIO.debug) e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean userExists(@NotNull String name) {
        Preconditions.checkNotNull(name);
        Preconditions.checkArgument(!name.trim().isEmpty(), name);

        UUID uuid = aio.getUserUUID(name);
        if (uuid == null) return false;
        return userExists(uuid);
    }

    @Override
    @Nullable
    public User getUser(@NotNull OfflinePlayer player) {
        Preconditions.checkNotNull(player);

        return getUser(player.getUniqueId());
    }

    @Override
    @Nullable
    public User getUser(@NotNull Player player) {
        Preconditions.checkNotNull(player);

        return getUser(player.getUniqueId());
    }

    @Override
    @Nullable
    public User getUser(@NotNull UUID uuid) {
        Preconditions.checkNotNull(uuid);

        User user = aio.getUserFromCache(uuid);
        if (user != null) return user;

        File file = new File(usersDir, uuid + ".json");

        try {
            return AIO.GSON.load(file, User.class);
        } catch (Exception e) {
            if (AIO.debug) e.printStackTrace();
            return null;
        }
    }

    @Override
    @Nullable
    public User getUser(@NotNull String name) {
        Preconditions.checkNotNull(name);
        Preconditions.checkArgument(!name.trim().isEmpty(), name);

        UUID uuid = aio.getUserUUID(name);
        if (uuid == null) return null;
        return getUser(uuid);
    }

    @Override
    @Nullable
    public User searchUser(@NotNull String name) {
        Preconditions.checkNotNull(name);
        Preconditions.checkArgument(!name.trim().isEmpty(), name);

        File[] files = usersDir.listFiles();
        if (files == null || files.length == 0) return null;

        for (File file : files) {
            try {
                User user = AIO.GSON.load(file, User.class);
                if (user.getName().equalsIgnoreCase(name)) return user;
            } catch (Exception ignored) {
            }
        }

        return null;
    }

    @Override
    public void updateUserAsync(@NotNull User user) {
        Bukkit.getScheduler().runTaskAsynchronously(aio, () -> updateUser(user));
    }

    @Override
    public boolean updateUser(@NotNull User user) {
        Preconditions.checkNotNull(user);

        //TODO users cache may need an update

        File file = new File(usersDir, user.getUniqueId() + ".json");

        try {
            AIO.GSON.save(user, file);
            return true;
        } catch (IOException e) {
            if (AIO.debug) e.printStackTrace();
            return false;
        }
    }

    @Override
    public void registerUserAsync(@NotNull User user) {
        Bukkit.getScheduler().runTaskAsynchronously(aio, () -> registerUser(user));
    }

    @Override
    public boolean registerUser(@NotNull User user) {
        Preconditions.checkNotNull(user);

        File file = new File(usersDir, user.getUniqueId() + ".json");

        try {
            AIO.GSON.save(user, file);
            return true;
        } catch (IOException e) {
            if (AIO.debug) e.printStackTrace();
            return false;
        }
    }
}
