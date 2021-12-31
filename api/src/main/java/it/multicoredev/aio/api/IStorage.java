package it.multicoredev.aio.api;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

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
public interface IStorage {

    /**
     * Check if the data of an {@link OfflinePlayer} exists in the storage.
     *
     * @param player the player to check.
     * @return true if the data exists, otherwise false.
     */
    boolean userExists(@NotNull OfflinePlayer player);

    /**
     * Check if the data of an {@link Player} exists in the storage.
     *
     * @param player the player to check.
     * @return true if the data exists, otherwise false.
     */
    boolean userExists(@NotNull Player player);

    /**
     * Check if the data of a player exists in the storage.
     *
     * @param uuid the uuid of the player to check.
     * @return true if the data exists, otherwise false.
     */
    boolean userExists(@NotNull UUID uuid);

    /**
     * Check if the data of a player exists in the storage.
     *
     * @param name the name of the player to check.
     * @return true if the data exists, otherwise false.
     */
    boolean userExists(@NotNull String name);

    /**
     * Get an {@link User} from the storage.
     *
     * @param player the player whose data is to be taken.
     * @return the data of the player or null if the data does not exist or in case of error
     */
    @Nullable User getUser(@NotNull OfflinePlayer player);

    /**
     * Get an {@link User} from the storage.
     *
     * @param player the player whose data is to be taken.
     * @return the data of the player or null if the data does not exist or in case of error
     */
    @Nullable User getUser(@NotNull Player player);

    /**
     * Get an {@link User} from the storage.
     *
     * @param uuid the uuid of the player whose data is to be taken.
     * @return the data of the player or null if the data does not exist or in case of error
     */
    @Nullable User getUser(@NotNull UUID uuid);

    /**
     * Get an {@link User} from the storage.
     *
     * @param name the name of the player whose data is to be taken.
     * @return the data of the player or null if the data does not exist or in case of error
     */
    @Nullable User getUser(@NotNull String name);

    /**
     * Search an {@link User} from the storage using the name as a query.
     *
     * @param name the name of the player to search.
     * @return the data of the player or null if the data does not exist or in case of error
     */
    @Nullable User searchUser(@NotNull String name);

    /**
     * Update asynchronously the data of a {@link User} inside the storage.
     *
     * @param user the user to be updated.
     */
    void updateUserAsync(@NotNull User user);

    /**
     * Update the data of a {@link User} inside the storage.
     *
     * @param user the user to be updated.
     * @return true if this operation is successful, otherwise false.
     */
    boolean updateUser(@NotNull User user);

    /**
     * Register asynchronously the data of a {@link User} inside the storage
     *
     * @param user the user to be saved.
     */
    void registerUserAsync(@NotNull User user);

    /**
     * Register the data of a {@link User} inside the storage
     *
     * @param user the user to be saved.
     * @return true if this operation is successful, otherwise false.
     */
    boolean registerUser(@NotNull User user);
}
