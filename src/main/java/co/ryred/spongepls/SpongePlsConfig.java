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

import com.google.common.io.ByteStreams;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.*;

/**
 * @author Cory Redmond
 *         Created by acech_000 on 12/11/2015.
 */
public class SpongePlsConfig
{

	private Plugin plugin;
	private Configuration config;

	public SpongePlsConfig( Plugin plugin )
	{

		this.plugin = plugin;

		if ( !plugin.getDataFolder().exists() ) {
			plugin.getDataFolder().mkdirs();
		}
		if ( !getFile().exists() ) {
			try {
				getFile().createNewFile();
				try ( InputStream is = getResourceAsStream(); OutputStream os = new FileOutputStream( getFile() ) ) {
					ByteStreams.copy( is, os );
				}
			} catch ( IOException e ) {
				throw new RuntimeException( "Unable to create config file", e );
			}
		}

	}

	public Configuration getConfig()
	{
		try {
			return this.config == null ? ( this.config = ConfigurationProvider.getProvider( YamlConfiguration.class ).load( getFile() ) ) : config;
		} catch ( IOException e ) {
			e.printStackTrace();
			return null;
		}
	}

	public void saveConfig()
	{
		try {
			ConfigurationProvider.getProvider( YamlConfiguration.class ).save( config, getFile() );
		} catch ( IOException e ) {
			e.printStackTrace();
			plugin.getLogger().severe( "Couldn't save config file!" );
		}
	}

	public void reload() throws IOException
	{
		this.config = ConfigurationProvider.getProvider( YamlConfiguration.class ).load( getFile() );
	}

	private File getFile()
	{
		return new File( plugin.getDataFolder(), "config.yml");
	}

	private InputStream getResourceAsStream()
	{
		return plugin.getResourceAsStream( "config.yml" );
	}

}
