package it.multicoredev.aio.storage.config.modules;

import com.google.gson.annotations.SerializedName;
import it.multicoredev.aio.api.Module;

import java.util.HashMap;
import java.util.Map;

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
public class ChatModule extends Module {
    @SerializedName("chat_format")
    public String chatFormat;
    @SerializedName("group_formats")
    public Map<String, String> groupFormats;
    @SerializedName("chat_radius")
    public Double chatRadius;

    public ChatModule() {
        super("chat");
    }

    @Override
    protected void init() {
        if (chatFormat == null) chatFormat = "&f<&7{DISPLAYNAME}&f>&r {MESSAGE}";
        if (groupFormats == null) {
            groupFormats = new HashMap<>();
            groupFormats.put("admin", "&7[&4{GROUP}&7] &c{DISPLAYNAME} &f>&r {MESSAGE}");
            groupFormats.put("mod", "&7[&6{GROUP}&7] &e{DISPLAYNAME} &f>&r {MESSAGE}");
            groupFormats.put("default", "&7[&2{GROUP}&7] &a{DISPLAYNAME} &f>&r {MESSAGE}");
        }

        chatRadius = 0D;
    }

    @Override
    public boolean isValid() {
        if (chatFormat == null || chatFormat.trim().isEmpty()) {
            chatFormat = null;
            init();
            return false;
        }

        return true;
    }
}
