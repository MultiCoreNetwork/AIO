package it.multicoredev.aio.commands.player.kits;

import it.multicoredev.aio.AIO;
import it.multicoredev.aio.commands.PluginCommand;
import it.multicoredev.mbcore.spigot.Chat;
import it.multicoredev.mbcore.spigot.util.chat.RawMessage;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
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
public class KitsCommand extends PluginCommand {
    private static final String CMD = "kits";

    public KitsCommand(AIO aio) {
        super(aio, CMD);
    }

    @Override
    public boolean run(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        List<String> kits = aio.getKitStorage().getKitNames(sender);
        if (kits.isEmpty()) {
            Chat.send(localization.noKits, sender);
            return true;
        }

        int page = 0;

        if (args.length > 0) {
            try {
                page = Math.max(0, Integer.parseInt(args[0]) - 1);
            } catch (NumberFormatException ignored) {
            }
        }

        float maxPages = (float) kits.size() / (float) 18;

        if (page > maxPages) {
            Chat.send(pu.replacePlaceholders(localization.pageNotFound, "{PAGES}", maxPages), sender);
            return true;
        }

        Chat.send(pu.replacePlaceholders(localization.availableKits), sender);

        if (kits.size() < 19) {
            for (String kit : kits) {
                Chat.send(pu.replacePlaceholders(localization.kitListFormat, "{KIT}", kit), sender);
            }
        } else {
            int minIndex = page * 18;
            int remaining = kits.size() - minIndex;
            int maxIndex = remaining <= 19 ? minIndex + remaining : minIndex + 18;

            for (int i = minIndex; i < maxIndex; i++) {
                Chat.send(pu.replacePlaceholders(localization.kitListFormat, "{KIT}", kits.get(i)), sender);
            }

            if (isPlayer(sender)) {
                String nav = pu.replacePlaceholders(
                        localization.pageNavigation, new String[]{
                                "{PREV_PAGE}",
                                "{CURRENT_PAGE}",
                                "{MAX_PAGES}",
                                "{NEXT_PAGE}"
                        },
                        new Object[]{
                                page == 0 ? "" : "{PREV_PAGE}",
                                page + 1,
                                (int) maxPages + 1,
                                page == (int) maxPages ? "" : "{NEXT_PAGE}"
                        });

                List<String> components = new ArrayList<>();
                int indexPrevPage = nav.indexOf("{PREV_PAGE}");
                int indexNextPage = nav.indexOf("{NEXT_PAGE}");

                if (indexPrevPage == -1 && indexNextPage == -1) {
                    components.add(nav);
                } else {
                    if (indexPrevPage != -1) {
                        components.add(nav.substring(0, indexPrevPage));
                        components.add("{PREV_PAGE}");

                        if (indexNextPage != -1) {
                            components.add(nav.substring(indexPrevPage + 11, indexNextPage));
                            components.add("{NEXT_PAGE}");
                            components.add(nav.substring(indexNextPage + 11));
                        } else {
                            components.add(nav.substring(indexPrevPage + 11));
                        }
                    } else {
                        components.add(nav.substring(0, indexNextPage));
                        components.add("{NEXT_PAGE}");
                        components.add(nav.substring(indexNextPage + 11));
                    }
                }

                ComponentBuilder componentBuilder = new ComponentBuilder();

                for (String str : components) {
                    componentBuilder.append(TextComponent.fromLegacyText(Chat.getTranslated(str)));
                }

                BaseComponent[] navComponents = componentBuilder.create();
                RawMessage msg = new RawMessage();

                for (BaseComponent bc : navComponents) {
                    if (bc.toPlainText().equals("{PREV_PAGE}")) {
                        TextComponent tc = (TextComponent) bc;
                        tc.setText("<-- ");
                        tc.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/kits " + (page - 1)));
                        msg.append(tc);
                    } else if (bc.toPlainText().equals("{NEXT_PAGE}")) {
                        TextComponent tc = (TextComponent) bc;
                        tc.setText(" -->");
                        tc.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/kits " + (page == 0 ? page + 2 : page + 1)));
                        msg.append(tc);
                    } else {
                        msg.append((TextComponent) bc);
                    }
                }

                Chat.sendRaw(msg, (Player) sender);

            } else {
                Chat.send(pu.replacePlaceholders(
                        localization.pageNavigation, new String[]{
                                "{PREV_PAGE}",
                                "{CURRENT_PAGE}",
                                "{MAX_PAGES}",
                                "{NEXT_PAGE}"
                        },
                        new Object[]{
                                page == 0 ? "" : "<-- ",
                                page + 1,
                                (int) maxPages + 1,
                                page == (int) maxPages ? "" : " -->"
                        }), sender);
            }
        }

        return true;
    }
}
