package sample.controller;

import com.google.common.base.Optional;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import io.javalin.http.Context;
import sample.model.User;
import sample.service.UserService;

@Singleton
public class UserController {
	
	private final UserService userService;
	
	@Inject
	public UserController(UserService userService) {
		super();
		this.userService = userService;
	}
	
	public void add(Context ctx) {
		User user = ctx.bodyAsClass(User.class);
		userService.add(user);
		ctx.status(201);
	}
	
	public void remove(Context ctx) {
		Optional<User> user = userService.findByUsername(ctx.pathParam("username"));
		if(user.isPresent()) {
			userService.remove(user.get());
			ctx.status(202);
		} else {
			ctx.status(404);
		}
	}
	
	public void findByUsername(Context ctx) {
		Optional<User> user = userService.findByUsername(ctx.pathParam("username"));
		if(user.isPresent()) {
			ctx.json(user.get());
		} else {
			ctx.status(404);
		}
	}
}
