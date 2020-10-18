package pink.zak.metrics;

import pink.zak.metrics.queries.stock.SystemQuery;
import pink.zak.metrics.queries.stock.backends.BukkitServerStats;
import pink.zak.metrics.queries.stock.backends.ProcessStats;
import pink.zak.metrics.queries.stock.minecraft.BukkitServerQuery;

public class Fake {

    public static void main(String[] args) {
        Metrics metrics = new Metrics(new Metrics.Config("http://localhost:9999", "Your-Access-Token-To-Write".toCharArray(), "test-org", "test-data", 5));

        // Here's an example of how you can push stats to the metric logger.
        metrics.<ProcessStats>log(query -> query
                .primary(new ProcessStats())
                .push(SystemQuery.RAM_USAGE)
        );

        // Here's an example for a minecraft server
        metrics.<BukkitServerStats>log(query -> query
                .primary(new BukkitServerStats())
                .push(BukkitServerQuery.ALL)
        );
    }
}
