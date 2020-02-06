package sample.tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.UnknownHostException;

import org.jongo.Jongo;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;

import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.exceptions.DistributionException;
import de.flapdoodle.embed.process.runtime.Network;
import sample.model.User;
import sample.service.UserService;

public class UserServiceTest {
	private static final MongodStarter starter = MongodStarter.getDefaultInstance();

	private static MongodExecutable mongodExe;
	private static MongodProcess mongod;

	private static MongoClient mongo;

	private static Jongo jongo;

	private static UserService userService;
	private static User user;

	@SuppressWarnings("deprecation")
	@BeforeClass
	public static void setUp() throws DistributionException, UnknownHostException, IOException {

		mongodExe = starter.prepare(new MongodConfigBuilder().version(Version.Main.PRODUCTION)
				.net(new Net("localhost", 12345, Network.localhostIsIPv6())).build());
		mongod = mongodExe.start();

		mongo = new MongoClient("localhost", 12345);

		jongo = new Jongo(mongo.getDB("testdb"));
		
		userService = new UserService(jongo);
		user = new User(null, "bart", "Bart Simpson");
	}
	
	@AfterClass
	public static void tearDown() throws Exception {
		mongod.stop();
		mongodExe.stop();
	}

	public Mongo getMongo() {
		return mongo;
	}
	
	@Test
	public void testAdd() {
		userService.add(user);
		assertTrue(userService.findByUsername(user.getUsername()).isPresent());
	}

	@Test
	public void testRemove() {
		userService.remove(user);
		assertFalse(userService.findByUsername(user.getUsername()).isPresent());
	}
}
