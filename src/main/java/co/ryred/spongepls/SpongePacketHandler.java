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

package co.ryred.spongepls;

import de.inventivegames.packetlistener.handler.PacketHandler;
import de.inventivegames.packetlistener.handler.ReceivedPacket;
import de.inventivegames.packetlistener.handler.SentPacket;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.UserConnection;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.protocol.packet.Handshake;

import java.util.regex.Pattern;

/**
 * @author Cory Redmond
 *         Created by acech_000 on 12/11/2015.
 */
public class SpongePacketHandler extends PacketHandler
{
	private final SpongePlsPlugin plugin;

	public SpongePacketHandler( SpongePlsPlugin plugin ) {
		super(plugin);
		this.plugin = plugin;
	}

	@Override
	public void onSend( SentPacket sentPacket )
	{
		try {

			if( sentPacket.getPacket() != null && sentPacket.getPacket() instanceof Handshake ) {

				if( !BungeeCord.getInstance().config.isIpForward() ) return;
				if( sentPacket.getPlayer() == null ) return;
				if( !(sentPacket.getConection() instanceof Server ) ) return;;

				Server server = ( (Server) sentPacket.getConection() );
				UserConnection user = ( (UserConnection) sentPacket.getPlayer() );
				Handshake handshake = ( (Handshake) sentPacket.getPacket() );

				if( !isAllowed( server.getInfo().getName() ) ) return;

				if( !user.getExtraDataInHandshake().isEmpty() ) {
					handshake.setHost( handshake.getHost() + "\00|" + user.getExtraDataInHandshake() );
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onReceive( ReceivedPacket receivedPacket )
	{

	}

	/**
	 *
	 * http://blog.janjonas.net/2012-03-06/java-test-string-match-wildcard-expression
	 *
	 * @param text Text to test
	 * @return True if the text matches the wildcard pattern
	 */
	public boolean isAllowed(String text )
	{

		for( String pattern : plugin.getConfig().getStringList( "allowed-servers" ) ) {
			if( Pattern.compile( pattern, Pattern.CASE_INSENSITIVE ).matcher( text ).find() ) return true;
		}

		return false;

	}

}
