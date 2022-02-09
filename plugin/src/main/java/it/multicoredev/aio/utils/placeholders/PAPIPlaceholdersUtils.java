package it.multicoredev.aio.utils.placeholders;

import com.google.common.base.Preconditions;
import it.multicoredev.aio.api.utils.IPlaceholdersUtils;
import me.clip.placeholderapi.PlaceholderAPI;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
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

    public String replacePlaceholders(@NotNull String msg, @NotNull String[] targets, @NotNull Object[] replacements, boolean hasPAPIPermission) {
        Preconditions.checkNotNull(msg);
        Preconditions.checkNotNull(targets);
        Preconditions.checkNotNull(replacements);
        Preconditions.checkArgument(targets.length == replacements.length);

        for (int i = 0; i < targets.length; i++) msg = msg.replace(targets[i], replacements[i].toString());
        if (hasPAPIPermission) {
            msg = PlaceholderAPI.setPlaceholders(null, msg);
            msg = msg.replaceAll("%[^%]+%", "");
        }

        return msg;
    }

    public String replacePlaceholders(@NotNull String msg, @NotNull String[] targets, @NotNull Object[] replacements) {
        return replacePlaceholders(msg, targets, replacements, true);
    }

    public String replacePlaceholders(@NotNull String msg, @NotNull String target, @NotNull Object replacement, boolean hasPAPIPermission) {
        Preconditions.checkNotNull(target);
        Preconditions.checkNotNull(replacement);

        return replacePlaceholders(msg, new String[]{target}, new Object[]{replacement}, hasPAPIPermission);
    }

    public String replacePlaceholders(@NotNull String msg, @NotNull String target, @NotNull Object replacement) {
        Preconditions.checkNotNull(target);
        Preconditions.checkNotNull(replacement);

        return replacePlaceholders(msg, new String[]{target}, new Object[]{replacement}, true);
    }

    public String replacePlaceholders(@NotNull String msg, boolean hasPAPIPermission) {
        return replacePlaceholders(msg, new String[]{}, new Object[]{}, hasPAPIPermission);
    }

    public String replacePlaceholders(@NotNull String msg) {
        return replacePlaceholders(msg, new String[]{}, new Object[]{}, true);
    }

    public List<String> replacePlaceholders(@NotNull List<String> msgs, @NotNull String[] targets, @NotNull Object[] replacements, boolean hasPAPIPermission) {
        Preconditions.checkNotNull(msgs);
        Preconditions.checkNotNull(targets);
        Preconditions.checkNotNull(replacements);
        Preconditions.checkArgument(targets.length == replacements.length);

        return msgs.stream().map(msg -> replacePlaceholders(msg, targets, replacements, hasPAPIPermission)).collect(Collectors.toList());
    }

    public List<String> replacePlaceholders(@NotNull List<String> msgs, @NotNull String[] targets, @NotNull Object[] replacements) {
        Preconditions.checkNotNull(msgs);
        Preconditions.checkNotNull(targets);
        Preconditions.checkNotNull(replacements);
        Preconditions.checkArgument(targets.length == replacements.length);

        return msgs.stream().map(msg -> replacePlaceholders(msg, targets, replacements, true)).collect(Collectors.toList());
    }

    public List<String> replacePlaceholders(@NotNull List<String> msgs, @NotNull String target, @NotNull Object replacement, boolean hasPAPIPermission) {
        Preconditions.checkNotNull(msgs);
        Preconditions.checkNotNull(target);
        Preconditions.checkNotNull(replacement);

        return msgs.stream().map(msg -> replacePlaceholders(msg, target, replacement, hasPAPIPermission)).collect(Collectors.toList());
    }

    public List<String> replacePlaceholders(@NotNull List<String> msgs, @NotNull String target, @NotNull Object replacement) {
        Preconditions.checkNotNull(msgs);
        Preconditions.checkNotNull(target);
        Preconditions.checkNotNull(replacement);

        return msgs.stream().map(msg -> replacePlaceholders(msg, target, replacement, true)).collect(Collectors.toList());
    }

    public List<String> replacePlaceholders(@NotNull List<String> msgs, boolean hasPAPIPermission) {
        Preconditions.checkNotNull(msgs);

        return msgs.stream().map(msg -> replacePlaceholders(msg, new String[]{}, new Object[]{}, hasPAPIPermission)).collect(Collectors.toList());
    }

    public List<String> replacePlaceholders(@NotNull List<String> msgs) {
        Preconditions.checkNotNull(msgs);

        return msgs.stream().map(msg -> replacePlaceholders(msg, new String[]{}, new Object[]{}, true)).collect(Collectors.toList());
    }

    public String[] replacePlaceholders(@NotNull String[] msgs, @NotNull String[] targets, @NotNull Object[] replacements, boolean hasPAPIPermission) {
        Preconditions.checkNotNull(msgs);
        Preconditions.checkNotNull(targets);
        Preconditions.checkNotNull(replacements);
        Preconditions.checkArgument(targets.length == replacements.length);

        return Arrays.stream(msgs).map(msg -> replacePlaceholders(msg, targets, replacements, hasPAPIPermission)).toArray(String[]::new);
    }

    public String[] replacePlaceholders(@NotNull String[] msgs, @NotNull String[] targets, @NotNull Object[] replacements) {
        Preconditions.checkNotNull(msgs);
        Preconditions.checkNotNull(targets);
        Preconditions.checkNotNull(replacements);
        Preconditions.checkArgument(targets.length == replacements.length);

        return Arrays.stream(msgs).map(msg -> replacePlaceholders(msg, targets, replacements, true)).toArray(String[]::new);
    }

    public String[] replacePlaceholders(@NotNull String[] msgs, @NotNull String target, @NotNull Object replacement, boolean hasPAPIPermission) {
        Preconditions.checkNotNull(msgs);
        Preconditions.checkNotNull(target);
        Preconditions.checkNotNull(replacement);

        return Arrays.stream(msgs).map(msg -> replacePlaceholders(msg, target, replacement, hasPAPIPermission)).toArray(String[]::new);
    }

    public String[] replacePlaceholders(@NotNull String[] msgs, @NotNull String target, @NotNull Object replacement) {
        Preconditions.checkNotNull(msgs);
        Preconditions.checkNotNull(target);
        Preconditions.checkNotNull(replacement);

        return Arrays.stream(msgs).map(msg -> replacePlaceholders(msg, target, replacement, true)).toArray(String[]::new);
    }

    public String[] replacePlaceholders(@NotNull String[] msgs, boolean hasPAPIPermission) {
        Preconditions.checkNotNull(msgs);

        return Arrays.stream(msgs).map(msg -> replacePlaceholders(msg, new String[]{}, new Object[]{}, hasPAPIPermission)).toArray(String[]::new);
    }

    public String[] replacePlaceholders(@NotNull String[] msgs) {
        Preconditions.checkNotNull(msgs);

        return Arrays.stream(msgs).map(msg -> replacePlaceholders(msg, new String[]{}, new Object[]{}, true)).toArray(String[]::new);
    }
}
