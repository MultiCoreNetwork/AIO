package it.multicoredev.aio.api;

import it.multicoredev.aio.api.models.Warp;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

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
public interface IWarpStorage {

    /**
     * Save warp changes in storage.
     */
    void saveWarps();

    /**
     * Check if a warp with a certain name exists.
     *
     * @param warpName the warp name to check
     * @return true if the warp exists, otherwise false
     */
    boolean existsWarp(String warpName);

    /**
     * Get a {@link Warp} from the storage.
     *
     * @param warpName the warp name to get
     * @return the warp or null if the warp does not exists or in case of error
     */
    Warp getWarp(String warpName);

    /**
     * Get all the warp names from the storage.
     *
     * @param sender
     * @return
     */
    List<String> getWarpNames(CommandSender sender);

    /**
     * Create a new warp.
     *
     * @param name the name
     * @param location the location
     * @param global if the warp will be accessible in all worlds
     * @return true if this operation is successful, otherwise false.
     */
    boolean createWarp(String name, Location location, boolean global);

    /**
     * Delete a warp.
     *
     * @param name the name of the warp to delete
     * @return true if this operation is successful, otherwise false
     */
    boolean deleteWarp(String name);
}
