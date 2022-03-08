package it.multicoredev.aio.api;

import com.google.common.base.Preconditions;
import com.google.gson.*;
import com.google.gson.annotations.JsonAdapter;
import it.multicoredev.mclib.json.JsonConfig;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;

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
@JsonAdapter(Module.Adapter.class)
public abstract class Module extends JsonConfig {
    protected String name;

    /**
     * Create a new module.
     *
     * @param name the name of the module.
     */
    public Module(@NotNull String name) {
        Preconditions.checkNotNull(name);
        Preconditions.checkArgument(!name.trim().isEmpty());

        this.name = name.trim().toLowerCase();
    }

    /**
     * Get the name of this module.
     *
     * @return the name of this module.
     */
    public String getName() {
        return name;
    }

    /**
     * Check if this module is valid.
     *
     * @return true if the module is valid, otherwise false.
     */
    public abstract boolean isValid();

    /**
     * Initialize class variables with default values.
     * This will be the default values if one of this is missing.
     */
    public abstract void init();

    public static class Adapter implements JsonSerializer<Module>, JsonDeserializer<Module> {

        @Override
        public Module deserialize(JsonElement json, Type type, JsonDeserializationContext ctx) throws JsonParseException {
            if (!json.isJsonObject()) return null;
            JsonObject obj = json.getAsJsonObject();
            if (!obj.has("name") || !obj.get("name").isJsonPrimitive()) return null;

            return ctx.deserialize(json, AIO.getInstance().getModuleManager().getModuleClass(obj.get("name").getAsString()));
        }

        @Override
        public JsonElement serialize(Module module, Type type, JsonSerializationContext ctx) {
            return ctx.serialize(module, AIO.getInstance().getModuleManager().getModuleClass(module.name));
        }
    }
}
