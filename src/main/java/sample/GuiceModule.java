package sample;

import java.io.IOException;
import java.util.Properties;

import com.google.common.base.Optional;
import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GuiceModule extends AbstractModule {

	private static final String APPLICATION_PROPERTIES = "application.properties";

	private final Optional<String> env;

	public GuiceModule() {
		this.env = Optional.fromNullable(System.getProperty("env"));
	}

	@Override
	protected void configure() {
		//log.info("Starting Guice configuration");
		//Names.bindProperties(binder(), loadApplicationProperties());
	}

	/**
	 * Load the application properties. Also seeks for env VM argument and in case
	 * it is found loads a secondary properties file into the system. Note that if
	 * there is a duplicated property on both files, the last loaded one will
	 * replace the first.
	 * 
	 * @return loaded properties
	 */
	private Properties loadApplicationProperties() {
		Properties props = new Properties();
		log.info("Loading default application properties - " + APPLICATION_PROPERTIES);
		loadPropertiesFile(APPLICATION_PROPERTIES, props);
		if (env.isPresent()) {
			String envPropertiesFile = String.format("application_%s.properties", env.get());
			log.info("Loading environment specific application properties - " + envPropertiesFile);
			loadPropertiesFile(envPropertiesFile, props);
		}
		return props;
	}

	private void loadPropertiesFile(String file, Properties props) {
		try {
			props.load(this.getClass().getClassLoader().getResourceAsStream(file));
		} catch (IOException e) {
			throw new RuntimeException("Unable to load " + file, e);
		}
	}

}
