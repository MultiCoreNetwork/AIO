package it.multicoredev.aio.storage.data;

import com.google.common.base.Preconditions;
import it.multicoredev.aio.AIO;
import it.multicoredev.aio.api.IWarpStorage;
import it.multicoredev.aio.api.models.Warp;
import it.multicoredev.mbcore.spigot.Chat;
import it.multicoredev.mclib.json.JsonConfig;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
public class WarpStorage extends JsonConfig implements IWarpStorage {
    private List<Warp> warps;

    public WarpStorage() {
        init();
    }

    @Override
    public void init() {
        if (warps == null) warps = new ArrayList<>();
    }

    @Override
    public void saveWarps() {
        AIO aio = (AIO) AIO.getInstance();

        try {
            aio.saveWarps();
        } catch (Exception e) {
            Chat.severe(e.getMessage());
            if (AIO.debug) e.printStackTrace();
        }
    }

    @Override
    public boolean existsWarp(String warpName) {
        for (Warp warp : warps) {
            if (warp.getName().equalsIgnoreCase(warpName)) return true;
        }

        return false;
    }

    @Override
    public Warp getWarp(String warpName) {
        for (Warp warp : warps) {
            if (warp.getName().equalsIgnoreCase(warpName)) return warp;
        }

        return null;
    }

    @Override
    public List<String> getWarpNames(CommandSender sender) {
        List<String> warpList = new ArrayList<>();

        if (warps.isEmpty()) return warpList;

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

    @Override
    public boolean createWarp(String name, Location location, boolean global) {
        Preconditions.checkNotNull(location);
        Preconditions.checkNotNull(name);

        if (existsWarp(name)) return false;
        warps.add(new Warp(location, name, global));
        return true;
    }

    @Override
    public boolean deleteWarp(String name) {
        Preconditions.checkNotNull(name);

        if (!existsWarp(name)) return false;
        warps.removeIf(warp -> warp.getName().equalsIgnoreCase(name));
        return true;
    }
}
