package pink.zak.metrics.queries.stock;

import com.influxdb.client.write.Point;
import pink.zak.metrics.queries.QueryInterface;
import pink.zak.metrics.queries.stock.backends.ProcessStats;

import java.util.function.BiFunction;

public enum SystemQuery implements QueryInterface<ProcessStats> {

    HEAP_SIZE((process, point) -> {
        return point.addField("heap-size", process.getHeapSize());
    }),
    HEAP_USAGE((process, point) -> {
        return point.addField("heap-usage", process.getHeapUsage());
    }),
    ALL((profile, point) -> {
        for (SystemQuery query : values()) {
            if (!query.toString().equals("ALL")) {
                query.get().apply(profile, point);
            }
        }
        return point;
    });

    private final BiFunction<ProcessStats, Point, Point> computation;

    SystemQuery(BiFunction<ProcessStats, Point, Point> computation) {
        this.computation = computation;
    }

    @Override
    public BiFunction<ProcessStats, Point, Point> tag() {
        return (process, point) -> point.addTag("identifier", process.getIdentifier());
    }

    @Override
    public BiFunction<ProcessStats, Point, Point> get() {
        return this.computation;
    }

    @Override
    public String measurement() {
        return "system-metrics";
    }
}