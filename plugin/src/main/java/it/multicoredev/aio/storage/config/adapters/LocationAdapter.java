package it.multicoredev.aio.storage.config.adapters;

import com.google.gson.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.lang.reflect.Type;

/**
 * Copyright © 2021 - 2022 by Lorenzo Magni & Daniele Patella
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
public class LocationAdapter implements JsonSerializer<Location>, JsonDeserializer<Location> {
    @Override
    public Location deserialize(JsonElement json, Type t, JsonDeserializationContext ctx) throws JsonParseException {
        if (!json.isJsonObject()) return null;
        JsonObject obj = json.getAsJsonObject();

        if (!obj.has("world")) return null;
        if (!obj.has("x")) return null;
        if (!obj.has("y")) return null;
        if (!obj.has("z")) return null;

        String worldName = obj.get("world").getAsString();
        World world = Bukkit.getWorld(worldName);

        double x = obj.get("x").getAsDouble();
        double y = obj.get("y").getAsDouble();
        double z = obj.get("z").getAsDouble();

        Location location = new Location(world, x, y, z);

        if (obj.has("yaw")) location.setYaw(obj.get("yaw").getAsFloat());
        if (obj.has("pitch")) location.setPitch(obj.get("pitch").getAsFloat());

        return location;
    }

    @Override
    public JsonElement serialize(Location location, Type t, JsonSerializationContext ctx) {
        JsonObject json = new JsonObject();
        if (location.getWorld() != null) json.addProperty("world", location.getWorld().getName());
        json.addProperty("x", location.getX());
        json.addProperty("y", location.getY());
        json.addProperty("z", location.getZ());
        json.addProperty("yaw", location.getYaw());
        json.addProperty("pitch", location.getPitch());

        return json;
    }
}
