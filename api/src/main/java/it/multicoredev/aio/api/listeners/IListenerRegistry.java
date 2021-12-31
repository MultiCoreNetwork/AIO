package it.multicoredev.aio.api.listeners;

import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Copyright © 2021 - 2022 by Lorenzo Magni & Daniele Patella
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
public interface IListenerRegistry {

    /**
     * Register a {@link ListenerCompound} in Spigot.
     *
     * @param listener the listener to be registered.
     * @param priority the priority of the listener.
     * @param plugin   the plugin that register the listener.
     */
    void registerListener(@NotNull ListenerCompound<? extends Event> listener, @NotNull EventPriority priority, @NotNull Plugin plugin);

    /**
     * Unregister a {@link ListenerCompound} from Spigot.
     *
     * @param listener the listener to be unregistered.
     */
    void unregisterListener(@NotNull ListenerCompound<? extends Event> listener);

    /**
     * Unregister all the {@link ListenerCompound} from Spigot registered by a plugin.
     *
     * @param plugin the plugin that register the listener.
     */
    void unregisterListeners(@NotNull Plugin plugin);

    /**
     * Unregister all the {@link ListenerCompound} in the registry from Spigot;
     */
    void unregisterListeners();

    /**
     * Get all the {@link ListenerCompound} registered in the registry by a plugin.
     *
     * @param plugin the plugin that registered the commands.
     * @return a List of {@link ListenerCompound} registered by the plugin,
     * empty List if no command is registered by that plugin
     */
    List<ListenerCompound<? extends Event>> getRegisteredListeners(@NotNull Plugin plugin);

    /**
     * Get all the {@link ListenerCompound} registered in the registry.
     *
     * @return a List of {@link ListenerCompound} registered,
     * empty List if no command is registered by that plugin
     */
    List<ListenerCompound<? extends Event>> getRegisteredListeners();

    /**
     * Change the priority of a listener.
     *
     * @param listener    The event of which to change the priority.
     * @param newPriority The new priority for the listener.
     * @return true if the priority was changed, false otherwise.
     */
    boolean setListenerPriority(@NotNull ListenerCompound<? extends Event> listener, @NotNull EventPriority newPriority);
}
