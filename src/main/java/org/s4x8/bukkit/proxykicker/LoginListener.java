package org.s4x8.bukkit.proxykicker;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import java.net.InetAddress;
import java.util.logging.Level;

public class LoginListener implements Listener {

    private ProxyKicker plugin;

    public LoginListener(ProxyKicker plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();

        if (player.hasPermission("proxykicker.bypass")) {
            return; // This player is exempt of checking
        }

        InetAddress playerAddress = event.getAddress();
        if (plugin.getTorDatabase().isTorIp(playerAddress)) {
            plugin.getLogger().log(Level.INFO, "Kicking {0} because his/her IP is in the Tor database", player.getName());
            event.disallow(PlayerLoginEvent.Result.KICK_OTHER, "Your IP is on the Tor database");
        }

        (new PortChecker(plugin, event)).start();
    }
}
