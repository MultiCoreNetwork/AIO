package it.multicoredev.aio.api.tp;

import com.google.common.base.Preconditions;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Copyright © 2022 by Daniele Patella. All rights reserved.
 * This file is part of AIO.
 * <p>
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING,
 * BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY,
 * OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */
public class TeleportRequest {
    private final TeleportRequestType type;
    private final Player requester;
    private final Player target;

    /**
     * Create a new teleport request.
     *
     * @param type      the type of the request.
     * @param requester the requester.
     * @param target    the target.
     */
    public TeleportRequest(@NotNull TeleportRequestType type, @NotNull Player requester, @NotNull Player target) {
        Preconditions.checkNotNull(type);
        Preconditions.checkNotNull(requester);
        Preconditions.checkNotNull(target);

        this.type = type;
        this.requester = requester;
        this.target = target;
    }

    /**
     * Get the type of the request.
     *
     * @return the type of the request.
     */
    public TeleportRequestType getType() {
        return type;
    }

    /**
     * Get the requester.
     *
     * @return the requester.
     */
    public Player getRequester() {
        return requester;
    }

    /**
     * Get the target.
     *
     * @return the target.
     */
    public Player getTarget() {
        return target;
    }
}
