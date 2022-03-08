package it.multicoredev.aio.storage.config;

import com.google.common.base.Preconditions;
import com.google.gson.JsonSyntaxException;
import it.multicoredev.aio.AIO;
import it.multicoredev.aio.api.IModuleManager;
import it.multicoredev.aio.api.Module;
import it.multicoredev.mbcore.spigot.Chat;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Copyright &copy; 2021 - 2022 by Lorenzo Magni &amp; Daniele Patella
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
public class ModuleManager implements IModuleManager {
    private final AIO aio;
    private final File modulesDir;
    private final Map<String, Module> modules = new HashMap<>();

    public ModuleManager(AIO aio, File modulesDir) {
        this.aio = aio;
        this.modulesDir = modulesDir;
    }

    @Override
    public boolean registerModule(@NotNull Module module) {
        Preconditions.checkNotNull(module);

        if (modules.containsKey(module.getName())) return false;
        modules.put(module.getName(), module);
        return true;
    }

    @Override
    @Nullable
    @SuppressWarnings("unchecked")
    public <T extends Module> T getModule(@NotNull Class<T> module) {
        Preconditions.checkNotNull(module);
        return (T) modules.values()
                .stream()
                .filter(m -> m.getClass().equals(module))
                .findFirst()
                .orElse(null);
    }

    @Override
    @Nullable
    @SuppressWarnings("unchecked")
    public <T extends Module> T getModule(@NotNull String module) {
        Preconditions.checkNotNull(module);

        return (T) modules.getOrDefault(module, null);
    }

    @Override
    public Collection<Module> getModules() {
        return Collections.unmodifiableCollection(modules.values());
    }

    @Override
    public boolean saveModule(@NotNull Class<? extends Module> module) {
        Preconditions.checkNotNull(module);

        Module mod = getModule(module);
        if (mod == null) return false;

        File moduleFile = new File(modulesDir, mod.getName() + ".json");
        try {
            AIO.gson.save(module, moduleFile);
            return true;
        } catch (IOException e) {
            Chat.warning("&c" + e.getMessage());
            if (AIO.debug) e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean saveModule(@NotNull String module) {
        Preconditions.checkNotNull(module);

        Module mod = modules.getOrDefault(module, null);
        if (mod == null) return false;

        File moduleFile = new File(modulesDir, mod.getName() + ".json");
        try {
            AIO.gson.save(module, moduleFile);
            return true;
        } catch (IOException e) {
            Chat.warning("&c" + e.getMessage());
            if (AIO.debug) e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean loadModule(@NotNull Class<? extends Module> module) {
        Preconditions.checkNotNull(module);

        Module mod = getModule(module);
        if (mod == null) return false;

        try {
            modules.put(mod.getName(), AIO.gson.load(new File(modulesDir, mod.getName() + ".json"), module));
            return true;
        } catch (IOException | JsonSyntaxException e) {
            Chat.warning("&c" + e.getMessage());
            if (AIO.debug) e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean loadModule(@NotNull String module) {
        Preconditions.checkNotNull(module);

        Module mod = getModule(module);
        if (mod == null) return false;

        try {
            modules.put(mod.getName(), AIO.gson.load(new File(modulesDir, mod.getName() + ".json"), mod.getClass()));
            return true;
        } catch (IOException | JsonSyntaxException e) {
            Chat.warning("&c" + e.getMessage());
            if (AIO.debug) e.printStackTrace();
            return false;
        }
    }

    @Override
    @Nullable
    public Class<? extends Module> getModuleClass(@NotNull String name) {
        Preconditions.checkNotNull(name);
        Preconditions.checkArgument(!name.trim().isEmpty());

        if (modules.containsKey(name)) return modules.get(name).getClass();
        return null;
    }

    @Override
    public boolean isModuleEnabled(@NotNull Class<? extends Module> module) {
        Preconditions.checkNotNull(module);

        Module mod = getModule(module);
        if (mod == null) return false;
        return aio.getConfiguration().modules.get(mod.getName());
    }

    @Override
    public boolean isModuleEnabled(@NotNull String module) {
        Preconditions.checkNotNull(module);

        Module mod = getModule(module);
        if (mod == null) return false;
        return aio.getConfiguration().modules.get(mod.getName());
    }

    @Override
    public boolean

    boolean moduleFileExists(@NotNull Module module) {
        Preconditions.checkNotNull(module);

        File file = new File(modulesDir, module.getName() + ".json");
        return file.exists() && file.isFile();
    }
}
