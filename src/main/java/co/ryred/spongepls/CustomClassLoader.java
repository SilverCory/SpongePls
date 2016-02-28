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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URLConnection;

/**
 * @author Cory Redmond
 *         Created by acech_000 on 13/11/2015.
 */
public class CustomClassLoader extends ClassLoader
{

	private final byte[] classData;

	public CustomClassLoader( ClassLoader classLoader, byte[] classData )
	{
		super( classLoader );
		this.classData = classData;
	}

	public Class loadClass( String name ) throws ClassNotFoundException
	{
		if ( !"net.md_5.bungee.netty.HandlerBoss".equals( name ) ) { return getParent().loadClass( name ); }

		try {

			//SpongePlsPlugin.get__INSTANCE__().getLogger().info( "[D] | Class: " + tempClass.toURI().toURL() );

			URLConnection connection = new File( "./net/md_5/bungee/netty/HandlerBoss.class" ).toURI().toURL().openConnection();
			InputStream input = connection.getInputStream();

			ByteArrayOutputStream buffer = new ByteArrayOutputStream();
			int data = input.read();

			while ( data != -1 ) {
				buffer.write( data );
				data = input.read();
			}

			input.close();

			byte[] classData = buffer.toByteArray();

			//defineClass(String name, byte[] b, int off, int len)
			//return getParent().defineClass("net.md_5.bungee.netty.HandlerBoss", classData, 0, classData.length);

			Method defineClassMethod = ClassLoader.class.getDeclaredMethod( "defineClass", String.class, byte[].class, int.class, int.class );
			defineClassMethod.setAccessible( true );
			return (Class) defineClassMethod.invoke( ClassLoader.getSystemClassLoader(), "net.md_5.bungee.netty.HandlerBoss", classData, 0, classData.length );

		} catch ( Exception e ) {
			e.printStackTrace();
		}

		return getParent().loadClass( name );

	}

}
