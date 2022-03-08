package it.multicoredev.aio.api;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.Collection;

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
public interface IModuleManager {

    /**
     * Register a module.
     *
     * @param module the module class to be registered.
     * @return true if the module has been registered, false if a module with the same name is already registered.
     */
    boolean registerModule(@NotNull Module module);

    /**
     * Get the module instance given the type of the module.
     * Return null if no module is found with that type.
     *
     * @param module the name of the module.
     * @param <T>  the instance of the module.
     * @return The instance of the module.
     */
    @Nullable <T extends Module> T getModule(@NotNull String module);

    /**
     * Get the module instance given the type of the module.
     * Return null if no module is found with that type.
     *
     * @param module the class of the module.
     * @param <T>  the instance of the module.
     * @return The instance of the module.
     */
    @Nullable <T extends Module> T getModule(@NotNull Class<T> module);

    /**
     * Get a collection of all the modules registered.
     *
     * @return a collection of all the modules registered.
     */
    Collection<Module> getModules();

    /**
     * Save a module to file.
     *
     * @param module the class of the module to be saved.
     * @return true if the module has been saved, otherwise false.
     */
    boolean saveModule(@NotNull Class<? extends Module> module);

    /**
     * Save a module to file.
     *
     * @param module the name of the module to be saved.
     * @return true if the module has been saved, otherwise false.
     */
    boolean saveModule(@NotNull String module);

    /**
     * Load a module from file.
     *
     * @param module the class of the module to be loaded.
     * @return true if the module has been loaded, otherwise false.
     */
    boolean loadModule(@NotNull Class<? extends Module> module);

    /**
     * Load a module from file.
     *
     * @param module the name of the module to be loaded.
     * @return true if the module has been loaded, otherwise false.
     */
    boolean loadModule(@NotNull String module);

    /**
     * Get the class of the module given the name.
     * Return null if no module is found with that name.
     *
     * @param name the name of the module.
     * @return the class of the module.
     */
    @Nullable Class<? extends Module> getModuleClass(@NotNull String name);

    /**
     * Check if a module is enabled.
     *
     * @param module the class of the module.
     * @return true if the module is enabled, otherwise false.
     */
    boolean isModuleEnabled(@NotNull Class<? extends Module> module);

    /**
     * Check if a module is enabled.
     *
     * @param module the name of the module.
     * @return true if the module is enabled, otherwise false.
     */
    boolean isModuleEnabled(@NotNull String module);

    /**
     * Get the file of a module.
     *
     * @param module the class of the module.
     * @return the file of the module.
     */
    File getModuleFile(@NotNull Class<? extends Module> module);

    /**
     * Get the file of a module.
     *
     * @param module the name of the module.
     * @return the file of the module.
     */
    File getModuleFile(@NotNull String module);

    /**
     * Check if a module file exists.
     *
     * @param module the class of the module.
     * @return the name of the module.
     */
    boolean moduleFileExists(@NotNull Class<? extends Module> module);

    /**
     * Check if a module file exists.
     *
     * @param module
     * @return
     */
    boolean moduleFileExists(@NotNull String module);
}
