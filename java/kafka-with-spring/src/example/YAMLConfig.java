package example;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties
public class YAMLConfig
{
    public String name;
    public String environment;
    public boolean enabled;
    public List<String> servers = new ArrayList<>();
}
