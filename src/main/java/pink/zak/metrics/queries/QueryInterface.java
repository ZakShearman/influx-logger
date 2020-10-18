package pink.zak.metrics.queries;

import com.influxdb.client.write.Point;

import java.util.function.BiFunction;

public interface QueryInterface<T> {

    BiFunction<T, Point, Point> tag();

    BiFunction<T, Point, Point> get();

    String measurement();
}