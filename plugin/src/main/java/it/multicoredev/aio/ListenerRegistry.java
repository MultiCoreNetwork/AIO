package it.multicoredev.aio;

import com.google.common.base.Preconditions;
import it.multicoredev.aio.api.listeners.IListenerRegistry;
import it.multicoredev.aio.api.listeners.ListenerCompound;
import it.multicoredev.mbcore.spigot.Chat;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static it.multicoredev.aio.AIO.debug;

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
public class ListenerRegistry implements IListenerRegistry {
    private final Map<Plugin, List<ListenerCompound<? extends Event>>> listeners = new HashMap<>();

    @Override
    public void registerListener(@NotNull ListenerCompound<? extends Event> listener, @NotNull EventPriority priority, @NotNull Plugin plugin) {
        Preconditions.checkNotNull(listener);
        Preconditions.checkNotNull(priority);
        Preconditions.checkNotNull(plugin);

        Bukkit.getPluginManager().registerEvents(listener.getListener(priority), plugin);

        if (listeners.containsKey(plugin)) listeners.get(plugin).add(listener);
        else listeners.put(plugin, new ArrayList<>(Collections.singleton(listener)));

        if (debug)
            Chat.info(String.format("&aL&r Registered listener &a%s&r.", listener.getListener().getClass().getSimpleName()));
    }

    @Override
    public void unregisterListener(@NotNull ListenerCompound<? extends Event> listener) {
        boolean listenerRegistered = false;
        for (Map.Entry<Plugin, List<ListenerCompound<? extends Event>>> entry : listeners.entrySet()) {
            if (entry.getValue().contains(listener)) {
                entry.getValue().remove(listener);
                if (entry.getValue().isEmpty()) listeners.remove(entry.getKey());
                listenerRegistered = true;
                break;
            }
        }

        if (!listenerRegistered) return;
        if (listener.getListener() == null) return;

        HandlerList.unregisterAll(listener.getListener());

        if (debug)
            Chat.info(String.format("&cL&r Registered listener &a%s&r.", listener.getListener().getClass().getSimpleName()));
    }

    @Override
    public void unregisterListeners(@NotNull Plugin plugin) {
        if (plugin == null || !listeners.containsKey(plugin)) return;

        listeners.get(plugin).forEach(this::unregisterListener);
        listeners.remove(plugin);
    }

    @Override
    public void unregisterListeners() {
        listeners.values().forEach(listeners -> listeners.forEach(this::unregisterListener));
        listeners.clear();
    }

    @Override
    public List<ListenerCompound<? extends Event>> getRegisteredListeners(@NotNull Plugin plugin) {
        if (plugin != null && listeners.containsKey(plugin)) return listeners.get(plugin);
        else return new ArrayList<>();
    }

    @Override
    public List<ListenerCompound<? extends Event>> getRegisteredListeners() {
        List<ListenerCompound<? extends Event>> listeners = new ArrayList<>();
        this.listeners.values().forEach(listeners::addAll);
        return listeners;
    }

    @Override
    public boolean setListenerPriority(@NotNull ListenerCompound<? extends Event> listener, @NotNull EventPriority newPriority) {
        Preconditions.checkNotNull(listener);
        Preconditions.checkNotNull(newPriority);

        Plugin plugin = null;
        for (Map.Entry<Plugin, List<ListenerCompound<? extends Event>>> entry : listeners.entrySet()) {
            if (entry.getValue().contains(listener)) {
                plugin = entry.getKey();
                break;
            }
        }

        if (plugin == null) return false;

        unregisterListener(listener);
        registerListener(listener, newPriority, plugin);
        return true;
    }
}
