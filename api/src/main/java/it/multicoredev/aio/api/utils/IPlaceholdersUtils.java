package it.multicoredev.aio.api.utils;

import org.jetbrains.annotations.NotNull;

import java.util.List;

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
public interface IPlaceholdersUtils {

    /**
     * Replace custom placeholders in a string and if loaded PlaceholderAPI placeholders.
     *
     * @param msg               the string to replace placeholders in.
     * @param targets           the targets to replace.
     * @param replacements      the replacements.
     * @param hasPAPIPermission if true, will use PlaceholderAPI.
     * @return the replaced string.
     */
    String replacePlaceholders(@NotNull String msg, @NotNull String[] targets, @NotNull Object[] replacements, boolean hasPAPIPermission);

    /**
     * Replace custom placeholders in a string and if loaded PlaceholderAPI placeholders.
     *
     * @param msg          the string to replace placeholders in.
     * @param targets      the targets to replace.
     * @param replacements the replacements.
     * @return the replaced string.
     */
    String replacePlaceholders(@NotNull String msg, @NotNull String[] targets, @NotNull Object[] replacements);

    /**
     * Replace custom placeholders in a string and if loaded PlaceholderAPI placeholders.
     *
     * @param msg               the string to replace placeholders in.
     * @param target            the target to replace.
     * @param replacement       the replacement.
     * @param hasPAPIPermission if true, will use PlaceholderAPI.
     * @return the replaced string.
     */
    String replacePlaceholders(@NotNull String msg, @NotNull String target, @NotNull Object replacement, boolean hasPAPIPermission);

    /**
     * Replace custom placeholders in a string and if loaded PlaceholderAPI placeholders.
     *
     * @param msg         the string to replace placeholders in.
     * @param target      the target to replace.
     * @param replacement the replacement.
     * @return the replaced string.
     */
    String replacePlaceholders(@NotNull String msg, @NotNull String target, @NotNull Object replacement);

    /**
     * Replace, if loaded, PlaceholderAPI placeholders.
     *
     * @param msg               the string to replace placeholders in.
     * @param hasPAPIPermission if true, will use PlaceholderAPI.
     * @return the replaced string.
     */
    String replacePlaceholders(@NotNull String msg, boolean hasPAPIPermission);

    /**
     * Replace, if loaded, PlaceholderAPI placeholders.
     *
     * @param msg the string to replace placeholders in.
     * @return the replaced string.
     */
    String replacePlaceholders(@NotNull String msg);

    /**
     * Replace custom placeholders in a list of strings and if loaded PlaceholderAPI placeholders.
     *
     * @param msgs              the list of strings to replace placeholders in.
     * @param targets           the targets to replace.
     * @param replacements      the replacements.
     * @param hasPAPIPermission if true, will use PlaceholderAPI.
     * @return the replaced list of strings.
     */
    List<String> replacePlaceholders(@NotNull List<String> msgs, @NotNull String[] targets, @NotNull Object[] replacements, boolean hasPAPIPermission);

    /**
     * Replace custom placeholders in a list of strings and if loaded PlaceholderAPI placeholders.
     *
     * @param msgs         the list of strings to replace placeholders in.
     * @param targets      the targets to replace.
     * @param replacements the replacements.
     * @return the replaced list of strings.
     */
    List<String> replacePlaceholders(@NotNull List<String> msgs, @NotNull String[] targets, @NotNull Object[] replacements);

    /**
     * Replace custom placeholders in a list of strings and if loaded PlaceholderAPI placeholders.
     *
     * @param msgs              the list of strings to replace placeholders in.
     * @param target            the target to replace.
     * @param replacement       the replacement.
     * @param hasPAPIPermission if true, will use PlaceholderAPI.
     * @return the replaced list of strings.
     */
    List<String> replacePlaceholders(@NotNull List<String> msgs, @NotNull String target, @NotNull Object replacement, boolean hasPAPIPermission);

    /**
     * Replace custom placeholders in a list of strings and if loaded PlaceholderAPI placeholders.
     *
     * @param msgs        the list of strings to replace placeholders in.
     * @param target      the target to replace.
     * @param replacement the replacement.
     * @return the replaced list of strings.
     */
    List<String> replacePlaceholders(@NotNull List<String> msgs, @NotNull String target, @NotNull Object replacement);

    /**
     * Replace, if loaded, PlaceholderAPI placeholders.
     *
     * @param msgs              the list of strings to replace placeholders in.
     * @param hasPAPIPermission if true, will use PlaceholderAPI.
     * @return the replaced list of strings.
     */
    List<String> replacePlaceholders(@NotNull List<String> msgs, boolean hasPAPIPermission);

    /**
     * Replace, if loaded, PlaceholderAPI placeholders.
     *
     * @param msgs the list of strings to replace placeholders in.
     * @return the replaced list of strings.
     */
    List<String> replacePlaceholders(@NotNull List<String> msgs);

    /**
     * Replace custom placeholders in a list of strings and if loaded PlaceholderAPI placeholders.
     *
     * @param msgs              the list of strings to replace placeholders in.
     * @param targets           the targets to replace.
     * @param replacements      the replacements.
     * @param hasPAPIPermission if true, will use PlaceholderAPI.
     * @return the replaced list of strings.
     */
    String[] replacePlaceholders(@NotNull String[] msgs, @NotNull String[] targets, @NotNull Object[] replacements, boolean hasPAPIPermission);

    /**
     * Replace custom placeholders in a list of strings and if loaded PlaceholderAPI placeholders.
     *
     * @param msgs         the list of strings to replace placeholders in.
     * @param targets      the targets to replace.
     * @param replacements the replacements.
     * @return the replaced list of strings.
     */
    String[] replacePlaceholders(@NotNull String[] msgs, @NotNull String[] targets, @NotNull Object[] replacements);

    /**
     * Replace custom placeholders in a list of strings and if loaded PlaceholderAPI placeholders.
     *
     * @param msgs              the list of strings to replace placeholders in.
     * @param target            the target to replace.
     * @param replacement       the replacement.
     * @param hasPAPIPermission if true, will use PlaceholderAPI.
     * @return the replaced list of strings.
     */
    String[] replacePlaceholders(@NotNull String[] msgs, @NotNull String target, @NotNull Object replacement, boolean hasPAPIPermission);

    /**
     * Replace custom placeholders in a list of strings and if loaded PlaceholderAPI placeholders.
     *
     * @param msgs        the list of strings to replace placeholders in.
     * @param target      the target to replace.
     * @param replacement the replacement.
     * @return the replaced list of strings.
     */
    String[] replacePlaceholders(@NotNull String[] msgs, @NotNull String target, @NotNull Object replacement);

    /**
     * Replace, if loaded, PlaceholderAPI placeholders.
     *
     * @param msgs              the list of strings to replace placeholders in.
     * @param hasPAPIPermission if true, will use PlaceholderAPI.
     * @return the replaced list of strings.
     */
    String[] replacePlaceholders(@NotNull String[] msgs, boolean hasPAPIPermission);

    /**
     * Replace, if loaded, PlaceholderAPI placeholders.
     *
     * @param msgs the list of strings to replace placeholders in.
     * @return the replaced list of strings.
     */
    String[] replacePlaceholders(@NotNull String[] msgs);
}
