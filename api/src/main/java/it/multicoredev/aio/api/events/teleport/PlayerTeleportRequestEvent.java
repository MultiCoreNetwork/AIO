package it.multicoredev.aio.api.events.teleport;

import com.google.common.base.Preconditions;
import it.multicoredev.aio.api.tp.Teleport;
import it.multicoredev.aio.api.tp.TeleportRequest;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

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
public class PlayerTeleportRequestEvent extends Event implements Cancellable {
    private static final HandlerList HANDLERS = new HandlerList();
    private boolean cancelled;

    private final TeleportRequest request;
    private TeleportRequest.CancelReason cancelReason;
    private String cancelMessageRequester;
    private String cancelMessageTarget;

    /**
     * PlayerTeleportRequestEvent is called when a teleport request is committed by a player.
     *
     * @param request the teleport request.
     */
    public PlayerTeleportRequestEvent(@NotNull TeleportRequest request) {
        Preconditions.checkNotNull(request);

        this.request = request;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
        if (cancelled && cancelReason == null) cancelReason = TeleportRequest.CancelReason.OTHER;
    }

    /**
     * Get the {@link Player} who requested the teleport.
     *
     * @return the player who requested the teleport.
     */
    public Player getRequester() {
        return request.getRequester();
    }

    /**
     * Get the {@link Player} who will receive the request.
     *
     * @return the player who will receive the request.
     */
    public Player getTarget() {
        return request.getTarget();
    }

    /**
     * Set the player who will receive the request.
     *
     * @param target the player who will receive the request.
     */
    public void setTarget(@NotNull Player target) {
        request.setTarget(target);
    }

    /**
     * Get the {@link TeleportRequest.RequestType} of the request.
     *
     * @return the request type.
     */
    public TeleportRequest.RequestType getRequestType() {
        return request.getType();
    }

    /**
     * Get the timeout of the request.
     *
     * @return the timeout.
     */
    public long getTimeout() {
        return request.getTimeout();
    }

    /**
     * Set the timeout of the request.
     *
     * @param timeout the new timeout.
     */
    public void setTimeout(long timeout) {
        request.setTimeout(timeout);
    }

    /**
     * Get the timestamp of the request creation.
     *
     * @return the timestamp.
     */
    public long getTimestamp() {
        return request.getTimestamp();
    }

    /**
     * Get the message to send to the requester.
     *
     * @return the message to send to the requester.
     */
    public String getRequesterMessage() {
        return request.getRequesterMessage();
    }

    /**
     * Set the message to send to the requester.
     *
     * @param message the message to send to the requester.
     */
    public void setRequesterMessage(String message) {
        request.setRequesterMessage(message);
    }

    /**
     * Get the message to send to the target.
     *
     * @return the message to send to the target.
     */
    public String getTargetMessage() {
        return request.getTargetMessage();
    }

    /**
     * Set the message to send to the target.
     *
     * @param message the message to send to the target.
     */
    public void setTargetMessage(String message) {
        request.setTargetMessage(message);
    }

    /**
     * Get the reason of the cancellation of the teleport.
     *
     * @return the reason of the cancellation of the teleport.
     */
    public TeleportRequest.CancelReason getCancelReason() {
        return cancelReason;
    }

    /**
     * Set the reason of the cancellation of the teleport.
     *
     * @param cancelReason the reason of the cancellation of the teleport.
     */
    public void setCancelReason(TeleportRequest.CancelReason cancelReason) {
        this.cancelReason = cancelReason;
    }

    /**
     * Get the message to send to the requester when the request is cancelled.
     *
     * @return the message to send to the requester when the request is cancelled.
     */
    public String getCancelMessageRequester() {
        return cancelMessageRequester;
    }
    
    /**
     * Set the message to send to the requester when the request is cancelled.
     *
     * @param message the message to send to the requester when the request is cancelled.
     */
    public void setCancelMessageRequester(String message) {
        this.cancelMessageRequester = message;
    }

    /**
     * Get the message to send to the target when the request is cancelled.
     *
     * @return the message to send to the target when the request is cancelled.
     */
    public String getCancelMessageTarget() {
        return cancelMessageTarget;
    }

    /**
     * Set the message to send to the target when the request is cancelled.
     *
     * @param message the message to send to the target when the request is cancelled.
     */
    public void setCancelMessageTarget(String message) {
        this.cancelMessageTarget = message;
    }
}
