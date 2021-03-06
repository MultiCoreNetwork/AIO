package it.multicoredev.aio.api.tp;

import com.google.common.base.Preconditions;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Date;

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
public class TeleportRequest implements Comparable<TeleportRequest> {
    private final Player requester;
    private Player target;
    private final RequestType type;
    private long timeout;
    private String requesterMessage;
    private String targetMessage;
    private final long timestamp;

    /**
     * Create a new TeleportRequest.
     *
     * @param requester        the player who request the teleport.
     * @param target           the player who will receive the request.
     * @param type             the type of the request.
     * @param timeout          the timeout in seconds of the request. After this time the request will expire.
     * @param requesterMessage the message to send to the requester.
     * @param targetMessage    the message to send to the target.
     */
    public TeleportRequest(@NotNull Player requester, @NotNull Player target, @NotNull RequestType type, long timeout, String requesterMessage, String targetMessage) {
        Preconditions.checkNotNull(requester);
        Preconditions.checkNotNull(target);
        Preconditions.checkNotNull(type);

        this.requester = requester;
        this.target = target;
        this.type = type;
        this.timeout = timeout;
        this.requesterMessage = requesterMessage;
        this.targetMessage = targetMessage;
        this.timestamp = new Date().getTime();
    }

    /**
     * Create a new TeleportRequest.
     *
     * @param requester        the player who request the teleport.
     * @param target           the player who will receive the request.
     * @param type             the type of the request.
     * @param timeout          the timeout in seconds of the request. After this time the request will expire.
     */
    public TeleportRequest(@NotNull Player requester, @NotNull Player target, @NotNull RequestType type, long timeout) {
        this(requester, target, type, timeout, null, null);
    }

    /**
     * Get the player who requested the teleport.
     *
     * @return the requester.
     */
    public Player getRequester() {
        return requester;
    }

    /**
     * Get the player who will receive the request.
     *
     * @return the target.
     */
    public Player getTarget() {
        return target;
    }

    /**
     * Set the player who will receive the request.
     *
     * @param target the new target.
     * @return this object.
     */
    public TeleportRequest setTarget(@NotNull Player target) {
        Preconditions.checkNotNull(target);

        this.target = target;
        return this;
    }

    /**
     * Get the type of the request.
     *
     * @return the type.
     */
    public RequestType getType() {
        return type;
    }

    /**
     * Get the timeout in seconds of the request.
     *
     * @return the timeout.
     */
    public long getTimeout() {
        return timeout;
    }

    /**
     * Set the timeout in seconds of the request.
     *
     * @param timeout the new timeout.
     * @return this object.
     */
    public TeleportRequest setTimeout(long timeout) {
        this.timeout = timeout;
        return this;
    }

    /**
     * Get the timestamp of the request creation.
     *
     * @return the timestamp.
     */
    public long getTimestamp() {
        return timestamp;
    }

    /**
     * Get the message to send to the requester.
     *
     * @return the message.
     */
    public String getRequesterMessage() {
        return requesterMessage;
    }

    /**
     * Set the message to send to the target.
     *
     * @return this object.
     */
    public String setRequesterMessage(String message) {
        this.requesterMessage = message;
        return this.requesterMessage;
    }

    /**
     * Get the message to send to the target.
     *
     * @return the message.
     */
    public String getTargetMessage() {
        return targetMessage;
    }

    /**
     * Set the message to send to the target.
     *
     * @return this object.
     */
    public String setTargetMessage(String message) {
        this.targetMessage = message;
        return this.targetMessage;
    }

    @Override
    public int compareTo(@NotNull TeleportRequest request) {
        return Long.compare(this.timestamp, request.timestamp);
    }

    public enum RequestType {
        TPA,
        TPAHERE
    }

    public enum CancelReason {
        DENIED,
        EXPIRED,
        CANCELLED,
        REPLACED,
        MOVEMENT,
        DAMAGE,
        UNSAFE_DESTINATION,
        INSUFFICIENT_MONEY,
        OTHER
    }
}
