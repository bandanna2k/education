package education.testContainers;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;

public class PropertyDao
{
    private final NamedParameterJdbcOperations jdbc;

    public PropertyDao(DataSource dataSource)
    {
        jdbc = new NamedParameterJdbcTemplate(dataSource);
    }

    public String getProperty(String key)
    {
        return jdbc.queryForObject("select `value` from `common`.`properties` where `key` = :key",
                new MapSqlParameterSource("key", key), String.class);
    }
}
