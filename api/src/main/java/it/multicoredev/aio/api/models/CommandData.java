package it.multicoredev.aio.api.models;

import com.google.common.base.Preconditions;
import it.multicoredev.mclib.json.JsonConfig;
import org.jetbrains.annotations.NotNull;

import java.util.*;

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
public class CommandData extends JsonConfig {
    private Boolean enabled;
    private List<String> alias;
    private String description;
    private Map<String, List<String>> usages;

    public CommandData(boolean enabled, @NotNull String description, @NotNull Map<String, List<String>> usages, String... alias) {
        Preconditions.checkNotNull(description);
        Preconditions.checkNotNull(usages);

        this.enabled = enabled;
        this.description = description;
        this.usages = usages;
        this.alias = Arrays.asList(alias);
    }

    public CommandData(boolean enabled, @NotNull String description, @NotNull String usage, String... alias) {
        Preconditions.checkNotNull(description);
        Preconditions.checkNotNull(usage);

        Map<String, List<String>> usages = new HashMap<>();
        usages.put("default", Collections.singletonList(usage));

        this.enabled = enabled;
        this.description = description;
        this.usages = usages;
        this.alias = Arrays.asList(alias);
    }

    public CommandData(@NotNull String description, @NotNull Map<String, List<String>> usages, String... alias) {
        this(true, description, usages, alias);
    }

    public CommandData(@NotNull String description, @NotNull String usage, String... alias) {
        this(true, description, usage, alias);
    }

    /**
     * Check if the command is enabled.
     *
     * @return true if the command is enabled, false otherwise.
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Get the aliases of the command.
     *
     * @return the aliases of the command.
     */
    public List<String> getAlias() {
        return Collections.unmodifiableList(alias);
    }

    /**
     * Get the description of the command.
     *
     * @return the description of the command.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Get the usages of the command linked to a given key.
     *
     * @param key The key of the usages.
     * @return The usages.
     */
    public List<String> getUsages(@NotNull String key) {
        Preconditions.checkNotNull(key);
        if (usages.containsKey(key)) return usages.get(key);
        else return new ArrayList<>();
    }

    /**
     * Get all the usages of the command.
     *
     * @return The usages.
     */
    public List<String> getUsages() {
        List<String> usages = new ArrayList<>();
        this.usages.values().forEach(usages::addAll);
        return usages;
    }

    @Override
    public void init() {
        if (enabled == null) enabled = false;
        if (description == null) description = "";
        if (usages == null) usages = new HashMap<>();
    }

    /**
     * Helper class for building the usages map.
     */
    public static class UsagesBuilder {
        private Map<String, List<String>> usages;

        public UsagesBuilder() {
            usages = new HashMap<>();
        }

        /**
         * Add a usage to the map.
         *
         * @param key    The key of the usage.
         * @param usages The usages.
         * @return The builder.
         */
        public UsagesBuilder add(@NotNull String key, @NotNull String... usages) {
            Preconditions.checkNotNull(key);
            Preconditions.checkNotNull(usages);

            if (!this.usages.containsKey(key)) this.usages.put(key, new ArrayList<>());
            this.usages.get(key).addAll(Arrays.asList(usages));
            return this;
        }

        /**
         * Add a usage to the map.
         *
         * @param key    The key of the usage.
         * @param usages The usages.
         * @return The builder.
         */
        public UsagesBuilder add(@NotNull String key, @NotNull List<String> usages) {
            Preconditions.checkNotNull(key);
            Preconditions.checkNotNull(usages);

            if (!this.usages.containsKey(key)) this.usages.put(key, new ArrayList<>());
            this.usages.get(key).addAll(usages);
            return this;
        }

        /**
         * Build the usages map.
         *
         * @return The usages map.
         */
        public Map<String, List<String>> build() {
            return usages;
        }
    }
}
