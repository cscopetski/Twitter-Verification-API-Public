package com.starfruitinteractive.StarfruitBackend;

import java.util.ArrayList;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import io.github.redouane59.twitter.TwitterClient;
import io.github.redouane59.twitter.dto.endpoints.AdditionalParameters;
import io.github.redouane59.twitter.dto.user.UserList;
import io.github.redouane59.twitter.signature.TwitterCredentials;

@SpringBootApplication
@RestController
public class StarfruitBackendApplication implements CommandLineRunner {

	Followers FollowersList = new Followers();

	TwitterClient twitterClient = new TwitterClient(TwitterCredentials.builder()
			.accessToken("SECRET")
			.accessTokenSecret("SECRET")
			.apiKey("SECRET")
			.apiSecretKey("SECRET")
			.build());

	public static void main(String[] args) {
		SpringApplication.run(StarfruitBackendApplication.class, args);
	}

	public void run(String... args) throws Exception {
		FollowersList.LoadFollowers(GetFollowers(1000)); // Change to obtain ALL followers
		System.out.println("Loaded current followers");
		Timer timer = new Timer();

		timer.schedule(new TimerTask() {
			public void run() {
				System.out.println("Updating follower list");
				FollowersList.UpdateFollowers(GetFollowers(30)); // Update to around 100-200 once we begin getting more
																	// followers coming
			}
		}, 0, 70 * 1000);
	}

	public List<String> GetFollowers(int followerCount) {
		UserList FollowerList = twitterClient.getFollowers("SECRET",
				AdditionalParameters.builder().recursiveCall(false).maxResults(followerCount).build());
		List<String> Followers = new ArrayList<String>();

		for (int i = 0; i < FollowerList.getData().size(); i++) {
			Followers.add(FollowerList.getData().get(i).getName());
		}

		return Followers;
	}

	@PostMapping("/api/Verification")
	public boolean IsVerified(@RequestBody String Username) {
		String UpdatedUsername = Username.replaceAll("^\"|\"$", "");
		return FollowersList.IsUserFollowing(UpdatedUsername);
	}
}