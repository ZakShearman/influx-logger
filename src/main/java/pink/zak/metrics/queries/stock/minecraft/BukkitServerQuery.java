package pink.zak.metrics.queries.stock.minecraft;

import com.influxdb.client.write.Point;
import pink.zak.metrics.queries.QueryInterface;
import pink.zak.metrics.queries.stock.backends.BukkitServerStats;

import java.util.function.BiFunction;

public enum BukkitServerQuery implements QueryInterface<BukkitServerStats> {

    RAM_USAGE((stats, point) -> {
        return point.addField("memory-usage", stats.getCurrentRamUsage());
    }),
    ONLINE_PLAYERS(((stats, point) -> {
        return point.addField("online-players", stats.getOnlinePlayers());
    })),
    ALL((profile, point) -> {
        for (BukkitServerQuery query : values()) {
            if (!query.toString().equals("ALL")) {
                query.get().apply(profile, point);
            }
        }
        return point;
    });;

    private final BiFunction<BukkitServerStats, Point, Point> computation;

    BukkitServerQuery(BiFunction<BukkitServerStats, Point, Point> computation) {
        this.computation = computation;
    }

    @Override
    public BiFunction<BukkitServerStats, Point, Point> tag() {
        return (process, point) -> point.addTag("identifier", process.getIdentifier());
    }

    @Override
    public BiFunction<BukkitServerStats, Point, Point> get() {
        return this.computation;
    }

    @Override
    public String measurement() {
        return "system-metrics";
    }
}
