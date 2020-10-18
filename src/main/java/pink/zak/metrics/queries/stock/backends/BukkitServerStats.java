package pink.zak.metrics.queries.stock.backends;

import org.bukkit.Bukkit;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class BukkitServerStats {
    private final Runtime runtime;
    private final String serverIdentifier;

    public BukkitServerStats() {
        this.serverIdentifier = this.defaultIdentifier();
        this.runtime = Runtime.getRuntime();
    }

    public BukkitServerStats(String serverIdentifier) {
        this.serverIdentifier = serverIdentifier;
        this.runtime = Runtime.getRuntime();
    }

    public String getIdentifier() {
        return this.serverIdentifier;
    }

    public int getOnlinePlayers() {
        return Bukkit.getServer().getOnlinePlayers().size();
    }

    public long getCurrentRamUsage() {
        return this.runtime.totalMemory() - this.runtime.freeMemory();
    }

    private String defaultIdentifier() {
        try {
            return InetAddress.getLocalHost().getHostName() + ":" + Bukkit.getServer().getPort();
        } catch (UnknownHostException e) {
            return Bukkit.getServer().getIp() + ":" + Bukkit.getServer().getPort();
        }
    }
}
