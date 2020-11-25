package pink.zak.metrics;

import com.google.common.collect.Lists;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.WriteApi;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import pink.zak.metrics.model.InfluxQuery;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.UnaryOperator;

public class Metrics {
    private final Config config;
    private final InfluxDBClient databaseClient;
    private final ScheduledExecutorService scheduledExecutorService;

    public Metrics(Config config, ScheduledExecutorService scheduledExecutor) {
        this.config = config;

        this.databaseClient = this.config.getDatabaseClient();
        this.scheduledExecutorService = scheduledExecutor;
        this.startScheduledCleanup();
    }

    public Metrics(Config config) {
        this(config, Executors.newSingleThreadScheduledExecutor());
    }

    /*
     * Naming of the following fields is incorrect according to java naming conventions.
     * This style is being skipped so my eyes don't bleed - Hyfe 2020
     */
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final List<Point> pendingResults = Lists.newCopyOnWriteArrayList();

    /**
     * Start the scheduler which writes and flushes the pending results.
     */
    public void startScheduledCleanup() {
        WriteApi writeApi = this.databaseClient.getWriteApi();
        this.scheduledExecutorService.scheduleAtFixedRate(() -> {
            writeApi.writePoints(this.pendingResults);
            this.pendingResults.clear();
        }, this.config.getLatency(), this.config.getLatency(), TimeUnit.SECONDS);
    }

    /**
     * This is the primary logging method. It creates a {@link InfluxQuery}
     * and thereafter it executes and terminates it.
     * <p>
     * Executing entails pushing it onto the pending results {@link java.util.concurrent.CopyOnWriteArrayList}.
     *
     * @param queryFunction the {@link InfluxQuery} modifier
     * @param <T>           the type you're modifying
     */
    public <T> void log(UnaryOperator<InfluxQuery<T>> queryFunction) {
        this.executorService.execute(() -> {
            queryFunction.apply(new InfluxQuery<>()).executeAndTerminate(this);
        });
    }

    public void queueResult(Point result) {
        this.pendingResults.add(result.time(System.currentTimeMillis(), WritePrecision.MS));
    }

    public InfluxDBClient getDbClient() {
        return this.databaseClient;
    }

    public static class Config {
        private final String url;
        private final char[] token;
        private final String organization;
        private final String bucket;
        private final long latency; // in seconds

        public Config(String url, char[] token, String organization, String bucket, long latency) {
            this.url = url;
            this.token = token;
            this.organization = organization;
            this.bucket = bucket;
            this.latency = latency;
        }

        public InfluxDBClient getDatabaseClient() {
            return InfluxDBClientFactory.create(this.url, this.token, this.organization, this.bucket);
        }

        public long getLatency() {
            return this.latency;
        }
    }
}