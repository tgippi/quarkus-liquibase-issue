package org.acme.quickstart;

import io.quarkus.test.junit.QuarkusTest;
import org.acme.quickstart.helper.LiquibaseCallback;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.inject.Inject;
import javax.sql.DataSource;

import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
@ExtendWith(LiquibaseCallback.class)
public class DemoTest {

    @Inject
    DataSource ds;

    @Test
    public void testTheTable() throws Exception {
        try (var con = ds.getConnection();
             var stmt = con.prepareStatement("SELECT t.ID FROM Test t");
             var resultSet = stmt.executeQuery()) {
            assertTrue(resultSet.next());
        }
    }

}
