package sample;

import java.io.Closeable;
import java.io.IOException;
import java.util.Properties;

import javax.inject.Named;

import org.jongo.Jongo;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Names;
import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AppModule extends AbstractModule implements Closeable {
	
	private static final String DEFAULT_APP_PROPERTIES = "app.properties";
	
	private MongoClient mongoClient;

	@Override
	protected void configure() {
		log.info("Starting Guice");
		Names.bindProperties(binder(), loadAppProperties());
	}

	private Properties loadAppProperties() {
		Properties props = new Properties();
		String config = System.getProperty("config");
		if (config == null) {
			config = DEFAULT_APP_PROPERTIES;
		}
		try {
			props.load(this.getClass().getClassLoader().getResourceAsStream(config));
		} catch (IOException e) {
			throw new RuntimeException("Unable to load " + config, e);
		}
		return props;
	}

	@SuppressWarnings("deprecation")
	@Provides
	private Jongo provideJongo(@NonNull @Named("mongo.uri") String uri, @NonNull @Named("mongo.db") String dbName) {
		try {
			mongoClient = new MongoClient(new MongoClientURI(uri));
			// https://github.com/bguerout/jongo/issues/254
			DB db = mongoClient.getDB(dbName);
			return new Jongo(db);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void close() {
		try {
			log.debug("Closing mongodb connection");
			mongoClient.close();
		} catch (Throwable e) {
			log.error("Error closing AppModule class", e);
		}
	}

	@Override
	public void finalize() {
		this.close();
	}
}
