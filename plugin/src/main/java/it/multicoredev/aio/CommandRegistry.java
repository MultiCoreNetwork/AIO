package it.multicoredev.aio;

import it.multicoredev.aio.api.BasePluginCommand;
import it.multicoredev.aio.api.ICommandRegistry;
import it.multicoredev.mbcore.spigot.Chat;
import org.bukkit.command.Command;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

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
public class CommandRegistry implements ICommandRegistry {
    private final SimpleCommandMap commandMap;
    private final Map<Plugin, List<BasePluginCommand>> commands = new HashMap<>();

    CommandRegistry(SimpleCommandMap commandMap) {
        this.commandMap = commandMap;
    }

    @Override
    public void registerCommand(@NotNull BasePluginCommand command, Plugin plugin) {
        commandMap.register(plugin.getName().toLowerCase(Locale.ROOT), command);

        if (commands.containsKey(plugin)) commands.get(plugin).add(command);
        else commands.put(plugin, new ArrayList<>(Collections.singleton(command)));

        if (debug)
            Chat.info(String.format("&aC&r Registered command &a%s&r with aliases &a%s&r.", command.getName(), Arrays.toString(command.getAliases().toArray())));
    }

    @Override
    @SuppressWarnings("unchecked")
    public void unregisterCommand(@NotNull BasePluginCommand command) {
        boolean commandRegistered = false;
        for (Map.Entry<Plugin, List<BasePluginCommand>> entry : commands.entrySet()) {
            if (entry.getValue().contains(command)) {
                entry.getValue().remove(command);
                if (entry.getValue().isEmpty()) commands.remove(entry.getKey());
                commandRegistered = true;
                break;
            }
        }

        if (!commandRegistered) return;

        try {
            Field knownCommandsField = SimpleCommandMap.class.getDeclaredField("knownCommands");
            boolean isFieldAccessible = knownCommandsField.isAccessible();

            if (!isFieldAccessible) knownCommandsField.setAccessible(true);

            Map<String, Command> knownCommands = (Map<String, Command>) knownCommandsField.get(commandMap);
            knownCommands.remove(command.getName());
            command.getAliases().forEach(knownCommands::remove);

            if (!isFieldAccessible) knownCommandsField.setAccessible(false);

            if (debug)
                Chat.info(String.format("&6C&r Unregistered command &6%s&r with aliases &6%s&r.", command.getName(), Arrays.toString(command.getAliases().toArray())));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void unregisterCommands(@NotNull Plugin plugin) {
        if (!commands.containsKey(plugin)) return;

        while (commands.get(plugin) != null) unregisterCommand(commands.get(plugin).get(0));
    }

    @Override
    public void unregisterCommands() {
        while (!commands.isEmpty()) unregisterCommands(commands.keySet().iterator().next());
    }

    @Override
    public List<BasePluginCommand> getRegisteredCommands(@NotNull Plugin plugin) {
        if (commands.containsKey(plugin)) return commands.get(plugin);
        else return new ArrayList<>();
    }

    @Override
    public List<BasePluginCommand> getRegisteredCommands() {
        List<BasePluginCommand> cmds = new ArrayList<>();
        commands.values().forEach(cmds::addAll);
        return cmds;
    }

    @Override
    public List<String> getCommandNames(@NotNull Plugin plugin) {
        return getRegisteredCommands(plugin).stream().map(BasePluginCommand::getName).collect(Collectors.toList());
    }

    @Override
    public List<String> getCommandNames() {
        return getRegisteredCommands().stream().map(BasePluginCommand::getName).collect(Collectors.toList());
    }

    @Override
    public List<String> getCommandAliases(@NotNull String command) {
        List<String> aliases = new ArrayList<>();

        commands.values().forEach(cmds -> cmds.forEach(cmd -> {
            if (cmd.getName().equalsIgnoreCase(command)) {
                aliases.add(cmd.getName());
                aliases.addAll(cmd.getAliases());
            }
        }));

        return aliases;
    }

    public List<String> getAllCommandNames() {
        Collection<Command> commands = commandMap.getCommands();
        List<String> names = new ArrayList<>();
        commands.forEach(cmd -> {
            names.add(cmd.getName());
            names.addAll(cmd.getAliases());
        });

        return names;
    }
}
