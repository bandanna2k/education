package education.yaml;

import org.junit.jupiter.api.Test;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.representer.Representer;

import java.io.InputStreamReader;
import java.util.List;
import java.util.Objects;

public class TestSnakeYaml {
    @Test
    public void shouldParseYamlFileWithMinimumClass() {

        Representer representer = new Representer(new DumperOptions());
        representer.getPropertyUtils().setSkipMissingProperties(true);

        Yaml yaml = new Yaml(new Constructor(MyYaml.class, new LoaderOptions()), representer);
        yaml.parse(new InputStreamReader(TestSnakeYaml.class.getResourceAsStream("/example.yaml")));

        MyYaml myYaml = yaml.load(new InputStreamReader(TestSnakeYaml.class.getResourceAsStream("/example.yaml")));
        System.out.println(myYaml);
    }

    public static final class MyYaml {
        public Directory directory;

        @Override
        public String toString() {
            return "MyYaml[" +
                    "directory=" + directory + ']';
        }
    }

    public static final class Directory {
        public List<Account> accounts;

        @Override
        public String toString() {
            return "Directory[" +
                    "accounts=" + accounts + ']';
        }
    }

    public static final class Account {
        public String name;

        @Override
        public String toString() {
            return "Account[" +
                    "name=" + name + ']';
        }
    }
}