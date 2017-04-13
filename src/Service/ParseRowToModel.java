package Service;

import java.sql.ResultSet;

public interface ParseRowToModel<T> {
    T parseRowToModel(ResultSet resultSet) throws Exception;
}