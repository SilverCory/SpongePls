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

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

import java.io.IOException;

/**
 * @author Cory Redmond
 *         Created by acech_000 on 12/11/2015.
 */
public class SpongePlsCommand extends Command
{
	private final SpongePlsPlugin plugin;

	public SpongePlsCommand( SpongePlsPlugin plugin ) {
		super( "SpongePls", null );
		this.plugin = plugin;
	}

	@Override
	public void execute( CommandSender sender, String[] args )
	{

		if( args.length == 1 && args[0].equalsIgnoreCase( "reload" ) && sender.hasPermission( "spongepls.reload" ) ) {
			try {
				plugin.getConfiguration().reload();
				sender.sendMessage( ChatColor.translateAlternateColorCodes( '&', "&aSpongePls configuration is reloaded!" ) );
			} catch ( IOException e ) {
				e.printStackTrace();
				sender.sendMessage( ChatColor.translateAlternateColorCodes( '&', "&cError reloading SpongePls, check the config." ) );
			}
			return;
		}

		sender.sendMessages(
				ChatColor.translateAlternateColorCodes( '&', "&9==== &2SpongePls&9 ====" ),
				ChatColor.translateAlternateColorCodes( '&', "&bAuthor: &eCory Redmond" ),
				ChatColor.translateAlternateColorCodes( '&', "&bVersion: &e" + plugin.getDescription().getVersion() )
		);

		if( sender.hasPermission( "spongepls.reload" ) ) {
			sender.sendMessages(
					ChatColor.translateAlternateColorCodes( '&', "&dCommands:" ),
					ChatColor.translateAlternateColorCodes( '&', " &e/spongepls reload" ),
					ChatColor.translateAlternateColorCodes( '&', " &5  Reloads the configuration." )
			);
		}

	}

}
