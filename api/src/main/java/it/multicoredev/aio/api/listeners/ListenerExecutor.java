package it.multicoredev.aio.api.listeners;

import com.google.common.base.Preconditions;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.plugin.EventExecutor;
import org.jetbrains.annotations.NotNull;

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
public abstract class ListenerExecutor<T extends Event> implements EventExecutor, Listener {
    private final Class<T> eventClass;

    public ListenerExecutor(@NotNull Class<T> eventClass) {
        Preconditions.checkNotNull(eventClass);

        this.eventClass = eventClass;
    }

    /**
     * Get the event class.
     *
     * @return The event class.
     */
    public Class<T> getEventClass() {
        return eventClass;
    }

    /**
     * Call the onEvent method of the listener.
     *
     * @param listener The listener to call.
     * @param event    The event that calls the listener.
     */
    @Override
    public void execute(@NotNull Listener listener, @NotNull Event event) {
        if (!eventClass.isInstance(event)) return;
        onEvent((T) event);
    }

    /**
     * Called when the event is called.
     *
     * @param event The event that calls the listener.
     */
    public abstract void onEvent(T event);
}
