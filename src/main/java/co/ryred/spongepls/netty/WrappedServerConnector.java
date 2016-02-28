/*
 * http://ryred.co/
 * ace[at]ac3-servers.eu
 *
 * =================================================================
 *
 * Copyright (c) 2015, Cory Redmond
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
 *  Neither the name of SpongePls nor the names of its
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
 *
 */

package co.ryred.spongepls.netty;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.BungeeServerInfo;
import net.md_5.bungee.ServerConnector;
import net.md_5.bungee.UserConnection;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.connection.LoginResult;
import net.md_5.bungee.forge.ForgeServerHandler;
import net.md_5.bungee.netty.ChannelWrapper;
import net.md_5.bungee.protocol.Protocol;
import net.md_5.bungee.protocol.packet.Handshake;

import java.lang.reflect.Field;

/**
 * @author Cory Redmond
 *         Created by acech_000 on 13/11/2015.
 */
public class WrappedServerConnector extends ServerConnector
{
	public WrappedServerConnector( ProxyServer bungee, UserConnection user, BungeeServerInfo target )
	{
		super( bungee, user, target );
	}

	public static UserConnection getUserConnection( ServerConnector sc ) throws NoSuchFieldException, IllegalAccessException
	{

		Field field = ServerConnector.class.getDeclaredField( "user" );
		field.setAccessible( true );
		return ( (UserConnection) field.get( sc ) );

	}

	public static BungeeServerInfo getTarget( ServerConnector sc ) throws NoSuchFieldException, IllegalAccessException
	{

		Field field = ServerConnector.class.getDeclaredField( "target" );
		field.setAccessible( true );
		return ( (BungeeServerInfo) field.get( sc ) );

	}

	public static ProxyServer getBungee( ServerConnector sc ) throws NoSuchFieldException, IllegalAccessException
	{

		Field field = ServerConnector.class.getDeclaredField( "bungee" );
		field.setAccessible( true );
		return ( (ProxyServer) field.get( sc ) );

	}

	public static void setHandler( ServerConnector sc, ForgeServerHandler handler ) throws NoSuchFieldException, IllegalAccessException
	{

		Field field = ServerConnector.class.getDeclaredField( "handshakeHandler" );
		field.setAccessible( true );
		field.set( sc, handler );

	}

	public static void setChannel( ServerConnector sc, ChannelWrapper ch ) throws NoSuchFieldException, IllegalAccessException
	{

		Field field = ServerConnector.class.getDeclaredField( "ch" );
		field.setAccessible( true );
		field.set( sc, ch );

	}

	@Override
	public void connected( ChannelWrapper ch ) throws Exception
	{

		setChannel( this, ch );

		UserConnection user = getUserConnection( this );
		BungeeServerInfo target = getTarget( this );

		setHandler( this, new ForgeServerHandler( user, ch, target ) );
		Handshake originalHandshake = user.getPendingConnection().getHandshake();
		Handshake copiedHandshake = new Handshake( originalHandshake.getProtocolVersion(), originalHandshake.getHost(), originalHandshake.getPort(), 2 );

		String newHost = copiedHandshake.getHost() + "\00" + user.getAddress().getHostString() + "\00" + user.getUUID();

		LoginResult profile = user.getPendingConnection().getLoginProfile();
		if ( profile != null && profile.getProperties() != null && profile.getProperties().length > 0 ) {
			newHost += "\00" + BungeeCord.getInstance().gson.toJson( profile.getProperties() );
		}

		// In order to be able to support Forge servers that use IP forwarding, we change the original host string from:
		//     <host><extradata>
		// to
		//     <host><BungeeData>\00|<extradata>
		// Implementations can then simply split on the string "\00|" and take the first chunk of the string and
		// do their normal processing, and may ignore the extra data if they so wish.
		if ( !user.getExtraDataInHandshake().isEmpty() ) {
			newHost += "\00|" + user.getExtraDataInHandshake();
		}

		copiedHandshake.setHost( newHost );

		ch.write( copiedHandshake );

		ch.setProtocol( Protocol.LOGIN );
		ch.write( user.getPendingConnection().getLoginRequest() );

	}

}
