package pink.zak.logger.model;

import com.influxdb.client.write.Point;
import pink.zak.logger.Logger;
import pink.zak.logger.queries.QueryInterface;
import pink.zak.logger.queries.stock.SystemQuery;

public class LoggerQuery<T> {
    private T primary;
    private Point result;

    /**
     * Creates a new query with a specific type
     * @param primary The type that the data will be grabbed from
     * @return The logger query to add push points to
     */
    public LoggerQuery<T> primary(T primary) {
        this.primary = primary;
        return this;
    }

    /**
     * Adds data to the point, see {@link SystemQuery} for examples
     * @param query the query type, normally made with an enum extending QueryInterface.
     * @return The modified LoggerQuery.
     */
    public LoggerQuery<T> push(QueryInterface<T> query) {
        if (this.result == null) {
            this.result = Point.measurement(query.measurement());
        }
        query.tag().apply(this.primary, this.result);
        query.get().apply(this.primary, this.result);
        return this;
    }

    public void executeAndTerminate() {
        Logger.queueResult(this.result);
        this.primary = null;
        this.result = null;
    }
}
