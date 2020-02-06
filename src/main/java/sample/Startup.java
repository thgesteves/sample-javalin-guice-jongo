package sample;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class Startup {

	public static void main(String[] args) {
		Injector injector = Guice.createInjector(new AppModule());
		injector.getInstance(JavalinConfig.class).boot();
	}
}
