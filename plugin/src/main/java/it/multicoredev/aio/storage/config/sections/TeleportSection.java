package it.multicoredev.aio.storage.config.sections;

import com.google.gson.annotations.SerializedName;
import it.multicoredev.mclib.json.JsonConfig;

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
public class TeleportSection extends JsonConfig {
    @SerializedName("teleport_request_timeout")
    public Integer tpRequestTimeout;

    @SerializedName("default_delay")
    private Integer defDelay;

    @SerializedName("back_delay")
    private Integer backDelay;
    @SerializedName("home_delay")
    private Integer homeDelay;
    @SerializedName("rtp_delay")
    private Integer rtpDelay;
    @SerializedName("spawn_delay")
    private Integer spawnDelay;
    @SerializedName("tp_delay")
    private Integer tpDelay;
    @SerializedName("tpa_delay")
    private Integer tpaDelay;
    @SerializedName("tpahere_delay")
    private Integer tpahereDelay;
    @SerializedName("warp_delay")
    private Integer warpDelay;

    @SerializedName("homes")
    private HomesSection homesSection;
    @SerializedName("rtp")
    public RTPSection rtpSection;

    @Override
    public void init() {
        if (defDelay == null) defDelay = 0;
        if (backDelay == null) backDelay = -1;
        if (homeDelay == null) homeDelay = -1;
        if (rtpDelay == null) rtpDelay = -1;
        if (spawnDelay == null) spawnDelay = -1;
        if (tpDelay == null) tpDelay = -1;
        if (tpaDelay == null) tpaDelay = -1;
        if (tpahereDelay == null) tpahereDelay = -1;
        if (warpDelay == null) warpDelay = -1;

        if (homesSection == null) {
            homesSection = new HomesSection();
            homesSection.init();
        }
        if (rtpSection == null) {
            rtpSection = new RTPSection();
            rtpSection.init();
        }
    }

    public int getDefaultDelay() {
        return Math.min(0, defDelay);
    }

    public int getBackDelay() {
        if (backDelay < 0) return getDefaultDelay();
        else return backDelay;
    }

    public Integer getHomeDelay() {
        if (homeDelay < 0) return getDefaultDelay();
        else return homeDelay;
    }

    public Integer getRtpDelay() {
        if (rtpDelay < 0) return getDefaultDelay();
        else return rtpDelay;
    }

    public Integer getSpawnDelay() {
        if (spawnDelay < 0) return getDefaultDelay();
        else return spawnDelay;
    }

    public Integer getTpDelay() {
        if (tpDelay < 0) return getDefaultDelay();
        else return tpDelay;
    }

    public Integer getTpaDelay() {
        if (tpaDelay < 0) return getDefaultDelay();
        else return tpaDelay;
    }

    public Integer getTpahereDelay() {
        if (tpahereDelay < 0) return getDefaultDelay();
        else return tpahereDelay;
    }

    public Integer getWarpDelay() {
        if (warpDelay < 0) return getDefaultDelay();
        else return warpDelay;
    }
}
