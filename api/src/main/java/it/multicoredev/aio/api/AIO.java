package it.multicoredev.aio.api;

import it.multicoredev.aio.api.listeners.IListenerRegistry;
import it.multicoredev.aio.api.tp.ITeleportManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

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
public abstract class AIO extends JavaPlugin {
    private static AIO aio;

    /**
     * Get an instance of the AIO API.
     *
     * @return the instance of the API.
     */
    public static AIO getInstance() {
        return aio;
    }

    /**
     * Get an instance of the {@link IModuleManager} interface.
     *
     * @return the instance of the {@link IModuleManager}.
     */
    public abstract IModuleManager getModuleManager();

    /**
     * Get an instance of the {@link ICommandRegistry} interface.
     *
     * @return the instance of the {@link ICommandRegistry}.
     */
    public abstract ICommandRegistry getCommandRegistry();

    /**
     * Get an instance of the {@link IListenerRegistry} interface.
     *
     * @return the instance of the {@link IListenerRegistry}-
     */
    public abstract IListenerRegistry getListenerRegistry();

    /**
     * Get an instance of the {@link ITeleportManager} interface.
     *
     * @return the instance of the {@link ITeleportManager}.
     */
    public abstract ITeleportManager getTeleportManager();

    /**
     * Get an instance of the {@link IStorage} interface.
     *
     * @return the instance of the {@link IStorage}.
     */
    public abstract IStorage getStorage();

    /**
     * Get an instance of the {@link IEconomy} interface.
     *
     * @return the instance of the {@link IEconomy} or null if module is disabled or Vault is missing.
     */
    @Nullable public abstract IEconomy getEconomy();
}
