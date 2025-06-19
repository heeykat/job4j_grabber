package ru.job4j.grabber;

import ru.job4j.grabber.model.Post;
import ru.job4j.grabber.service.Config;
import ru.job4j.grabber.service.SchedulerManager;
import ru.job4j.grabber.service.SuperJobGrab;
import ru.job4j.grabber.stores.JdbcStore;

import java.sql.DriverManager;
import java.sql.SQLException;
import org.apache.log4j.Logger;

public class Main {
    private static final Logger LOG = Logger.getLogger(Main.class);

    public static void main(String[] args) throws InterruptedException {
        var config = new Config();
        config.load("application.properties");
        try (var connection = DriverManager.getConnection(
                config.get("db.url"),
                config.get("db.username"),
                config.get("db.password"));
        var scheduler = new SchedulerManager()) {
            var store = new JdbcStore(connection);
            var post = new Post();
            post.setTitle("Super Java Job");
            post.setLink("http://example.com/job");
            post.setDescription("Best job for Java devs");
            post.setTime(System.currentTimeMillis());
            store.save(post);
            scheduler.init();
            scheduler.load(
                    Integer.parseInt(config.get("rabbit.interval")),
                    SuperJobGrab.class,
                    store);
            Thread.sleep(10000);
        } catch (SQLException e) {
            LOG.error("When create a connection", e);
        }
    }
}
