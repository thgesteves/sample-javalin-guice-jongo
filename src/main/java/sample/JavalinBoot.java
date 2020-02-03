package sample;

import com.google.inject.Singleton;

import io.javalin.Javalin;

@Singleton
public class JavalinBoot {
	public void boot() {
		Javalin app = Javalin.create().start(9080);
		app.get("/", ctx -> ctx.result("Hello World"));
	}
}
