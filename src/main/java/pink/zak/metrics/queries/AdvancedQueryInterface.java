package pink.zak.metrics.queries;

import com.influxdb.client.write.Point;
import pink.zak.metrics.service.TriFunction;

import java.util.function.BiFunction;

public interface AdvancedQueryInterface<T, K> {

    TriFunction<T, K, Point, Point> tag();

    TriFunction<T, K, Point, Point> get();

    String measurement();
}
