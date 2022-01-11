package it.multicoredev.aio.storage.data;

import com.google.gson.annotations.Expose;
import it.multicoredev.aio.AIO;
import it.multicoredev.aio.models.Kit;
import it.multicoredev.mclib.json.JsonConfig;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
public class KitStorage extends JsonConfig {
    @Expose(serialize = false, deserialize = false)
    private final AIO aio;
    @Expose(serialize = false, deserialize = false)
    private final File file;
    private List<Kit> kits;

    public KitStorage(@NotNull AIO aio, @NotNull File file) {
        this.aio = aio;
        this.file = file;

        init();
    }

    @Override
    protected void init() {
        if (kits == null) kits = new ArrayList<>();
    }

    @Nullable
    public Kit getKitByName(String name) {
        for (Kit kit : kits) {
            if (kit.getName().equals(name)) return kit;
        }

        return null;
    }

    public List<String> getKitNames(CommandSender sender) {
        List<String> kitList = new ArrayList<>();

        for (Kit kit : kits) {
            if (sender.hasPermission("aio.kit." + kit.getName())) kitList.add(kit.getName());
        }

        return kitList;
    }
}