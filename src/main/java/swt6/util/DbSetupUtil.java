package swt6.util;

import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.destination.DriverManagerDestination;
import com.ninja_squad.dbsetup.operation.Operation;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DbSetupUtil {
    public static DbSetup getDbSetup(Operation operation) throws IOException {
        Properties prop = new Properties();
        InputStream input = null;
        try {
            input = DbSetupUtil.class.getClassLoader().getResourceAsStream("dbsetup.properties");
            // load properties file
            prop.load(input);
            return new DbSetup(new DriverManagerDestination(prop.getProperty("connectionUrl"),
                    prop.getProperty("user"), prop.getProperty("password")), operation);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static DbSetup getTestDbSetup(Operation operation) throws IOException {
        Properties prop = new Properties();
        InputStream input = null;
        try {
            input = DbSetupUtil.class.getClassLoader().getResourceAsStream("dbsetup.test.properties");
            // load properties file
            prop.load(input);
            return new DbSetup(new DriverManagerDestination(prop.getProperty("connectionUrl"),
                    prop.getProperty("user"), prop.getProperty("password")), operation);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
