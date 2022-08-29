package ru.otus.configuration;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.WriteConcern;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import de.flapdoodle.embed.mongo.config.MongodConfig;
import io.mongock.driver.mongodb.reactive.driver.MongoReactiveDriver;
import io.mongock.runner.springboot.MongockSpringboot;
import io.mongock.runner.springboot.base.MongockInitializingBeanRunner;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

@Configuration
public class MongoDBConfiguration {

    @Bean
    public MongockInitializingBeanRunner mongockInitializingBeanRunner(MongoClient reactiveMongoClient, ApplicationContext context) {
        MongoReactiveDriver driver = MongoReactiveDriver.withDefaultLock(reactiveMongoClient, "test");
        driver.setWriteConcern(WriteConcern.MAJORITY.withJournal(false));
        return MongockSpringboot.builder()
                .setDriver(driver)
                .addMigrationScanPackage("ru.otus.mongock.changelog")
                .setSpringContext(context)
                .setTransactionEnabled(false)
                .buildInitializingBeanRunner();
    }

    @Bean
    MongoClient mongoClient(MongodConfig embeddedMongoConfiguration) {
        CodecRegistry codecRegistry = fromRegistries(
                MongoClientSettings.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));

        return MongoClients.create(MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(String.format("mongodb://localhost:%s/", embeddedMongoConfiguration.net().getPort())))
                .codecRegistry(codecRegistry)
                .build());
    }
}
