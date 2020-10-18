package pink.zak.logger.queries.stock;

import com.influxdb.client.write.Point;
import pink.zak.logger.queries.QueryInterface;
import pink.zak.logger.queries.stock.backends.ProcessStats;

import java.util.function.BiFunction;

public enum SystemQuery implements QueryInterface<ProcessStats> {

    RAM_USAGE((process, point) -> {
        return point.addField("memory-usage", process.getCurrentRamUsage());
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