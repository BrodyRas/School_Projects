package DAO;

import Model.Post;
import Model.PostSkel;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Thread.sleep;

public class PostDAO {
    private static final String PostsTable = "posts-table";
    private static final String FeedTable = "feed-table";
    private static final String UsernameAttr = "Username";
    private static final String FirstNameAttr = "FirstName";
    private static final String LastNameAttr = "LastName";
    private static final String URLAttr = "ProfileURL";
    private static final String BodyAttr = "Body";
    private static final String TimeAttr = "Time";
    private static final String AttachmentAttr = "Attachment";
    private static final String HashAttr = "Hashtags";
    private static final String AuthorAttr = "Author";

    private static AmazonDynamoDB amazonDynamoDB = AmazonDynamoDBClientBuilder
            .standard()
            .withRegion("us-east-2")
            .build();
    private static DynamoDB dynamoDB = new DynamoDB(amazonDynamoDB);

    public PostDAO() {
    }

    public List<PostSkel> getStory(String username) {
        Map<String, String> attrNames = new HashMap<String, String>();
        attrNames.put("#user", UsernameAttr);

        Map<String, AttributeValue> attrValues = new HashMap<>();
        attrValues.put(":Username", new AttributeValue().withS(username));

        QueryRequest queryRequest = new QueryRequest()
                .withTableName(PostsTable)
                .withKeyConditionExpression("#user = :Username")
                .withExpressionAttributeNames(attrNames)
                .withExpressionAttributeValues(attrValues);

        QueryResult queryResult = amazonDynamoDB.query(queryRequest);
        List<Map<String, AttributeValue>> items = queryResult.getItems();
        List<PostSkel> list = new ArrayList<>();
        if (items != null) {
            for (Map<String, AttributeValue> item : items) {
                String myUsername = item.get(UsernameAttr).getS();
                String time = item.get(TimeAttr).getS();
                String body = item.get(BodyAttr).getS();
                String firstName = item.get(FirstNameAttr).getS();
                String lastName = item.get(LastNameAttr).getS();
                String url = item.get(URLAttr).getS();
                String attachment = null;
                if (item.get(AttachmentAttr) != null) {
                    attachment = item.get(AttachmentAttr).getS();
                }

                list.add(new PostSkel(myUsername, firstName, lastName, url, body, attachment, time));
            }
        }

        return list;
    }

    public List<PostSkel> getFeed(String username) {
        Map<String, String> attrNames = new HashMap<String, String>();
        attrNames.put("#user", UsernameAttr);

        Map<String, AttributeValue> attrValues = new HashMap<>();
        attrValues.put(":Username", new AttributeValue().withS(username));

        QueryRequest queryRequest = new QueryRequest()
                .withTableName(FeedTable)
                .withKeyConditionExpression("#user = :Username")
                .withExpressionAttributeNames(attrNames)
                .withExpressionAttributeValues(attrValues);

        QueryResult queryResult = amazonDynamoDB.query(queryRequest);
        List<Map<String, AttributeValue>> items = queryResult.getItems();
        List<PostSkel> list = new ArrayList<>();
        if (items != null) {
            for (Map<String, AttributeValue> item : items) {
                String myUsername = item.get(AuthorAttr).getS();
                String time = item.get(TimeAttr).getS();
                String body = item.get(BodyAttr).getS();
                String firstName = item.get(FirstNameAttr).getS();
                String lastName = item.get(LastNameAttr).getS();
                String url = item.get(URLAttr).getS();
                String attachment = null;
                if (item.get(AttachmentAttr) != null) {
                    attachment = item.get(AttachmentAttr).getS();
                }

                list.add(new PostSkel(myUsername, firstName, lastName, url, body, attachment, time));
            }
        }

        return list;
    }

    public List<PostSkel> getHashedPosts(String hash) {
        Map<String, AttributeValue> expressionAttributeValues =
                new HashMap<String, AttributeValue>();
        expressionAttributeValues.put(":val", new AttributeValue().withS(hash));

        ScanRequest scanRequest = new ScanRequest()
                .withTableName(PostsTable)
                .withFilterExpression("contains(Hashtags, :val)")
                .withExpressionAttributeValues(expressionAttributeValues);

        ScanResult result = amazonDynamoDB.scan(scanRequest);
        List<PostSkel> list = new ArrayList<>();
        for (Map<String, AttributeValue> item : result.getItems()) {
            String username = item.get(UsernameAttr).getS();
            String time = item.get(TimeAttr).getS();
            String body = item.get(BodyAttr).getS();
            String firstName = item.get(FirstNameAttr).getS();
            String lastName = item.get(LastNameAttr).getS();
            String url = item.get(URLAttr).getS();
            String attachment = null;
            if (item.get(AttachmentAttr) != null) {
                attachment = item.get(AttachmentAttr).getS();
            }

            list.add(new PostSkel(username, firstName, lastName, url, body, attachment, time));
        }

        return list;
    }

    public String makePost(Post post) {
        Table postTable = dynamoDB.getTable(PostsTable);
        Table feedTable = dynamoDB.getTable(FeedTable);

        post.parseBody();

        //Puts on StoryTable
        Item postItem = new Item()
                .withPrimaryKey(UsernameAttr, post.getUsername())
                .withString(TimeAttr, post.getTimeString())
                .withString(BodyAttr, post.getBody())
                .withString(FirstNameAttr, post.getFirstName())
                .withString(LastNameAttr, post.getLastName())
                .withString(URLAttr, post.getProfilePictureURL())
                .withList(HashAttr, post.getHashtags().toArray());

        if (post.getAttachment() != null) {
            postItem.withString(AttachmentAttr, post.getAttachment());
        }

        //Puts on FeedTable
        Item feedItem = new Item()
                .withPrimaryKey(UsernameAttr, post.getUsername())
                .withString(TimeAttr, post.getTimeString())
                .withString(BodyAttr, post.getBody())
                .withString(FirstNameAttr, post.getFirstName())
                .withString(LastNameAttr, post.getLastName())
                .withString(URLAttr, post.getProfilePictureURL())
                .withList(HashAttr, post.getHashtags().toArray())
                .withString(AuthorAttr, post.getUsername());

        if (post.getAttachment() != null) {
            feedItem.withString(AttachmentAttr, post.getAttachment());
        }

        try {
            System.out.println("Adding a new item...");
            postTable.putItem(postItem);
            feedTable.putItem(feedItem);

            return "PutItem succeeded!";

        } catch (Exception e) {
            e.printStackTrace();
            return e.toString();
        }
    }

    public String batchWriteFollow(List<String> followers, Post post) {
        List<Item> feedList = new ArrayList<>();

        for (String s : followers) {
            Item feedItem = new Item()
                    .withPrimaryKey(UsernameAttr, s)
                    .withString(TimeAttr, post.getTimeString())
                    .withString(BodyAttr, post.getBody())
                    .withString(FirstNameAttr, post.getFirstName())
                    .withString(LastNameAttr, post.getLastName())
                    .withString(URLAttr, post.getProfilePictureURL())
                    .withList(HashAttr, post.getHashtags().toArray())
                    .withString(AuthorAttr, post.getUsername());

            if (post.getAttachment() != null) {
                feedItem.withString(AttachmentAttr, post.getAttachment());
            }
            feedList.add(feedItem);
        }

        System.out.println("batchWrite: About to write " + feedList.size() + " items to DB...");
        TableWriteItems feedTableItems = new TableWriteItems(FeedTable)
                        .withItemsToPut(feedList);

        BatchWriteItemOutcome outcome = dynamoDB.batchWriteItem(feedTableItems);
        while (outcome.getUnprocessedItems().size() > 0) {
            outcome = dynamoDB.batchWriteItemUnprocessed(outcome.getUnprocessedItems());
        }

        return "Done writing!";
    }

}
