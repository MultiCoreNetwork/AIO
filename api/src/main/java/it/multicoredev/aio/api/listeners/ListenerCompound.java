package it.multicoredev.aio.api.listeners;

import com.google.common.base.Preconditions;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
public class ListenerCompound<T extends Event> {
    private final ListenerExecutor<T> listenerExecutor;
    private EventPriority priority = EventPriority.NORMAL;
    private Listener listener;

    /**
     * Create a custom listener with different priorities.
     *
     * @param listener An instance of the {@link ListenerExecutor} that contains the listener code.
     */
    public ListenerCompound(@NotNull ListenerExecutor<T> listener) {
        Preconditions.checkNotNull(listener);

        this.listenerExecutor = listener;
    }

    /**
     * Get the {@link Listener} given a priority. - DO NOT USE THIS METHOD DIRECTLY.
     *
     * @param priority The priority of the listener.
     * @return An instance of the listener with the given priority.
     */
    public Listener getListener(@NotNull EventPriority priority) {
        Preconditions.checkNotNull(priority);
        this.priority = priority;

        if (priority == EventPriority.MONITOR) listener = new ListenerMonitor<>(listenerExecutor);
        else if (priority == EventPriority.HIGHEST) listener = new ListenerHighest<>(listenerExecutor);
        else if (priority == EventPriority.HIGH) listener = new ListenerHigh<>(listenerExecutor);
        else if (priority == EventPriority.NORMAL) listener = new ListenerNormal<>(listenerExecutor);
        else if (priority == EventPriority.LOW) listener = new ListenerLow<>(listenerExecutor);
        else if (priority == EventPriority.LOWEST) listener = new ListenerLowest<>(listenerExecutor);
        else listener = new ListenerNormal<>(listenerExecutor);
        
        return listener;
    }

    /**
     * Get the priority of the listener.
     *
     * @return the priority of the listener or EventPriority.NORMAL if not set.
     */
    public EventPriority getPriority() {
        return priority;
    }

    /**
     * Get the {@link Listener} associated to the compound.
     *
     * @return the listener associated to the compound or null if {@link #getListener(EventPriority)} has not been called.
     */
    public @Nullable Listener getListener() {
        return listener;
    }
}
