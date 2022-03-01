package DAO;

import Model.Post;
import Model.User;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.*;

import java.util.*;


public class UserDAO {
    public UserDAO(){}

	private static final String UserTable = "user-table";

	private static final String UsernameAttr = "Username";
	private static final String PasswordAttr = "Password";
	private static final String FirstNameAttr = "FirstName";
	private static final String LastNameAttr = "LastName";
	private static final String URLAttr = "ProfileURL";
	private static final String FollowersAttr = "Followers";
	private static final String FollowingAttr = "Following";

	private static AmazonDynamoDB amazonDynamoDB = AmazonDynamoDBClientBuilder
														.standard()
														.withRegion("us-east-2")
														.build();
	private static DynamoDB dynamoDB = new DynamoDB(amazonDynamoDB);

	public String changePicture(String username, String url){
		Table table = dynamoDB.getTable(UserTable);

		UpdateItemSpec updateItemSpec = new UpdateItemSpec().withPrimaryKey(UsernameAttr, username)
				.withUpdateExpression("set ProfileURL = :p")
				.withValueMap(new ValueMap().withString(":p", url))
				.withReturnValues(ReturnValue.UPDATED_NEW);

		try {
			UpdateItemOutcome outcome = table.updateItem(updateItemSpec);
			return "UpdateItem succeeded:\n" + outcome.getItem().toJSONPretty();
		}
		catch (Exception e) {
			return "Unable to update item: " + username;
		}
	}

	public List<User> getFollows(String username, String type){
		System.out.println("getFollows: GetFollows...");
		User user = getUser(username);
		System.out.println("getFollows: got user...");
		if(user == null){
			return null;
		}
		else{
			List<User> list = new ArrayList<>();
			switch (type){
				case "FOLLOWERS":
					for(String s : user.getFollowers()){
						System.out.println("getFollows: adding user " + s + "...");
						list.add(getUser(s));
					}
					break;
				case "FOLLOWING":
					for(String s : user.getFollowing()){
						System.out.println("getFollows: adding user " + s + "...");
						list.add(getUser(s));
					}
					break;
			}
			System.out.println("getFollows: returning list...");
			return list;
		}
	}

	public User getUser(String target){
		Table userTable = dynamoDB.getTable(UserTable);

		Item item = userTable.getItem(UsernameAttr, target);
		if(item == null){
			return null;
		}
		else{
			String username = item.getString(UsernameAttr);
			String password = item.getString(PasswordAttr);
			String firstName = item.getString(FirstNameAttr);
			String lastName = item.getString(LastNameAttr);
			String url = item.getString(URLAttr);

			System.out.println("getUser: getting " + username + "'s followers...");
			FollowsDAO followsDAO = new FollowsDAO();
			List<String> followers = new ArrayList<>();
			ResultsPage page = new ResultsPage();
			page.setLastKey(null);
			do{
				page = followsDAO.getFollowers(username, 1000, page.getLastKey());
				System.out.println("getUser: " + page.getValues());
				followers.addAll(page.getValues());
			}
			while (page.hasLastKey());

			List<String> following = followsDAO.getFollowing(username);

			System.out.println("getUser: returning user " + username);
			return new User(firstName, lastName, username, password, url, followers, following);
		}
	}

	public String register(User user){
		Table table = dynamoDB.getTable(UserTable);
		user.initializeFollows();

		Item item = new Item()
				.withPrimaryKey(UsernameAttr, user.getUsername())
				.withString(PasswordAttr, user.getPassword())
				.withString(FirstNameAttr, user.getFirstName())
				.withString(LastNameAttr, user.getLastName())
				.withString(URLAttr, user.getPictureURL())
				.withList(FollowersAttr, user.getFollowers().toArray())
				.withList(FollowingAttr, user.getFollowing().toArray());

		try {
			System.out.println("Adding a new item...");
			PutItemOutcome outcome = table.putItem(item);
			return "PutItem succeeded:\n" + outcome.getPutItemResult();

		}
		catch (Exception e) {
			e.printStackTrace();
			return e.toString();
		}
	}
}
