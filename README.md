S4X8 ProxyKicker
================

What is this?
-------------

This is a plugin for [Bukkit] made to block connections from anonymous and publicproxies like Tor exit nodes or SOCKS proxies at the Minecraft server [Mine21].

Installation
------------

To install the plugin on your Bukkit server, you may either [download the pre-built version], or compile it yourself.

To compile it yourself, you need [Oracle Java Development Kit] v1.6 or newer, [Maven 2], and Git. When you have all of them downloaded and installed, you have to grab the source code using GIT and compile it:

	git clone http://github.com/socram8888/ProxyKicker
	cd ProxyKicker
	mvn clean package
	
Maven will automatically download all the dependencies. It may take more than five minutes if you have a slow internet connection, but usually it will take less than a minute.

The resulting compiled Java Archive file (.jar) ready to be used will be at /target. Just move it (or copy it) to Bukkit's plugin folder.

Configuration
-------------

The configuration for the plugin is stored in a [YAML]-formatted configuration file at /plugins/ProxyKicker/config.yml:
	
	# ProxyKicker configuration file
	
	# Expiration of proxy list cache, in milliseconds. By default, a week. Setting this value to zero or less will disable caching.
	tor-update-interval: 604800000
	# Unix time of last update. This value should not be modified manually. A value of zero or less forces an update
	tor-update-date: -1
	
	# Put on this list feeds for the Tor exit nodes.
	tor-list:
	- http://torstatus.blutmagie.de/ip_list_all.php/Tor_ip_list_ALL.csv
	
	# Every time an user connects, ProxyKicker will scan for these ports on his/her endpoint, and will kick him/her if one of the port is open
	proxy-ports:
	- 3127
	- 3128
	- 1080
	- 6666
	- 6668
	- 6667
	- 6673
	- 42321

This plugin will automatically download at startup a list Tor exit node database if tor.db does not exist or has expired.

You can skip checking for certain users by using PermissionsEx or another Bukkit Permission plugin and adding them the permission node "proxykicker.bypass".

License
-----

This software is released under the open-source MIT license:

>Copyright © 2013 Marcos Vives Del Sol
>
>Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
>
>The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
>
>THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

Changelog
---------
 * 08/IV/2013 1.0: First public release

About the author
----------------

My name is Marcos Vives Del Sol, aka "socram8888". I'm a 18-year-old Spanish guy who likes programming useless stuff that nobody uses. If you want to report a bug, ask for a new feature, or just say hello, you can contact me in my e-mail account <socram8888@gmail.com>.

This plugin is was based on a very mature version of proxykiller, programmed by jcarlosn (who also continued developing of the plugin - see [ZeroProxy]). This initially became as a fix for some race-condition bugs it had which made the server crash, and evolved into a full rewriting of the plugin. Right now, no line from the original code has been left.

  [Bukkit]: http://www.bukkit.org/
  [Maven 2]: http://maven.apache.org/
  [Mine21]: http://mine21.net/
  [Oracle Java Development Kit]: http://www.oracle.com/technetwork/java/javase/downloads/index.html
  [download the pre-built version]: https://www.dropbox.com/sh/lhn3xu1r5gdot37/Oeebo2IUDi
  [YAML]: http://www.yaml.org/
  [ZeroProxy]: http://dev.bukkit.org/server-mods/zeroproxy/
