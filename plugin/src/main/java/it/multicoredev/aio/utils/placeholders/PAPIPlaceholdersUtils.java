package it.multicoredev.aio.utils.placeholders;

import com.google.common.base.Preconditions;
import it.multicoredev.aio.api.utils.IPlaceholdersUtils;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

/**
 * Copyright © 2022 by Lorenzo Magni
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
public class PAPIPlaceholdersUtils implements IPlaceholdersUtils {

    @Override
    public String replacePlaceholders(String msg, @NotNull String[] targets, @NotNull Object[] replacements, OfflinePlayer player, boolean usePAPI) {
        Preconditions.checkNotNull(targets);
        Preconditions.checkNotNull(replacements);
        Preconditions.checkArgument(targets.length == replacements.length);

        if (msg == null) return null;

        for (int i = 0; i < targets.length; i++) msg = msg.replace(targets[i], replacements[i].toString());
        if (usePAPI) msg = replacePAPIPlaceholders(msg, player);
        return msg;
    }

    @Override
    public String replacePlaceholders(String message, @NotNull String[] targets, @NotNull Object[] replacements, boolean usePAPI) {
        return replacePlaceholders(message, targets, replacements, null, usePAPI);
    }

    @Override
    public String replacePlaceholders(String message, @NotNull String[] targets, @NotNull Object[] replacements, OfflinePlayer player) {
        return replacePlaceholders(message, targets, replacements, player, true);
    }

    @Override
    public String replacePlaceholders(String message, @NotNull String[] targets, @NotNull Object[] replacements) {
        return replacePlaceholders(message, targets, replacements, null, true);
    }

    @Override
    public String replacePlaceholders(String message, @NotNull String target, @NotNull Object replacement, OfflinePlayer player, boolean usePAPI) {
        return replacePlaceholders(message, new String[]{target}, new Object[]{replacement}, player, usePAPI);
    }

    @Override
    public String replacePlaceholders(String message, @NotNull String target, @NotNull Object replacement, boolean usePAPI) {
        return replacePlaceholders(message, new String[]{target}, new Object[]{replacement}, null, usePAPI);
    }

    @Override
    public String replacePlaceholders(String message, @NotNull String target, @NotNull Object replacement, OfflinePlayer player) {
        return replacePlaceholders(message, new String[]{target}, new Object[]{replacement}, player, true);
    }

    @Override
    public String replacePlaceholders(String message, @NotNull String target, @NotNull Object replacement) {
        return replacePlaceholders(message, new String[]{target}, new Object[]{replacement}, null, true);
    }

    @Override
    public String replacePlaceholders(String message, OfflinePlayer player, boolean usePAPI) {
        return replacePlaceholders(message, new String[]{}, new Object[]{}, player, usePAPI);
    }

    @Override
    public String replacePlaceholders(String message, boolean usePAPI) {
        return replacePlaceholders(message, new String[]{}, new Object[]{}, null, usePAPI);
    }

    @Override
    public String replacePlaceholders(String message, OfflinePlayer player) {
        return replacePlaceholders(message, new String[]{}, new Object[]{}, player, true);
    }

    @Override
    public String replacePlaceholders(String message) {
        return replacePlaceholders(message, new String[]{}, new Object[]{}, null, true);
    }

    @Override
    public List<String> replacePlaceholders(List<String> messages, @NotNull String[] targets, @NotNull Object[] replacements, OfflinePlayer player, boolean usePAPI) {
        if (messages == null) return null;

        return messages.stream()
                .map(message -> replacePlaceholders(message, targets, replacements, player, usePAPI))
                .collect(Collectors.toList());
    }

    @Override
    public List<String> replacePlaceholders(List<String> messages, @NotNull String[] targets, @NotNull Object[] replacements, boolean usePAPI) {
        return replacePlaceholders(messages, targets, replacements, null, usePAPI);
    }

    @Override
    public List<String> replacePlaceholders(List<String> messages, @NotNull String[] targets, @NotNull Object[] replacements, OfflinePlayer player) {
        return replacePlaceholders(messages, targets, replacements, player, true);
    }

    @Override
    public List<String> replacePlaceholders(List<String> messages, @NotNull String[] targets, @NotNull Object[] replacements) {
        return replacePlaceholders(messages, targets, replacements, null, true);
    }

    @Override
    public List<String> replacePlaceholders(List<String> messages, @NotNull String target, @NotNull Object replacement, OfflinePlayer player, boolean usePAPI) {
        return replacePlaceholders(messages, new String[]{target}, new Object[]{replacement}, player, usePAPI);
    }

    @Override
    public List<String> replacePlaceholders(List<String> messages, @NotNull String target, @NotNull Object replacement, boolean usePAPI) {
        return replacePlaceholders(messages, new String[]{target}, new Object[]{replacement}, null, usePAPI);
    }

    @Override
    public List<String> replacePlaceholders(List<String> messages, @NotNull String target, @NotNull Object replacement, OfflinePlayer player) {
        return replacePlaceholders(messages, new String[]{target}, new Object[]{replacement}, player, true);
    }

    @Override
    public List<String> replacePlaceholders(List<String> messages, @NotNull String target, @NotNull Object replacement) {
        return replacePlaceholders(messages, new String[]{target}, new Object[]{replacement}, null, true);
    }

    @Override
    public List<String> replacePlaceholders(List<String> messages, OfflinePlayer player, boolean usePAPI) {
        return replacePlaceholders(messages, new String[]{}, new Object[]{}, player, usePAPI);
    }

    @Override
    public List<String> replacePlaceholders(List<String> messages, boolean usePAPI) {
        return replacePlaceholders(messages, new String[]{}, new Object[]{}, null, usePAPI);
    }

    @Override
    public List<String> replacePlaceholders(List<String> messages, OfflinePlayer player) {
        return replacePlaceholders(messages, new String[]{}, new Object[]{}, player, true);
    }

    @Override
    public List<String> replacePlaceholders(List<String> messages) {
        return replacePlaceholders(messages, new String[]{}, new Object[]{}, null, true);
    }

    @Override
    public String[] replacePlaceholders(String[] messages, @NotNull String[] targets, @NotNull Object[] replacements, OfflinePlayer player, boolean usePAPI) {
        if (messages == null) return null;

        return Arrays.stream(messages)
                .map(message -> replacePlaceholders(message, targets, replacements, player, usePAPI))
                .toArray(String[]::new);
    }

    @Override
    public String[] replacePlaceholders(String[] messages, @NotNull String[] targets, @NotNull Object[] replacements, boolean usePAPI) {
        return replacePlaceholders(messages, targets, replacements, null, usePAPI);
    }

    @Override
    public String[] replacePlaceholders(String[] messages, @NotNull String[] targets, @NotNull Object[] replacements, OfflinePlayer player) {
        return replacePlaceholders(messages, targets, replacements, player, true);
    }

    @Override
    public String[] replacePlaceholders(String[] messages, @NotNull String[] targets, @NotNull Object[] replacements) {
        return replacePlaceholders(messages, targets, replacements, null, true);
    }

    @Override
    public String[] replacePlaceholders(String[] messages, @NotNull String target, @NotNull Object replacement, OfflinePlayer player, boolean usePAPI) {
        return replacePlaceholders(messages, new String[]{target}, new Object[]{replacement}, player, usePAPI);
    }

    @Override
    public String[] replacePlaceholders(String[] messages, @NotNull String target, @NotNull Object replacement, boolean usePAPI) {
        return replacePlaceholders(messages, new String[]{target}, new Object[]{replacement}, null, usePAPI);
    }

    @Override
    public String[] replacePlaceholders(String[] messages, @NotNull String target, @NotNull Object replacement, OfflinePlayer player) {
        return replacePlaceholders(messages, new String[]{target}, new Object[]{replacement}, player, true);
    }

    @Override
    public String[] replacePlaceholders(String[] messages, @NotNull String target, @NotNull Object replacement) {
        return replacePlaceholders(messages, new String[]{target}, new Object[]{replacement}, null, true);
    }

    @Override
    public String[] replacePlaceholders(String[] messages, OfflinePlayer player, boolean usePAPI) {
        return replacePlaceholders(messages, new String[]{}, new Object[]{}, player, usePAPI);
    }

    @Override
    public String[] replacePlaceholders(String[] messages, boolean usePAPI) {
        return replacePlaceholders(messages, new String[]{}, new Object[]{}, null, usePAPI);
    }

    @Override
    public String[] replacePlaceholders(String[] messages, OfflinePlayer player) {
        return replacePlaceholders(messages, new String[]{}, new Object[]{}, player, true);
    }

    @Override
    public String[] replacePlaceholders(String[] messages) {
        return replacePlaceholders(messages, new String[]{}, new Object[]{}, null, true);
    }

    private String replacePAPIPlaceholders(@NotNull String msg, OfflinePlayer player) {
        msg = PlaceholderAPI.setPlaceholders(player, msg);
        msg = PlaceholderAPI.setBracketPlaceholders(player, msg);

        Matcher pm = PlaceholderAPI.getPlaceholderPattern().matcher(msg);
        msg = pm.replaceAll("");
        Matcher bpm = PlaceholderAPI.getBracketPlaceholderPattern().matcher(msg);
        msg = bpm.replaceAll("");

        return msg;
    }
}
