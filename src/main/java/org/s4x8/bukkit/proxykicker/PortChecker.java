
package org.s4x8.bukkit.proxykicker;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerLoginEvent;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Iterator;

public class PortChecker extends Thread {
	private ProxyKicker plugin;
	private PlayerLoginEvent event;
    
	public PortChecker(ProxyKicker plugin, PlayerLoginEvent event) {
		this.plugin = plugin;
		this.event = event;
	}

	public void run() {
		Player player = event.getPlayer();
		InetAddress ip = event.getAddress();

		Iterator<Integer> it = plugin.getProxyPorts().iterator();

		while (it.hasNext()) {
			int port = it.next();
			
    			if (hasPortOpen(ip, port)) {
				plugin.getLogger().info("Kicking " + player.getName() + " because he/she has port " + port + " open");
				Kicker kicker = new Kicker(player, "You have been kicked because you have port " + port + " open");
				plugin.getServer().getScheduler().runTask(plugin, kicker);
    				return; //it is not necessary to continue looping
    			};
    		};
    	};
    
	private boolean hasPortOpen(InetAddress ip, int port) {
		Socket socket = new Socket();
		InetSocketAddress socketAddress = new InetSocketAddress(ip, port);
        	try {
			socket.connect(socketAddress, 5000);
	 		socket.close();
			return true;
		} catch (Exception e) { };

		return false;
	};
};
