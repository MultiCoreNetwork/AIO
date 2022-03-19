package it.multicoredev.aio.storage.data;

import com.google.common.base.Preconditions;
import it.multicoredev.aio.AIO;
import it.multicoredev.aio.api.IKits;
import it.multicoredev.aio.api.models.Kit;
import it.multicoredev.mclib.json.JsonConfig;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Copyright © 2022 by Daniele Patella. All rights reserved.
 * This file is part of AIO.
 * <p>
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING,
 * BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY,
 * OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */
public class KitStorage extends JsonConfig implements IKits {
    private List<Kit> kits;

    @Override
    public void init() {
        if (kits == null) kits = new ArrayList<>();
    }

    @Override
    public List<Kit> getKits() {
        return Collections.unmodifiableList(kits);
    }

    @Override
    @Nullable
    public Kit getKit(@NotNull String name) {
        Preconditions.checkNotNull(name);

        return kits.stream().filter(kit -> kit.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    @Override
    public boolean kitExists(@NotNull String name) {
        Preconditions.checkNotNull(name);

        return kits.stream().anyMatch(kit -> kit.getName().equalsIgnoreCase(name));
    }

    @Override
    public synchronized boolean addKit(@NotNull Kit kit) {
        Preconditions.checkNotNull(kit);

        if (kitExists(kit.getName())) return false;

        kits.add(kit);
        ((AIO) AIO.getInstance()).saveKits();
        return true;
    }

    public List<String> getKitsNameByPerm(CommandSender sender) {
        return kits.stream()
                .map(Kit::getName)
                .filter(name -> sender.hasPermission("aio.kit." + name) || sender.hasPermission("aio.kit.*"))
                .collect(Collectors.toList());
    }
}