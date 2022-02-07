package it.multicoredev.aio.storage.config;

import com.google.common.base.Preconditions;
import it.multicoredev.aio.AIO;
import it.multicoredev.aio.api.IModuleManager;
import it.multicoredev.aio.api.Module;
import it.multicoredev.aio.storage.config.modules.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
    public static final Map<Class<? extends Module>, String> DEF_MODULES = new HashMap<>();
    private final AIO aio;
    private final Map<String, Module> modules = new HashMap<>();

    static {
        DEF_MODULES.put(ChatModule.class, "chat");
        DEF_MODULES.put(CommandAliasesModule.class, "command_aliases");
        DEF_MODULES.put(EconomyModule.class, "economy");
        DEF_MODULES.put(JoinQuitModule.class, "join_quit");
        DEF_MODULES.put(PingModule.class, "ping");
        DEF_MODULES.put(SpawnModule.class, "spawn");
    }

    public ModuleManager(AIO aio) {
        this.aio = aio;
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
    @Nullable
    @SuppressWarnings("unchecked")
    public <T> T getModule(@NotNull Class<? extends Module> type) {
        Preconditions.checkNotNull(type);

        for (Module module : modules.values()) {
            if (module.getClass().equals(type)) return (T) module;
        }

        return null;
    }

    @Override
    public boolean registerModule(@NotNull Module module) {
        Preconditions.checkNotNull(module);

        if (modules.containsKey(module.getName())) return false;
        modules.put(module.getName(), module);
        return true;
    }

    public boolean isModuleEnabled(@NotNull Class<? extends Module> type) {
        Preconditions.checkNotNull(type);
        Preconditions.checkArgument(DEF_MODULES.containsKey(type));

        return aio.getConfiguration().modules.get(DEF_MODULES.get(type));
    }
}
