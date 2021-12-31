package it.multicoredev.aio.storage.data;

import com.google.common.base.Preconditions;
import com.google.gson.annotations.Expose;
import it.multicoredev.aio.AIO;
import it.multicoredev.aio.models.Warp;
import it.multicoredev.mbcore.spigot.Chat;
import it.multicoredev.mclib.json.JsonConfig;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;
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
public class WarpStorage extends JsonConfig {
    @Expose(serialize = false, deserialize = false)
    private final AIO aio;
    @Expose(serialize = false, deserialize = false)
    private final File file;
    @Expose(serialize = true, deserialize = true)
    private List<Warp> warps;

    public WarpStorage(@NotNull AIO aio, @NotNull File file) {
        this.aio = aio;
        this.file = file;

        init();
    }

    @Override
    protected void init() {
        if (warps == null) warps = new ArrayList<>();
    }

    public void addWarp(@NotNull Location location, @NotNull String name, boolean global) {
        Preconditions.checkNotNull(location);
        Preconditions.checkNotNull(name);

        warps.add(new Warp(location, name, global));
    }

    public void save() {
        Bukkit.getScheduler().runTaskAsynchronously(aio, () -> {
            try {
                AIO.serialize(file, this);
            } catch (Exception e) {
                Chat.severe("&4" + e.getMessage());
                if (AIO.debug) e.printStackTrace();
            }
        });
    }

    public boolean existsWarp(String warpName) {
        for (Warp warp : warps) {
            if (warp.getName().equalsIgnoreCase(warpName)) return true;
        }

        return false;
    }

    public Warp getWarp(String warpName) {
        for (Warp warp : warps) {
            if (warp.getName().equalsIgnoreCase(warpName)) return warp;
        }

        return null;
    }


    public List<String> warpList(CommandSender sender) {
        List<String> warpList = new ArrayList<>();

        for (Warp warp : warps) {
            if (sender.hasPermission("aio.warp." + warp.getName())) warpList.add(warp.getName());
        }

        if (!(sender instanceof Player)) return warpList;

        Player player = (Player) sender;

        if (!player.hasPermission("aio.warp.local.bypass")) {
            warpList.removeIf(warp -> !player.getLocation().getWorld().getName().equals(warp));
        }

        return warpList;
    }

    public void createWarp(String name, Location location, boolean global) {
        warps.add(new Warp(location, name, global));
    }

    public void deleteWarp(String name) {
        warps.removeIf(warp -> warp.getName().equalsIgnoreCase(name));
    }
}
