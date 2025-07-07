package ru.job4j.grabber;

import org.apache.log4j.Logger;
import ru.job4j.grabber.model.Post;
import ru.job4j.grabber.service.*;
import ru.job4j.grabber.stores.JdbcStore;
import ru.job4j.grabber.stores.Store;
import ru.job4j.grabber.utils.HabrCareerDateTimeParser;

import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {
    private static final Logger LOG = Logger.getLogger(Main.class);

    public static void main(String[] args) throws InterruptedException {
        var config = new Config();
        config.load("application.properties");
        try (var connection = DriverManager.getConnection(config.get("db.url"),
                config.get("db.username"),
                config.get("db.password"))) {
            Store store = new JdbcStore(connection);

            var post = new Post();
            post.setTitle("Super Java Job");
            post.setLink("http://example.com/job");
            post.setDescription("Best job for Java devs");
            post.setTime(System.currentTimeMillis());
            store.save(post);

            var dateTimeParser = new HabrCareerDateTimeParser();
            var parser = new HabrCareerParse(dateTimeParser);
            parser.fetch().forEach(
                    store::save
            );

            var scheduler = new SchedulerManager();
            scheduler.init();
            scheduler.load(
                    Integer.parseInt(config.get("rabbit.interval")),
                    SuperJobGrab.class,
                    store
            );
            Thread.sleep(10000);
            new Web(store).start(Integer.parseInt(config.get("server.port")));
        } catch (SQLException e) {
            LOG.error("When create a connection", e);
        }
    }
}