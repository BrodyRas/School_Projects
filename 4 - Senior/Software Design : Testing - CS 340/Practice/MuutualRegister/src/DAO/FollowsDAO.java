package DAO;

import Model.User;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.QueryRequest;
import com.amazonaws.services.dynamodbv2.model.QueryResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FollowsDAO {
    public FollowsDAO(){}

	private static final String FollowsTable = "follows-table";

	private static final String FollowerAttr = "Follower";
	private static final String FollowedAttr = "Followee";

	private static AmazonDynamoDB amazonDynamoDB = AmazonDynamoDBClientBuilder
														.standard()
														.withRegion("us-east-2")
														.build();
	private static DynamoDB dynamoDB = new DynamoDB(amazonDynamoDB);

	public List<String> getFollowing(String username) {
		Map<String, String> attrNames = new HashMap<String, String>();
		attrNames.put("#follower", FollowerAttr);

		Map<String, AttributeValue> attrValues = new HashMap<>();
		attrValues.put(":Username", new AttributeValue().withS(username));

		QueryRequest queryRequest = new QueryRequest()
				.withTableName(FollowsTable)
				.withKeyConditionExpression("#follower = :Username")
				.withExpressionAttributeNames(attrNames)
				.withExpressionAttributeValues(attrValues);

		QueryResult queryResult = amazonDynamoDB.query(queryRequest);
		List<Map<String, AttributeValue>> items = queryResult.getItems();
		List<String> list = new ArrayList<>();
		if (items != null) {
			for (Map<String, AttributeValue> item : items) {
				list.add(item.get(FollowedAttr).getS());
			}
		}

		return list;
	}

	private static boolean isNonEmptyString(String value) {
		return (value != null && value.length() > 0);
	}

	public ResultsPage getFollowers(String username, int pageSize, String lastFollower) {
		ResultsPage result = new ResultsPage();
		Map<String, String> attrNames = new HashMap<>();
		attrNames.put("#followed", FollowedAttr);

		Map<String, AttributeValue> attrValues = new HashMap<>();
		attrValues.put(":username", new AttributeValue().withS(username));

		QueryRequest queryRequest = new QueryRequest()
				.withTableName(FollowsTable)
				.withIndexName("Followee-Follower-index")
				.withKeyConditionExpression("#followed = :username")
				.withExpressionAttributeNames(attrNames)
				.withExpressionAttributeValues(attrValues)
				.withLimit(pageSize);

		if (isNonEmptyString(lastFollower)) {
			Map<String, AttributeValue> startKey = new HashMap<>();
			startKey.put(FollowedAttr, new AttributeValue().withS(username));
			startKey.put(FollowerAttr, new AttributeValue().withS(lastFollower));

			queryRequest = queryRequest.withExclusiveStartKey(startKey);
		}

		QueryResult queryResult = amazonDynamoDB.query(queryRequest);
		List<Map<String, AttributeValue>> items = queryResult.getItems();
		if (items != null) {
			for (Map<String, AttributeValue> item : items) {
				result.addValue(item.get(FollowerAttr).getS());
			}
		}

		Map<String, AttributeValue> lastKey = queryResult.getLastEvaluatedKey();
		if (lastKey != null) {
			result.setLastKey(lastKey.get(FollowerAttr).getS());
		}

		return result;
	}

	public String setFollow(String follower, String followed, boolean state){
		Table table = dynamoDB.getTable(FollowsTable);

		if(state){
			Item item = new Item()
					.withPrimaryKey(FollowerAttr, follower)
					.withString(FollowedAttr, followed);
			table.putItem(item);
			return "PutItem succeeded!\n";
		}
		//remove the target name from the follows list
		else{
			table.deleteItem(FollowerAttr, follower,
					FollowedAttr, followed);
			return "PutItem succeeded:\n";
		}
	}
}
