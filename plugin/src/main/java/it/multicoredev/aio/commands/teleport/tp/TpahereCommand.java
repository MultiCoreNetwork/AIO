package it.multicoredev.aio.commands.teleport.tp;

import it.multicoredev.aio.AIO;
import it.multicoredev.aio.api.tp.ITeleportManager;
import it.multicoredev.aio.api.tp.TeleportRequestType;
import it.multicoredev.aio.api.utils.IPlaceholdersUtils;
import it.multicoredev.aio.commands.PluginCommand;
import it.multicoredev.mbcore.spigot.Chat;
import it.multicoredev.mbcore.spigot.util.TabCompleterUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright © 2022 by Daniele Patella. All rights reserved.
 * This file is part of AIO.
 * <p>
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING,
 * BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY,
 * OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */
public class TpahereCommand extends PluginCommand {
    private static final String CMD = "tpahere";

    public TpahereCommand(AIO aio) {
        super(aio, CMD);
    }

    @Override
    public boolean run(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (!isPlayer(sender)) {
            Chat.send(localization.notPlayer, sender);
            return true;
        }

        Player requester = (Player) sender;

        if (args.length < 1) {
            incorrectUsage(sender);
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);

        if (target == null) {
            Chat.send(localization.playerNotFound, sender);
            return true;
        }

        if (requester.getUniqueId() == target.getUniqueId()) {
            Chat.send(localization.noTeleportRequestYourself, sender);
            return true;
        }

        ITeleportManager teleportManager = aio.getTeleportManager();

        if (teleportManager.hasRequesterTeleportRequest(requester)) {
            Chat.send(localization.anotherRequestInPending, requester);
            return true;
        }

        teleportManager.requestTeleport(TeleportRequestType.TPAHERE, requester, target);

        IPlaceholdersUtils placeholdersUtils = aio.getPlaceholdersUtils();
        Chat.send(placeholdersUtils.replacePlaceholders(localization.tpahereRequestSent, "{TARGET}", target.getName()), sender);
        Chat.send(placeholdersUtils.replacePlaceholders(localization.tpahereRequestTarget, "{REQUESTER}", requester.getName()), target);
        return true;
    }

    @Override
    public List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) {
        if (hasCommandPerm(sender) && args.length == 1) {
            List<String> nicknames = TabCompleterUtil.getPlayers(args[0], sender.hasPermission("pv.see"));

            if (isPlayer(sender)) {
                Player player = (Player) sender;
                nicknames.remove(player.getName());
            }

            return nicknames;
        }

        return new ArrayList<>();
    }
}