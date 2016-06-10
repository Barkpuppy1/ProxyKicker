package org.s4x8.bukkit.proxykicker;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.Date;
import java.util.logging.Logger;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.logging.Level;
import org.bukkit.plugin.java.JavaPlugin;

public class ProxyKicker extends JavaPlugin {

    private ArrayList<Integer> proxyPorts;
    private TorDatabase torDatabase;
    private boolean disableTorCache;
    public static ProxyKicker plugin;

    @Override
    public void onLoad() {
        ProxyKicker.plugin = this;
    }

    private void loadPorts() {
        proxyPorts = new ArrayList<Integer>();

        Iterator<String> it = plugin.getConfig().getStringList("proxy-ports").iterator();
        while (it.hasNext()) {
            int port = -1;
            String portString = it.next();

            try {
                port = Integer.parseInt(portString);
            } catch (NumberFormatException ex) {
            }

            if (port >= 1 && port <= 65535) {
                proxyPorts.add(port);
            } else {
                plugin.getLogger().log(Level.WARNING, "{0} is not a valid port!", portString);
            }
        }
    }

    private void initializeTor() {
        torDatabase = new TorDatabase();
        disableTorCache = false;

        boolean update = false;
        long updateInterval = getConfig().getLong("tor-update-interval", 604800000L);

        if (updateInterval <= 0L) {
            update = true;
            disableTorCache = true;
        } else {
            long lastUpdate = getConfig().getLong("tor-update-date", -1L);
            if (lastUpdate < 0L) {
                update = true;
            }
            if ((new Date()).getTime() - lastUpdate >= updateInterval) {
                update = true;
            }
        }

        if (update) {
            downloadTor();
            saveTor();
        } else {
            loadTor();
        }
    }

    private void downloadTor() {
        Logger logger = getLogger();
        logger.info("Downloading Tor exit node databases...");

        Iterator<String> it = getConfig().getStringList("tor-list").iterator();
        while (it.hasNext()) {
            String stringUrl = it.next();
            URL url = null;
            try {
                url = new URL(stringUrl);
            } catch (MalformedURLException exception) {
                logger.log(Level.WARNING, "Invalid Tor list URL: {0}", stringUrl);
            }

            if (url != null) {
                InputStream loadStream = null;
                try {
                    loadStream = url.openStream();
                    torDatabase.loadText(loadStream);
                } catch (IOException exception) {
                    logger.log(Level.WARNING, "An ocurred while trying to download Tor list: {0}", url);
                } catch (ParseException exception) {
                    logger.log(Level.WARNING, "Unable to parse Tor list: {0}", url);
                } finally {
                    try {
                        if (loadStream != null) {
                            loadStream.close();
                        }
                    } catch (Exception e) {
                    }
                }
            }
        }
        logger.info("Finished downloading Tor databases");
    }

    private void loadTor() {
        Logger logger = getLogger();
        logger.info("Loading Tor exit nodes from cache...");

        File torCacheFile = new File(getDataFolder(), "tor.db");
        FileInputStream loadStream = null;
        try {
            loadStream = new FileInputStream(torCacheFile);
            torDatabase.loadBinary(loadStream);
            logger.info("Tor database loaded successfully!");
        } catch (IOException exception) {
            logger.warning("Unable to load Tor cache file");
            downloadTor();
            saveTor();
        } catch (ParseException exception) {
            logger.warning("Tor file is corrupt");
            downloadTor();
            saveTor();
        }
        try {
            if (loadStream != null) {
                loadStream.close();
            }
        } catch (Exception exception) {
        }
    }

    private void saveTor() {
        Logger logger = getLogger();

        if (disableTorCache) {
            logger.info("Tor database cache disabled");
            return;
        }
        File torCacheFile = new File(getDataFolder(), "tor.db");
        FileOutputStream saveStream = null;
        try {
            saveStream = new FileOutputStream(torCacheFile);
            torDatabase.saveBinary(saveStream);
            getConfig().set("tor-update-date", (new Date()).getTime());
            saveConfig();
            logger.info("Tor database saved successfully!");
        } catch (IOException exception) {
            logger.warning("Unable to save Tor cache");
        }
        try {
            if (saveStream != null) {
                saveStream.close();
            }
        } catch (Exception exception) {
        }
    }

    private void initListener() {
        getServer().getPluginManager().registerEvents(new LoginListener(this), this);
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();

        loadPorts();
        initializeTor();
        initListener();
    }

    public TorDatabase getTorDatabase() {
        return torDatabase;
    }

    public ArrayList<Integer> getProxyPorts() {
        return proxyPorts;
    }
}
