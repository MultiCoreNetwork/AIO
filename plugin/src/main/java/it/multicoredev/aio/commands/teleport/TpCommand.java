package it.multicoredev.aio.commands.teleport;

import it.multicoredev.aio.AIO;
import it.multicoredev.aio.commands.PluginCommand;
import it.multicoredev.aio.storage.config.modules.SpawnModule;
import it.multicoredev.aio.utils.TeleportMessageCallback;
import it.multicoredev.mbcore.spigot.Chat;
import it.multicoredev.mbcore.spigot.util.TabCompleterUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
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
public class TpCommand extends PluginCommand {
    private static final String CMD = "tp";

    public TpCommand(AIO aio) {
        super(aio, CMD);
    }

    @Override
    public boolean run(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            incorrectUsage(sender);
            return false;
        }

        if (isPlayer(sender)) {
            if (args.length == 1) { //tp <player>
                Player src = (Player) sender;
                Player target = Bukkit.getPlayer(args[0]);

                if (target == null) {
                    Chat.send(pu.replacePlaceholders(localization.playerNotFound), sender);
                    return false;
                }

                aio.getTeleportManager().teleport(
                        src,
                        target,
                        config.teleportSection.getTpDelay(),
                        localization.pendingTpSelf,
                        localization.tpSelf
                );
            } else if (args.length == 2) { //tp <player> <player>
                Player src = Bukkit.getPlayer(args[0]);
                Player target = Bukkit.getPlayer(args[1]);

                if (src == null || target == null) {
                    Chat.send(pu.replacePlaceholders(localization.playerNotFound), sender); //TODO Use custom message to say if the null is the first or the second player
                    return false;
                }

                boolean senderIsTarget = sender.equals(target);

                aio.getTeleportManager().teleport(
                        src,
                        target,
                        !senderIsTarget && sender.hasPermission("aio.teleport.instant") ? 0 : config.teleportSection.getTpDelay(),
                        localization.pendingTpSelf,
                        localization.tpSelf,
                        !senderIsTarget ? new TeleportMessageCallback(aio, sender, localization.tp) : null
                );
            } else if (args.length == 3) { //tp <x> <y> <z>
                Player src = (Player) sender;
                World world = src.getWorld();
                double x;
                double y;
                double z;

                try {
                    x = Double.parseDouble(args[0]);
                    y = Double.parseDouble(args[1]);
                    z = Double.parseDouble(args[2]);
                } catch (NumberFormatException ignored) {
                    Chat.send(pu.replacePlaceholders(localization.invalidTpDestination), sender);
                    return false;
                }

                Location target = new Location(world, x, y, z);

                aio.getTeleportManager().teleport(
                        src,
                        target,
                        config.teleportSection.getTpDelay(),
                        localization.pendingTpSelf,
                        localization.tpSelf
                );
            } else if (args.length == 4) { //tp <player> <x> <y> <z> | tp <world> <x> <y> <z>
                Player src = Bukkit.getPlayer(args[0]);

                if (src != null) {
                    World world = src.getWorld();
                    double x;
                    double y;
                    double z;

                    try {
                        x = Double.parseDouble(args[0]);
                        y = Double.parseDouble(args[1]);
                        z = Double.parseDouble(args[2]);
                    } catch (NumberFormatException ignored) {
                        Chat.send(pu.replacePlaceholders(localization.invalidTpDestination), sender);
                        return false;
                    }

                    Location target = new Location(world, x, y, z);

                    boolean senderIsTarget = sender.equals(target);

                    aio.getTeleportManager().teleport(
                            src,
                            target,
                            !senderIsTarget && sender.hasPermission("aio.teleport.instant") ? 0 : config.teleportSection.getTpDelay(),
                            localization.pendingTpSelf,
                            localization.tpSelf,
                            !senderIsTarget ? new TeleportMessageCallback(aio, sender, localization.tp) : null
                    );
                } else {
                    src = (Player) sender;

                    World world = Bukkit.getWorld(args[0]);

                    if (world == null) {
                        Chat.send(pu.replacePlaceholders(localization.worldNotFound), sender);
                        return false;
                    }

                    double x;
                    double y;
                    double z;

                    try {
                        x = Double.parseDouble(args[0]);
                        y = Double.parseDouble(args[1]);
                        z = Double.parseDouble(args[2]);
                    } catch (NumberFormatException ignored) {
                        Chat.send(pu.replacePlaceholders(localization.invalidTpDestination), sender);
                        return false;
                    }

                    Location target = new Location(world, x, y, z);

                    aio.getTeleportManager().teleport(
                            src,
                            target,
                            config.teleportSection.getTpDelay(),
                            localization.pendingTpSelf,
                            localization.tpSelf
                    );
                }
            } else if (args.length == 5) { //tp <player> <world> <x> <y> <z> | tp <x> <y> <z> <pitch> <yaw>
                Player src = Bukkit.getPlayer(args[0]);

                if (src != null) {
                    World world = Bukkit.getWorld(args[1]);

                    if (world == null) {
                        Chat.send(pu.replacePlaceholders(localization.worldNotFound), sender);
                        return false;
                    }

                    double x;
                    double y;
                    double z;

                    try {
                        x = Double.parseDouble(args[2]);
                        y = Double.parseDouble(args[3]);
                        z = Double.parseDouble(args[4]);
                    } catch (NumberFormatException ignored) {
                        Chat.send(pu.replacePlaceholders(localization.invalidTpDestination), sender);
                        return false;
                    }

                    Location target = new Location(world, x, y, z);

                    boolean senderIsTarget = sender.equals(target);

                    aio.getTeleportManager().teleport(
                            src,
                            target,
                            !senderIsTarget && sender.hasPermission("aio.teleport.instant") ? 0 : config.teleportSection.getTpDelay(),
                            localization.pendingTpSelf,
                            localization.tpSelf,
                            !senderIsTarget ? new TeleportMessageCallback(aio, sender, localization.tp) : null
                    );
                } else {
                    src = (Player) sender;
                    World world = src.getWorld();

                    double x;
                    double y;
                    double z;
                    float pitch;
                    float yaw;

                    try {
                        x = Double.parseDouble(args[0]);
                        y = Double.parseDouble(args[1]);
                        z = Double.parseDouble(args[2]);
                        pitch = Float.parseFloat(args[3]);
                        yaw = Float.parseFloat(args[4]);

                        //TODO Check pitch and yaw order and min/max values
                    } catch (NumberFormatException ignored) {
                        Chat.send(pu.replacePlaceholders(localization.invalidTpDestination), sender);
                        return false;
                    }

                    Location target = new Location(world, x, y, z, yaw, pitch);

                    aio.getTeleportManager().teleport(
                            src,
                            target,
                            config.teleportSection.getTpDelay(),
                            localization.pendingTpSelf,
                            localization.tpSelf
                    );
                }
            } else if (args.length == 6) { //tp <player> <x> <y> <z> <pitch> <yaw> | tp <world> <x> <y> <z> <pitch> <yaw>
                Player src = Bukkit.getPlayer(args[0]);

                if (src != null) {
                    World world = Bukkit.getWorld(args[1]);

                    if (world == null) {
                        Chat.send(pu.replacePlaceholders(localization.worldNotFound), sender);
                        return false;
                    }

                    double x;
                    double y;
                    double z;
                    float pitch;
                    float yaw;

                    try {
                        x = Double.parseDouble(args[0]);
                        y = Double.parseDouble(args[1]);
                        z = Double.parseDouble(args[2]);
                        pitch = Float.parseFloat(args[3]);
                        yaw = Float.parseFloat(args[4]);

                        //TODO Check pitch and yaw order and min/max values
                    } catch (NumberFormatException ignored) {
                        Chat.send(pu.replacePlaceholders(localization.invalidTpDestination), sender);
                        return false;
                    }

                    Location target = new Location(world, x, y, z, yaw, pitch);

                    boolean senderIsTarget = sender.equals(target);

                    aio.getTeleportManager().teleport(
                            src,
                            target,
                            !senderIsTarget && sender.hasPermission("aio.teleport.instant") ? 0 : config.teleportSection.getTpDelay(),
                            localization.pendingTpSelf,
                            localization.tpSelf,
                            !senderIsTarget ? new TeleportMessageCallback(aio, sender, localization.tp) : null
                    );
                } else {
                    src = (Player) sender;
                    World world = Bukkit.getWorld(args[0]);

                    if (world == null) {
                        Chat.send(pu.replacePlaceholders(localization.worldNotFound), sender);
                        return false;
                    }

                    double x;
                    double y;
                    double z;
                    float pitch;
                    float yaw;

                    try {
                        x = Double.parseDouble(args[1]);
                        y = Double.parseDouble(args[2]);
                        z = Double.parseDouble(args[3]);
                        pitch = Float.parseFloat(args[4]);
                        yaw = Float.parseFloat(args[5]);

                        //TODO Check pitch and yaw order and min/max values
                    } catch (NumberFormatException ignored) {
                        Chat.send(pu.replacePlaceholders(localization.invalidTpDestination), sender);
                        return false;
                    }

                    Location target = new Location(world, x, y, z, yaw, pitch);

                    aio.getTeleportManager().teleport(
                            src,
                            target,
                            config.teleportSection.getTpDelay(),
                            localization.pendingTpSelf,
                            localization.tpSelf
                    );
                }
            } else { //tp <player> <world> <x> <y> <z> <yaw> <pitch>
                Player src = Bukkit.getPlayer(args[0]);
                if (src == null) {
                    Chat.send(pu.replacePlaceholders(localization.playerNotFound), sender);
                    return true;
                }

                World world = Bukkit.getWorld(args[1]);
                if (world == null) {
                    Chat.send(pu.replacePlaceholders(localization.worldNotFound), sender);
                    return true;
                }

                double x;
                double y;
                double z;
                float pitch;
                float yaw;

                try {
                    x = Double.parseDouble(args[1]);
                    y = Double.parseDouble(args[2]);
                    z = Double.parseDouble(args[3]);
                    pitch = Float.parseFloat(args[4]);
                    yaw = Float.parseFloat(args[5]);

                    //TODO Check pitch and yaw order and min/max values
                } catch (NumberFormatException ignored) {
                    Chat.send(pu.replacePlaceholders(localization.invalidTpDestination), sender);
                    return false;
                }

                Location target = new Location(world, x, y, z, yaw, pitch);

                boolean senderIsTarget = sender.equals(target);

                aio.getTeleportManager().teleport(
                        src,
                        target,
                        !senderIsTarget && sender.hasPermission("aio.teleport.instant") ? 0 : config.teleportSection.getTpDelay(),
                        localization.pendingTpSelf,
                        localization.tpSelf,
                        !senderIsTarget ? new TeleportMessageCallback(aio, sender, localization.tp) : null
                );
            }
        } else {
            if (args.length == 1) {
                Chat.send(pu.replacePlaceholders(localization.notPlayer), sender);
                return false;
            } else if (args.length == 2) { //tp <player> <player>
                Player src = Bukkit.getPlayer(args[0]);
                Player target = Bukkit.getPlayer(args[1]);

                if (src == null || target == null) {
                    Chat.send(pu.replacePlaceholders(localization.playerNotFound), sender); //TODO Use custom message to say if the null is the first or the second player
                    return false;
                }

                boolean senderIsTarget = sender.equals(target);

                aio.getTeleportManager().teleport(
                        src,
                        target,
                        !senderIsTarget && sender.hasPermission("aio.teleport.instant") ? 0 : config.teleportSection.getTpDelay(),
                        localization.pendingTpSelf,
                        localization.tpSelf,
                        !senderIsTarget ? new TeleportMessageCallback(aio, sender, localization.tp) : null
                );
            } else if (args.length == 3) {
                Chat.send(pu.replacePlaceholders(localization.notPlayer), sender);
                return false;
            } else if (args.length == 4) { //tp <player> <x> <y> <z>
                Player src = Bukkit.getPlayer(args[0]);

                if (src == null) {
                    Chat.send(pu.replacePlaceholders(localization.playerNotFound), sender);
                    return false;
                }

                World world = src.getWorld();
                double x;
                double y;
                double z;

                try {
                    x = Double.parseDouble(args[0]);
                    y = Double.parseDouble(args[1]);
                    z = Double.parseDouble(args[2]);
                } catch (NumberFormatException ignored) {
                    Chat.send(pu.replacePlaceholders(localization.invalidTpDestination), sender);
                    return false;
                }

                Location target = new Location(world, x, y, z);

                boolean senderIsTarget = sender.equals(target);

                aio.getTeleportManager().teleport(
                        src,
                        target,
                        !senderIsTarget && sender.hasPermission("aio.teleport.instant") ? 0 : config.teleportSection.getTpDelay(),
                        localization.pendingTpSelf,
                        localization.tpSelf,
                        !senderIsTarget ? new TeleportMessageCallback(aio, sender, localization.tp) : null
                );
            } else if (args.length == 5) { //tp <player> <world> <x> <y> <z>
                Player src = Bukkit.getPlayer(args[0]);

                if (src == null) {
                    Chat.send(pu.replacePlaceholders(localization.playerNotFound), sender);
                    return false;
                }

                World world = Bukkit.getWorld(args[1]);

                if (world == null) {
                    Chat.send(pu.replacePlaceholders(localization.worldNotFound), sender);
                    return false;
                }

                double x;
                double y;
                double z;

                try {
                    x = Double.parseDouble(args[2]);
                    y = Double.parseDouble(args[3]);
                    z = Double.parseDouble(args[4]);
                } catch (NumberFormatException ignored) {
                    Chat.send(pu.replacePlaceholders(localization.invalidTpDestination), sender);
                    return false;
                }

                Location target = new Location(world, x, y, z);

                boolean senderIsTarget = sender.equals(target);

                aio.getTeleportManager().teleport(
                        src,
                        target,
                        !senderIsTarget && sender.hasPermission("aio.teleport.instant") ? 0 : config.teleportSection.getTpDelay(),
                        localization.pendingTpSelf,
                        localization.tpSelf,
                        !senderIsTarget ? new TeleportMessageCallback(aio, sender, localization.tp) : null
                );
            } else if (args.length == 6) { //tp <player> <x> <y> <z> <pitch> <yaw>
                Player src = Bukkit.getPlayer(args[0]);

                if (src == null) {
                    Chat.send(pu.replacePlaceholders(localization.playerNotFound), sender);
                    return false;
                }

                World world = Bukkit.getWorld(args[1]);

                if (world == null) {
                    Chat.send(pu.replacePlaceholders(localization.worldNotFound), sender);
                    return false;
                }

                double x;
                double y;
                double z;
                float pitch;
                float yaw;

                try {
                    x = Double.parseDouble(args[0]);
                    y = Double.parseDouble(args[1]);
                    z = Double.parseDouble(args[2]);
                    pitch = Float.parseFloat(args[3]);
                    yaw = Float.parseFloat(args[4]);

                    //TODO Check pitch and yaw order and min/max values
                } catch (NumberFormatException ignored) {
                    Chat.send(pu.replacePlaceholders(localization.invalidTpDestination), sender);
                    return false;
                }

                Location target = new Location(world, x, y, z, yaw, pitch);

                boolean senderIsTarget = sender.equals(target);

                aio.getTeleportManager().teleport(
                        src,
                        target,
                        !senderIsTarget && sender.hasPermission("aio.teleport.instant") ? 0 : config.teleportSection.getTpDelay(),
                        localization.pendingTpSelf,
                        localization.tpSelf,
                        !senderIsTarget ? new TeleportMessageCallback(aio, sender, localization.tp) : null
                );
            } else { //tp <player> <world> <x> <y> <z> <yaw> <pitch>
                Player src = Bukkit.getPlayer(args[0]);
                if (src == null) {
                    Chat.send(pu.replacePlaceholders(localization.playerNotFound), sender);
                    return true;
                }

                World world = Bukkit.getWorld(args[1]);
                if (world == null) {
                    Chat.send(pu.replacePlaceholders(localization.worldNotFound), sender);
                    return true;
                }

                double x;
                double y;
                double z;
                float pitch;
                float yaw;

                try {
                    x = Double.parseDouble(args[1]);
                    y = Double.parseDouble(args[2]);
                    z = Double.parseDouble(args[3]);
                    pitch = Float.parseFloat(args[4]);
                    yaw = Float.parseFloat(args[5]);

                    //TODO Check pitch and yaw order and min/max values
                } catch (NumberFormatException ignored) {
                    Chat.send(pu.replacePlaceholders(localization.invalidTpDestination), sender);
                    return false;
                }

                Location target = new Location(world, x, y, z, yaw, pitch);

                boolean senderIsTarget = sender.equals(target);

                aio.getTeleportManager().teleport(
                        src,
                        target,
                        !senderIsTarget && sender.hasPermission("aio.teleport.instant") ? 0 : config.teleportSection.getTpDelay(),
                        localization.pendingTpSelf,
                        localization.tpSelf,
                        !senderIsTarget ? new TeleportMessageCallback(aio, sender, localization.tp) : null
                );
            }
        }

        return true;
    }

    @Override
    public List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) {
        //TODO
        return new ArrayList<>();
    }
}
