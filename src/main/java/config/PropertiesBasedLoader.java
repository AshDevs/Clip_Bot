package config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesBasedLoader {
  public static Config load_from_properties(String filename) {
    Config config = new Config();

    Properties properties = new Properties();

    try (InputStream inputStream = PropertiesBasedLoader.class
        .getClassLoader()
        .getResourceAsStream(filename)) {
      assert inputStream != null;
      properties.load(inputStream);

      config.bot_token = properties.getProperty("bot_token");
    } catch (IOException e) {
      e.printStackTrace();
    }

    return config;
  }
}
