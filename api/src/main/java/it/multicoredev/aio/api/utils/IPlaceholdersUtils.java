package it.multicoredev.aio.api.utils;

import org.bukkit.OfflinePlayer;
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
     * @param message      the string to replace placeholders in.
     * @param targets      the targets to replace.
     * @param replacements the replacements.
     * @param player       the player to replace placeholders for.
     * @param usePAPI      if true, will use PlaceholderAPI.
     * @return the replaced string.
     */
    String replacePlaceholders(String message, @NotNull String[] targets, @NotNull Object[] replacements, OfflinePlayer player, boolean usePAPI);

    /**
     * Replace custom placeholders in a string and if loaded PlaceholderAPI placeholders.
     *
     * @param message      the string to replace placeholders in.
     * @param targets      the targets to replace.
     * @param replacements the replacements.
     * @param usePAPI      if true, will use PlaceholderAPI.
     * @return the replaced string.
     */
    String replacePlaceholders(String message, @NotNull String[] targets, @NotNull Object[] replacements, boolean usePAPI);

    /**
     * Replace custom placeholders in a string and if loaded PlaceholderAPI placeholders.
     * By default, it will use PlaceholderAPI.
     *
     * @param message      the string to replace placeholders in.
     * @param targets      the targets to replace.
     * @param replacements the replacements.
     * @param player       the player to replace placeholders for.
     * @return the replaced string.
     */
    String replacePlaceholders(String message, @NotNull String[] targets, @NotNull Object[] replacements, OfflinePlayer player);

    /**
     * Replace custom placeholders in a string and if loaded PlaceholderAPI placeholders.
     * By default, it will use PlaceholderAPI.
     *
     * @param message      the string to replace placeholders in.
     * @param targets      the targets to replace.
     * @param replacements the replacements.
     * @return the replaced string.
     */
    String replacePlaceholders(String message, @NotNull String[] targets, @NotNull Object[] replacements);

    /**
     * Replace custom placeholders in a string and if loaded PlaceholderAPI placeholders.
     *
     * @param message     the string to replace placeholders in.
     * @param target      the target to replace.
     * @param replacement the replacement.
     * @param player      the player to replace placeholders for.
     * @param usePAPI     if true, will use PlaceholderAPI.
     * @return the replaced string.
     */
    String replacePlaceholders(String message, @NotNull String target, @NotNull Object replacement, OfflinePlayer player, boolean usePAPI);

    /**
     * Replace custom placeholders in a string and if loaded PlaceholderAPI placeholders.
     *
     * @param message     the string to replace placeholders in.
     * @param target      the target to replace.
     * @param replacement the replacement.
     * @param usePAPI     if true, will use PlaceholderAPI.
     * @return the replaced string.
     */
    String replacePlaceholders(String message, @NotNull String target, @NotNull Object replacement, boolean usePAPI);

    /**
     * Replace custom placeholders in a string and if loaded PlaceholderAPI placeholders.
     * By default, it will use PlaceholderAPI.
     *
     * @param message     the string to replace placeholders in.
     * @param target      the target to replace.
     * @param replacement the replacement.
     * @param player      the player to replace placeholders for.
     * @return the replaced string.
     */
    String replacePlaceholders(String message, @NotNull String target, @NotNull Object replacement, OfflinePlayer player);

    /**
     * Replace custom placeholders in a string and if loaded PlaceholderAPI placeholders.
     * By default, it will use PlaceholderAPI.
     *
     * @param message     the string to replace placeholders in.
     * @param target      the target to replace.
     * @param replacement the replacement.
     * @return the replaced string.
     */
    String replacePlaceholders(String message, @NotNull String target, @NotNull Object replacement);

    /**
     * Replace, if loaded, PlaceholderAPI placeholders.
     *
     * @param message the string to replace placeholders in.
     * @param player  the player to replace placeholders for.
     * @param usePAPI if true, will use PlaceholderAPI.
     * @return the replaced string.
     */
    String replacePlaceholders(String message, OfflinePlayer player, boolean usePAPI);

    /**
     * Replace, if loaded, PlaceholderAPI placeholders.
     *
     * @param message the string to replace placeholders in.
     * @param usePAPI if true, will use PlaceholderAPI.
     * @return the replaced string.
     */
    String replacePlaceholders(String message, boolean usePAPI);

    /**
     * Replace, if loaded, PlaceholderAPI placeholders.
     * By default, it will use PlaceholderAPI.
     *
     * @param message the string to replace placeholders in.
     * @param player  the player to replace placeholders for.
     * @return the replaced string.
     */
    String replacePlaceholders(String message, OfflinePlayer player);

    /**
     * Replace, if loaded, PlaceholderAPI placeholders.
     * By default, it will use PlaceholderAPI.
     *
     * @param message the string to replace placeholders in.
     * @return the replaced string.
     */
    String replacePlaceholders(String message);

    /**
     * Replace custom placeholders in a list of strings and if loaded PlaceholderAPI placeholders.
     *
     * @param messages     the list of strings to replace placeholders in.
     * @param targets      the targets to replace.
     * @param replacements the replacements.
     * @param player       the player to replace placeholders for.
     * @param usePAPI      if true, will use PlaceholderAPI.
     * @return the replaced list of strings.
     */
    List<String> replacePlaceholders(List<String> messages, @NotNull String[] targets, @NotNull Object[] replacements, OfflinePlayer player, boolean usePAPI);

    /**
     * Replace custom placeholders in a list of strings and if loaded PlaceholderAPI placeholders.
     *
     * @param messages     the list of strings to replace placeholders in.
     * @param targets      the targets to replace.
     * @param replacements the replacements.
     * @param usePAPI      if true, will use PlaceholderAPI.
     * @return the replaced list of strings.
     */
    List<String> replacePlaceholders(List<String> messages, @NotNull String[] targets, @NotNull Object[] replacements, boolean usePAPI);

    /**
     * Replace custom placeholders in a list of strings and if loaded PlaceholderAPI placeholders.
     * By default, it will use PlaceholderAPI.
     *
     * @param messages     the list of strings to replace placeholders in.
     * @param targets      the targets to replace.
     * @param replacements the replacements.
     * @param player       the player to replace placeholders for.
     * @return the replaced list of strings.
     */
    List<String> replacePlaceholders(List<String> messages, @NotNull String[] targets, @NotNull Object[] replacements, OfflinePlayer player);

    /**
     * Replace custom placeholders in a list of strings and if loaded PlaceholderAPI placeholders.
     *
     * @param messages     the list of strings to replace placeholders in.
     * @param targets      the targets to replace.
     * @param replacements the replacements.
     * @return the replaced list of strings.
     */
    List<String> replacePlaceholders(List<String> messages, @NotNull String[] targets, @NotNull Object[] replacements);

    /**
     * Replace custom placeholders in a list of strings and if loaded PlaceholderAPI placeholders.
     *
     * @param messages    the list of strings to replace placeholders in.
     * @param target      the target to replace.
     * @param replacement the replacement.
     * @param player      the player to replace placeholders for.
     * @param usePAPI     if true, will use PlaceholderAPI.
     * @return the replaced list of strings.
     */
    List<String> replacePlaceholders(List<String> messages, @NotNull String target, @NotNull Object replacement, OfflinePlayer player, boolean usePAPI);

    /**
     * Replace custom placeholders in a list of strings and if loaded PlaceholderAPI placeholders.
     *
     * @param messages    the list of strings to replace placeholders in.
     * @param target      the target to replace.
     * @param replacement the replacement.
     * @param usePAPI     if true, will use PlaceholderAPI.
     * @return the replaced list of strings.
     */
    List<String> replacePlaceholders(List<String> messages, @NotNull String target, @NotNull Object replacement, boolean usePAPI);

    /**
     * Replace custom placeholders in a list of strings and if loaded PlaceholderAPI placeholders.
     * By default, it will use PlaceholderAPI.
     *
     * @param messages    the list of strings to replace placeholders in.
     * @param target      the target to replace.
     * @param replacement the replacement.
     * @param player      the player to replace placeholders for.
     * @return the replaced list of strings.
     */
    List<String> replacePlaceholders(List<String> messages, @NotNull String target, @NotNull Object replacement, OfflinePlayer player);

    /**
     * Replace custom placeholders in a list of strings and if loaded PlaceholderAPI placeholders.
     * By default, it will use PlaceholderAPI.
     *
     * @param messages    the list of strings to replace placeholders in.
     * @param target      the target to replace.
     * @param replacement the replacement.
     * @return the replaced list of strings.
     */
    List<String> replacePlaceholders(List<String> messages, @NotNull String target, @NotNull Object replacement);

    /**
     * Replace, if loaded, PlaceholderAPI placeholders.
     *
     * @param messages the list of strings to replace placeholders in.
     * @param player   the player to replace placeholders for.
     * @param usePAPI  if true, will use PlaceholderAPI.
     * @return the replaced list of strings.
     */
    List<String> replacePlaceholders(List<String> messages, OfflinePlayer player, boolean usePAPI);

    /**
     * Replace, if loaded, PlaceholderAPI placeholders.
     *
     * @param messages the list of strings to replace placeholders in.
     * @param usePAPI  if true, will use PlaceholderAPI.
     * @return the replaced list of strings.
     */
    List<String> replacePlaceholders(List<String> messages, boolean usePAPI);

    /**
     * Replace, if loaded, PlaceholderAPI placeholders.
     * By default, it will use PlaceholderAPI.
     *
     * @param messages the list of strings to replace placeholders in.
     * @param player   the player to replace placeholders for.
     * @return the replaced list of strings.
     */
    List<String> replacePlaceholders(List<String> messages, OfflinePlayer player);

    /**
     * Replace, if loaded, PlaceholderAPI placeholders.
     * By default, it will use PlaceholderAPI.
     *
     * @param messages the list of strings to replace placeholders in.
     * @return the replaced list of strings.
     */
    List<String> replacePlaceholders(List<String> messages);

    /**
     * Replace custom placeholders in a list of strings and if loaded PlaceholderAPI placeholders.
     *
     * @param messages     the list of strings to replace placeholders in.
     * @param targets      the targets to replace.
     * @param replacements the replacements.
     * @param player       the player to replace placeholders for.
     * @param usePAPI      if true, will use PlaceholderAPI.
     * @return the replaced list of strings.
     */
    String[] replacePlaceholders(String[] messages, @NotNull String[] targets, @NotNull Object[] replacements, OfflinePlayer player, boolean usePAPI);

    /**
     * Replace custom placeholders in a list of strings and if loaded PlaceholderAPI placeholders.
     *
     * @param messages     the list of strings to replace placeholders in.
     * @param targets      the targets to replace.
     * @param replacements the replacements.
     * @param usePAPI      if true, will use PlaceholderAPI.
     * @return the replaced list of strings.
     */
    String[] replacePlaceholders(String[] messages, @NotNull String[] targets, @NotNull Object[] replacements, boolean usePAPI);

    /**
     * Replace custom placeholders in a list of strings and if loaded PlaceholderAPI placeholders.
     * By default, it will use PlaceholderAPI.
     *
     * @param messages     the list of strings to replace placeholders in.
     * @param targets      the targets to replace.
     * @param replacements the replacements.
     * @param player       the player to replace placeholders for.
     * @return the replaced list of strings.
     */
    String[] replacePlaceholders(String[] messages, @NotNull String[] targets, @NotNull Object[] replacements, OfflinePlayer player);

    /**
     * Replace custom placeholders in a list of strings and if loaded PlaceholderAPI placeholders.
     *
     * @param messages     the list of strings to replace placeholders in.
     * @param targets      the targets to replace.
     * @param replacements the replacements.
     * @return the replaced list of strings.
     */
    String[] replacePlaceholders(String[] messages, @NotNull String[] targets, @NotNull Object[] replacements);

    /**
     * Replace custom placeholders in a list of strings and if loaded PlaceholderAPI placeholders.
     *
     * @param messages    the list of strings to replace placeholders in.
     * @param target      the target to replace.
     * @param replacement the replacement.
     * @param player      the player to replace placeholders for.
     * @param usePAPI     if true, will use PlaceholderAPI.
     * @return the replaced list of strings.
     */
    String[] replacePlaceholders(String[] messages, @NotNull String target, @NotNull Object replacement, OfflinePlayer player, boolean usePAPI);

    /**
     * Replace custom placeholders in a list of strings and if loaded PlaceholderAPI placeholders.
     *
     * @param messages    the list of strings to replace placeholders in.
     * @param target      the target to replace.
     * @param replacement the replacement.
     * @param usePAPI     if true, will use PlaceholderAPI.
     * @return the replaced list of strings.
     */
    String[] replacePlaceholders(String[] messages, @NotNull String target, @NotNull Object replacement, boolean usePAPI);

    /**
     * Replace custom placeholders in a list of strings and if loaded PlaceholderAPI placeholders.
     * By default, it will use PlaceholderAPI.
     *
     * @param messages    the list of strings to replace placeholders in.
     * @param target      the target to replace.
     * @param replacement the replacement.
     * @param player      the player to replace placeholders for.
     * @return the replaced list of strings.
     */
    String[] replacePlaceholders(String[] messages, @NotNull String target, @NotNull Object replacement, OfflinePlayer player);

    /**
     * Replace custom placeholders in a list of strings and if loaded PlaceholderAPI placeholders.
     * By default, it will use PlaceholderAPI.
     *
     * @param messages    the list of strings to replace placeholders in.
     * @param target      the target to replace.
     * @param replacement the replacement.
     * @return the replaced list of strings.
     */
    String[] replacePlaceholders(String[] messages, @NotNull String target, @NotNull Object replacement);

    /**
     * Replace, if loaded, PlaceholderAPI placeholders.
     *
     * @param messages the list of strings to replace placeholders in.
     * @param player   the player to replace placeholders for.
     * @param usePAPI  if true, will use PlaceholderAPI.
     * @return the replaced list of strings.
     */
    String[] replacePlaceholders(String[] messages, OfflinePlayer player, boolean usePAPI);

    /**
     * Replace, if loaded, PlaceholderAPI placeholders.
     *
     * @param messages the list of strings to replace placeholders in.
     * @param usePAPI  if true, will use PlaceholderAPI.
     * @return the replaced list of strings.
     */
    String[] replacePlaceholders(String[] messages, boolean usePAPI);

    /**
     * Replace, if loaded, PlaceholderAPI placeholders.
     * By default, it will use PlaceholderAPI.
     *
     * @param messages the list of strings to replace placeholders in.
     * @param player   the player to replace placeholders for.
     * @return the replaced list of strings.
     */
    String[] replacePlaceholders(String[] messages, OfflinePlayer player);

    /**
     * Replace, if loaded, PlaceholderAPI placeholders.
     * By default, it will use PlaceholderAPI.
     *
     * @param messages the list of strings to replace placeholders in.
     * @return the replaced list of strings.
     */
    String[] replacePlaceholders(String[] messages);
}
