package vistar.practice.demo;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class InstaHubApplication {

    private static void databaseUpdate() throws SQLException, LiquibaseException {
        try (
                Connection connection = DriverManager.getConnection(
                        "jdbc:postgresql://localhost:5432/postgres",
                        "postgres",
                        "postgres"
                )
        ) {
            Database database = DatabaseFactory.getInstance()
                    .findCorrectDatabaseImplementation(new JdbcConnection(connection));
            try (
                    Liquibase liquibase = new liquibase.Liquibase(
                            "config/liquibase/master.xml",
                            new ClassLoaderResourceAccessor(),
                            database
                    )
            ) {
                liquibase.update(new Contexts(), new LabelExpression());
            }
        } catch (SQLException | LiquibaseException ex) {
            throw ex;
        }
    }

    public static void main(String[] args) {

        try {
            databaseUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }

        final EntityManagerFactory entityManagerFactory =
                Persistence.createEntityManagerFactory("vistar.practice.demo.unit");
        final EntityManager entityManager = entityManagerFactory.createEntityManager();
    }
}
