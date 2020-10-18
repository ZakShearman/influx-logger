package pink.zak.logger;

import pink.zak.logger.queries.stock.SystemQuery;
import pink.zak.logger.queries.stock.backends.BukkitServerStats;
import pink.zak.logger.queries.stock.backends.ProcessStats;
import pink.zak.logger.queries.stock.minecraft.BukkitServerQuery;

public class Fake {

    public static void main(String[] args) {
        new Logger(new Logger.Config("http://localhost:9999", "Your-Access-Token-To-Write".toCharArray(), "test-org", "test-data", 5));

        // Here's an example of how you can push stats to the logger.
        Logger.<ProcessStats>log(query -> query
                .primary(new ProcessStats())
                .push(SystemQuery.RAM_USAGE)
        );

        // Here's an example for a minecraft server
        Logger.<BukkitServerStats>log(query -> query
                .primary(new BukkitServerStats())
                .push(BukkitServerQuery.ALL)
        );
    }
}
