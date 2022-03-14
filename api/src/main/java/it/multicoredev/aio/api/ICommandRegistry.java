package it.multicoredev.aio.api;

import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;

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
public interface ICommandRegistry {

    /**
     * Register a {@link BasePluginCommand} in Spigot.
     *
     * @param command the command to be registered.
     * @param plugin  the plugin that register the command.
     */
    void registerCommand(@NotNull BasePluginCommand command, Plugin plugin);

    /**
     * Unregister a {@link BasePluginCommand} from Spigot.
     *
     * @param command the command to be unregistered.
     */
    void unregisterCommand(@NotNull BasePluginCommand command);

    /**
     * Unregister all the {@link BasePluginCommand} from Spigot registered by a plugin.
     *
     * @param plugin the plugin that registered the commands.
     */
    void unregisterCommands(@NotNull Plugin plugin);

    /**
     * Unregister all {@link BasePluginCommand} in the registry from Spigot.
     */
    void unregisterCommands();

    /**
     * Get all the {@link BasePluginCommand} registered in this registry  by a plugin.
     *
     * @param plugin the plugin that registered the commands.
     * @return a List of {@link BasePluginCommand} registered by the plugin,
     * empty List if no command is registered by that plugin.
     */
    List<BasePluginCommand> getRegisteredCommands(@NotNull Plugin plugin);

    /**
     * Get all the {@link BasePluginCommand} registered in this registry.
     *
     * @return a List of {@link BasePluginCommand} registered,
     * empty List if no command is registered by that plugin.
     */
    List<BasePluginCommand> getRegisteredCommands();

    /**
     * Get all the commands names and alias registered in this registry by a plugin.
     *
     * @param plugin the plugin that registered the commands.
     * @return a List of the command names and alias registered by the plugin,
     * empty List if no command is registered by that plugin.
     */
    List<String> getCommandNames(@NotNull Plugin plugin);

    /**
     * Get all the commands names and alias registered in this registry.
     *
     * @return a List of the command names and alias registered,
     * empty List if no command is registered by that plugin.
     */
    List<String> getCommandNames();

    /**
     * Get a list of aliases for a command.
     *
     * @param command the command to get the aliases.
     * @return a list of aliases for the command.
     */
    List<String> getCommandAliases(@NotNull String command);
}
