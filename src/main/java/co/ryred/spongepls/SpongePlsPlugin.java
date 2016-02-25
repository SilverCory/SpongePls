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

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author Cory Redmond
 *         Created by acech_000 on 12/11/2015.
 */
public class SpongePlsPlugin extends Plugin
{

	@Getter
	@Setter(AccessLevel.PRIVATE)
	public static SpongePlsPlugin __INSTANCE__ = null;
	@Setter(AccessLevel.PACKAGE)
	private static List<String> patterns = new ArrayList<>();
	@Getter( AccessLevel.PACKAGE )
	private SpongePlsConfig configuration;
	private boolean changedIpforwarding;

	/**
	 * http://blog.janjonas.net/2012-03-06/java-test-string-match-wildcard-expression
	 *
	 * @param text Text to test
	 * @return True if the text matches the wildcard pattern
	 */
	public static boolean isAllowed( String text )
	{

		for ( String pattern : patterns ) {
			if ( Pattern.compile( pattern, Pattern.CASE_INSENSITIVE ).matcher( text ).find() ) return true;
		}

		return false;

	}

	@Override
	public void onLoad()
	{
		set__INSTANCE__( this );
		this.configuration = new SpongePlsConfig( this );
	}

	@Override
	public void onEnable()
	{

		if( BungeeCord.getInstance().getConfig().isIpForward() ) {
			try {
				Field field = BungeeCord.getInstance().getConfig().getClass().getDeclaredField( "ipForward" );
				field.setAccessible( true );
				field.set( BungeeCord.getInstance().getConfig(), false );
				changedIpforwarding = true;
			} catch (NoSuchFieldException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}


		getProxy().getPluginManager().registerCommand( this, new SpongePlsCommand( this ) );
		getProxy().getPluginManager().registerListener( this, new PlayerListener( this ) );
	}

	@Override
	public void onDisable()
	{
		set__INSTANCE__( null );

		if( changedIpforwarding ) {
			try {
				Field field = BungeeCord.getInstance().getConfig().getClass().getDeclaredField( "ipForward" );
				field.setAccessible( true );
				field.set( BungeeCord.getInstance().getConfig(), true );
			} catch (NoSuchFieldException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}

	}

	public Configuration getConfig() {
		if( getConfiguration() == null ) return null;
		return getConfiguration().getConfig();
	}

	public void saveConfig() {
		if( getConfiguration() == null ) return;
		getConfiguration().saveConfig();
	}

}
