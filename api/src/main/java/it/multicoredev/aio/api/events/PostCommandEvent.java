package it.multicoredev.aio.api.events;

import org.bukkit.command.CommandSender;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
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
public class PostCommandEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();

    private final CommandSender sender;
    private final String command;
    private final String[] args;
    private final boolean success;

    /**
     * PostCommandEvent is called after a command registered is executed;
     *
     * @param sender  the sender of the command.
     * @param command the command that was executed.
     * @param args    the arguments of the command.
     * @param success true if the command was executed successfully, false otherwise.
     */
    public PostCommandEvent(@NotNull CommandSender sender, @NotNull String command, @NotNull String[] args, boolean success) {
        this.sender = sender;
        this.command = command;
        this.args = args;
        this.success = success;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    /**
     * Get the sender of the command.
     *
     * @return the sender of the command.
     */
    public CommandSender getCommandSender() {
        return sender;
    }

    /**
     * Get the command that was executed.
     *
     * @return the command that was executed.
     */
    public String getCommand() {
        return command;
    }

    /**
     * Get the arguments of the command.
     *
     * @return the arguments of the command.
     */
    public String[] getArgs() {
        return args;
    }

    /**
     * Get if the command was successful.
     *
     * @return true if the command was successful, false otherwise.
     */
    public boolean isSuccess() {
        return success;
    }
}