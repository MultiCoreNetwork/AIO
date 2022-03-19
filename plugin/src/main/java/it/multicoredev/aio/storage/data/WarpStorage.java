package it.multicoredev.aio.storage.data;

import com.google.common.base.Preconditions;
import it.multicoredev.aio.AIO;
import it.multicoredev.aio.api.IWarps;
import it.multicoredev.aio.api.models.Warp;
import it.multicoredev.mclib.json.JsonConfig;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
public class WarpStorage extends JsonConfig implements IWarps {
    private List<Warp> warps;

    @Override
    public void init() {
        if (warps == null) warps = new ArrayList<>();
    }

    @Override
    public List<Warp> getWarps() {
        return Collections.unmodifiableList(warps);
    }

    @Override
    public @Nullable Warp getWarp(@NotNull String name) {
        Preconditions.checkNotNull(name);

        return warps.stream().filter(warp -> warp.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    @Override
    public boolean warpExists(@NotNull String name) {
        Preconditions.checkNotNull(name);

        return warps.stream().anyMatch(warp -> warp.getName().equalsIgnoreCase(name));
    }

    @Override
    public synchronized boolean addWarp(@NotNull Warp warp) {
        Preconditions.checkNotNull(warp);

        if (warpExists(warp.getName())) return false;

        warps.add(warp);
        ((AIO) AIO.getInstance()).saveWarps();
        return true;
    }

    @Override
    public synchronized void deleteWarp(@NotNull Warp warp) {
        Preconditions.checkNotNull(warp);

        warps.remove(warp);
        ((AIO) AIO.getInstance()).saveWarps();
    }

    public List<String> getWarpsNameByPerm(CommandSender sender) {
        if (!(sender instanceof Player)) return warps.stream().map(Warp::getName).collect(Collectors.toList());

        List<Warp> filteredWarps = warps.stream()
                .filter(warp -> sender.hasPermission("aio.warp." + warp.getName()) || sender.hasPermission("aio.warp.*"))
                .collect(Collectors.toList());

        if (sender.hasPermission("aio.warp.local-bypass")) return filteredWarps.stream().map(Warp::getName).collect(Collectors.toList());

        Location location = ((Player) sender).getLocation();
        if (location.getWorld() == null) return filteredWarps.stream().map(Warp::getName).collect(Collectors.toList());

        return filteredWarps.stream()
                .filter(warp -> Objects.equals(warp.getLocation().getWorld(), location.getWorld()))
                .map(Warp::getName)
                .collect(Collectors.toList());
    }
}
