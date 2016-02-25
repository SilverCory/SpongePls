/*
 * http://ryred.co/
 * ace[at]ac3-servers.eu
 *
 * =================================================================
 *
 * Copyright (c) 2016, Cory Redmond
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 *  Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 *  Neither the name of spongepls nor the names of its
 *   contributors may be used to endorse or promote products derived from
 *   this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package co.ryred.spongepls;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.UserConnection;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.connection.InitialHandler;
import net.md_5.bungee.connection.LoginResult;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.protocol.packet.Handshake;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.UUID;

/**
 * Created by Cory Redmond on 25/02/2016.
 *
 * @author Cory Redmond <ace@ac3-servers.eu>
 */
public class PlayerListener implements Listener {

    private final SpongePlsPlugin plugin;
    private final Field handshakeField;
    private final HashMap<UUID, Handshake> originalHandshakes = new HashMap<>();

    public PlayerListener(SpongePlsPlugin plugin) {
        this.plugin = plugin;

        Field field = null;

        try {
            field = InitialHandler.class.getDeclaredField( "handshake" );
            field.setAccessible( true );
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        handshakeField = field;

    }

    @EventHandler
    public void onPlayerJoin( PostLoginEvent event ) {
        if( event.getPlayer() instanceof UserConnection ) {
            UserConnection user = ((UserConnection) event.getPlayer());
            originalHandshakes.put(user.getUniqueId(), user.getPendingConnection().getHandshake());
        }
    }

    @EventHandler
    public void onPlayerLeave( PlayerDisconnectEvent event ) {
        if( event.getPlayer() instanceof UserConnection ) {
            UserConnection user = ((UserConnection) event.getPlayer());
            originalHandshakes.remove(user.getUniqueId());
        }
    }

    @EventHandler
    public void onServerConnect( ServerConnectEvent event ) {

        if( event.getPlayer() instanceof UserConnection ) {

            UserConnection user = ((UserConnection) event.getPlayer());
            Handshake handshake = originalHandshakes.get(event.getPlayer().getUniqueId());

            if (SpongePlsPlugin.isAllowed(event.getTarget().getName())) {
                Handshake copiedHandshake = new Handshake(handshake.getProtocolVersion(), handshake.getHost(), handshake.getPort(), 2);

                String newHost = copiedHandshake.getHost() + "\00" + user.getAddress().getHostString() + "\00" + user.getUUID();

                LoginResult profile = user.getPendingConnection().getLoginProfile();
                if (profile != null && profile.getProperties() != null && profile.getProperties().length > 0) {
                    newHost += "\00" + BungeeCord.getInstance().gson.toJson(profile.getProperties());
                }

                if (!user.getExtraDataInHandshake().isEmpty()) {
                    newHost += "\00|" + user.getExtraDataInHandshake();
                }

                copiedHandshake.setHost(newHost);

            }

            try {
                setHandshake(user.getPendingConnection(), handshake);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        }

    }

    public void setHandshake( InitialHandler handler, Handshake handshake ) throws IllegalAccessException {
        if( handshakeField == null ) return;
        handshakeField.set( handler, handshake );
    }

}
