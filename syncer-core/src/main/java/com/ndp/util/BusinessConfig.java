package ndp.util;

import org.jboss.logging.Logger;

import java.io.InputStream;
import java.util.Properties;

public class BusinessConfig {
    private static final Logger logger = Logger.getLogger(BusinessConfig.class);
    private Properties properties = new Properties();

    public BusinessConfig() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("business.properties")) {
            if (input == null) {
                System.out.println("Sorry, unable to find business-config.properties");
                return;
            }
            properties.load(input);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public String getProperty(String company, String key) {
        logger.warn(company + "." + key);
        return properties.getProperty(company + "." + key);
    }
}