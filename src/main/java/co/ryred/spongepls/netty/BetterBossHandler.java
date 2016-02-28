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

import co.ryred.spongepls.SpongePlsPlugin;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.BungeeServerInfo;
import net.md_5.bungee.ServerConnector;
import net.md_5.bungee.UserConnection;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.netty.PacketHandler;

/**
 * @author Cory Redmond
 *         Created by acech_000 on 13/11/2015.
 */
public class BetterBossHandler
{

	public static PacketHandler invokePls( PacketHandler handler )
	{

		if ( BungeeCord.getInstance().getConfig().isIpForward() && handler instanceof ServerConnector ) {

			ServerConnector sc = ( (ServerConnector) handler );
			try {
				if ( SpongePlsPlugin.get__INSTANCE__().isAllowed( WrappedServerConnector.getTarget( sc ).getName() ) ) {

					ProxyServer bungee = WrappedServerConnector.getBungee( sc );
					UserConnection uc = WrappedServerConnector.getUserConnection( sc );
					BungeeServerInfo si = WrappedServerConnector.getTarget( sc );

					WrappedServerConnector wsc = new WrappedServerConnector( bungee, uc, si );

					SpongePlsPlugin.get__INSTANCE__().getLogger().info( "Using the Forge Server Connector for " + uc.getName() );
					SpongePlsPlugin.get__INSTANCE__().getLogger().info( " for the target \"" + si.getName() + "\"." );

					return wsc;

				}
			} catch ( Exception e ) {
				e.printStackTrace();
				return handler;
			}

		}

		return handler;

	}

}
