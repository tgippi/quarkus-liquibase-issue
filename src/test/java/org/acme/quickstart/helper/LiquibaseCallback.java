package org.acme.quickstart.helper;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Properties;

public class LiquibaseCallback implements BeforeAllCallback {

    private static final String DEFAULT_CHANGELOG_PATH = "liquibase/changelog.xml";
    private String url;

    public LiquibaseCallback() {
        this(null);
    }

    public LiquibaseCallback(String url) {
        this.url = url;
    }

    @Override
    public void beforeAll(ExtensionContext extensionContext) throws Exception {
        try (Connection databaseConnection = getDatabaseConnection()) {
            Database database = DatabaseFactory.getInstance()
                    .findCorrectDatabaseImplementation(new JdbcConnection(databaseConnection));

            Liquibase liquibase = new Liquibase(DEFAULT_CHANGELOG_PATH, new ClassLoaderResourceAccessor(), database);
            liquibase.update("test");
        }
    }

    private Connection getDatabaseConnection() throws SQLException {
        String connectionString = Optional.ofNullable(url)
                .orElseGet(() -> {
                    try {
                        Properties prop = new Properties();
                        prop.load(this.getClass().getClassLoader().getResourceAsStream("application.properties"));
                        return prop.getProperty("quarkus.datasource.url");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });

        return DriverManager.getConnection(connectionString);
    }

    public String getUrl() {
        return url;
    }
}

