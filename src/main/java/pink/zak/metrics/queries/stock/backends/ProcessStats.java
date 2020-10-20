package pink.zak.metrics.queries.stock.backends;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class ProcessStats {
    private final Runtime runtime;
    private String identifier;

    public ProcessStats() {
        this.runtime = Runtime.getRuntime();
        try {
            this.identifier = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            this.identifier = "Unknown Identifier";
        }
    }

    public ProcessStats(String identifier) {
        this.runtime = Runtime.getRuntime();
        this.identifier = identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return this.identifier;
    }

    public long getHeapSize() {
        return this.runtime.totalMemory();
    }

    public long getHeapUsage() {
        return this.runtime.totalMemory() - this.runtime.freeMemory();
    }
}
