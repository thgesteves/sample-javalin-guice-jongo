package sample.service;

import javax.inject.Inject;

import org.bson.types.ObjectId;
import org.jongo.Jongo;
import org.jongo.MongoCollection;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

import lombok.NonNull;
import sample.model.User;

public class UserService {

	private final Jongo jongo;

	@Inject
	public UserService(Jongo jongo) {
		Preconditions.checkNotNull(jongo);
		this.jongo = jongo;
	}

	private MongoCollection users() {
		return jongo.getCollection("users");
	}

	public User add(User user) {
		Preconditions.checkNotNull(user);
		users().save(user);
		return user;
	}
	
	public void remove(@NonNull User user) {
		Preconditions.checkNotNull(user.getId());
		users().remove(new ObjectId(user.getId()));
	}

	public Optional<User> findByUsername(@NonNull String username) {
		Preconditions.checkNotNull(username);
		User user = users().findOne("{username: #}", username).as(User.class);
		return Optional.fromNullable(user);
	}
}
