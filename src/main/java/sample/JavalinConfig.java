package sample;

import javax.inject.Inject;
import javax.inject.Named;

import com.google.inject.Singleton;

import io.javalin.Javalin;
import sample.controller.UserController;

@Singleton
public class JavalinConfig {
	
	@Inject
	@Named("javalin.port")
	private String javalinPort; 
	
	@Inject
	private UserController userController;
	
	public void boot() {
		Javalin app = Javalin.create().start(Integer.parseInt(javalinPort));
		routes(app);
	}
	
	private void routes(Javalin app) {
		app.post("/user/", userController::add);
		app.get("/user/:username", userController::findByUsername);
		app.delete("/user/:username", userController::remove);
	}
}
