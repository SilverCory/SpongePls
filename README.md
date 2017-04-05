# **SpongePls**
**[IPForwarding for sponge via BungeeCord.](https://github.com/SpigotMC/BungeeCord/pull/1557).**

## **Downloading, Contributing, & Building**
[![Build Status](http://ci.ac3-servers.eu/job/SpongePls/badge/icon)](http://ci.ac3-servers.eu/job/SpongePls/)

Download the jar file from [my build server](http://ci.ac3-servers.eu/job/SpongePls/lastSuccessfulBuild/artifact/target/SpongePls.jar).
It requires Java 8 to run.

To contribute, it's in Java, it's got a formatting you can cope with, and it's on github, you should know what to do. I'm not too fussy.

To build, just use maven! `mvn clean install`.

**Well wasn't that simple!**

## **Usage**

I aimed to make this reasonably easy to use. And it's more than easy for someone to make a quick GUI for the java program, so if you can/want; please do!

1.  Drag and drop SpongePls.jar into your plugins folder.
2.  Restart BungeeCord.
3.  Edit the Configuration to your preferences.

The reload command is a little wobbly. So let's just not use that. ;)

**NOTE: Make sure the following configuration options are set**

* In the Sponge `global.conf`
  * `sponge.bungeecord.ipforwarding` is **true**
  * `sponge.modules.bungeecord` is **true**
* In `server.properties`
  * `online-mode` is **false**
* In the BungeeCord `config.yml`
  * `ip-forwarding` is **true**

## **Configuration**

The configuration supports regex as well as normal server names. If the server name doesn't match the server being joined then the extra data won't be sent. You don't want to send this data if the server is vanilla or SpigotMC.

## **Be kind**

I'm not releasing this on SpigotMC, because anyone with sense and a little knowledge of how BungeeCord's IP forwarding works, could easily copy my code and recreate one of my most downloaded plugins. The mentioned plugin took the most effort out of all of them. I will be using this method in that plugin to edit the data. I'd appreciate it greatly if you kept it for personal use if you do. If you really want one of the plugins and can't afford even half the price, I'll happily give it to you.
