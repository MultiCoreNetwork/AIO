package it.multicoredev.aio.api;

import it.multicoredev.aio.api.models.CommandData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

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
public abstract class BasePluginCommand extends Command {

    /**
     * Create a new command.
     *
     * @param name         The base command.
     * @param description  Description of the command.
     * @param usageMessage The usage of the command used in plugin.yml.
     * @param alias        A list of aliases for the command.
     */
    public BasePluginCommand(@NotNull String name, String description, String usageMessage, List<String> alias) {
        super(
                name,
                description,
                usageMessage,
                alias != null ? alias.stream().map(a -> a.toLowerCase(Locale.ROOT)).collect(Collectors.toList()) : new ArrayList<>()
        );
    }

    /**
     * Create a new command.
     *
     * @param name         The base command.
     * @param description  Description of the command.
     * @param usageMessage The usage of the command used in plugin.yml.
     * @param alias        An array of aliases for the command.
     */
    public BasePluginCommand(@NotNull String name, @NotNull String description, String usageMessage, String[] alias) {
        this(name, description, usageMessage, Arrays.asList(alias));
    }

    /**
     * Create a new command.
     *
     * @param name         The base command.
     * @param commandData  The command data.
     */
    public BasePluginCommand(@NotNull String name, @NotNull CommandData commandData) {
        this(name, commandData.description, commandData.getUsages("default").isEmpty() ? "" : commandData.getUsages("default").get(0), commandData.alias);
    }

    /**
     * Executes the given command, returning its success.
     * If false is returned, then the "usage" plugin.yml entry for this command (if defined) will be sent to the player.
     *
     * @param sender The source of the command.
     * @param label  Alias of the command which was used.
     * @param args   Passed command arguments.
     * @return true if a valid command, otherwise false.
     */
    public abstract boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args);

    /**
     * Requests a list of possible completions for a command argument.
     *
     * @param sender The source of the command.
     * @param alias  The alias used.
     * @param args   The arguments passed to the command, including final partial argument to be completed and command label.
     * @return A List of possible completions for the final argument, default is an empty list.
     */
    @NotNull
    public List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) {
        return new ArrayList<>();
    }

    /**
     * Check if a command sender if a player.
     *
     * @param sender The source of the command.
     * @return true if the source of the command is a player, false if it's not.
     */
    protected boolean isPlayer(CommandSender sender) {
        return sender instanceof Player;
    }
}
