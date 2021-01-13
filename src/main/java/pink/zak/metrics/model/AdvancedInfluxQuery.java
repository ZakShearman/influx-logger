package pink.zak.metrics.model;

import com.google.common.collect.Maps;
import com.influxdb.client.write.Point;
import org.jetbrains.annotations.NotNull;
import pink.zak.metrics.Metrics;
import pink.zak.metrics.queries.AdvancedQueryInterface;
import pink.zak.metrics.queries.QueryInterface;
import pink.zak.metrics.queries.stock.SystemQuery;

import java.util.Map;

public class AdvancedInfluxQuery<T, K> {
    private T primary;
    private Point result;

    /**
     * Creates a new query with a specific type
     * @param primary The type that the data will be grabbed from
     * @return The influx query to add push points to
     */
    public AdvancedInfluxQuery<T, K> primary(T primary) {
        this.primary = primary;
        return this;
    }

    /**
     * Adds data to the point, see {@link SystemQuery} for examples
     * @param query the query type, normally made with an enum extending QueryInterface.
     * @return The modified InfluxQuery.
     */
    public AdvancedInfluxQuery<T, K> push(AdvancedQueryInterface<T, K> query, K key) {
        return this.push(query, key, Maps.newHashMap());
    }

    /**
     * Adds data to the point, see {@link SystemQuery} for examples
     * @param query the query type, normally made with an enum extending QueryInterface.
     * @param tags Any additional tags to add to the data point
     * @return The modified InfluxQuery.
     */
    public AdvancedInfluxQuery<T, K> push(AdvancedQueryInterface<T, K> query, K key, @NotNull Map<String, String> tags) {
        if (this.result == null) {
            this.result = Point.measurement(query.measurement());
        }
        query.tag().apply(this.primary, key, this.result);
        query.get().apply(this.primary, key, this.result);
        this.result.addTags(tags);
        return this;
    }

    public void executeAndTerminate(Metrics metrics) {
        metrics.queueResult(this.result);
        this.primary = null;
        this.result = null;
    }
}
