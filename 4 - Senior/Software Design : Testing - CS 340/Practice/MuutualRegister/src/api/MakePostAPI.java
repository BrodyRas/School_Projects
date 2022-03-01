package api;

import DAO.PostDAO;
import DAO.UserDAO;
import Model.Post;
import Model.User;
import Requests.PageRequest;
import Requests.PostRequest;
import Result.GeneralResult;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;
import com.google.gson.Gson;
import java.util.Collections;

public class MakePostAPI {
    public GeneralResult handle(PostRequest request, Context context) {
        Gson gson = new Gson();

        //Put post on StoryTable and FeedTable for myself
        UserDAO userDAO = new UserDAO();
        User user = userDAO.getUser(request.username);
        Post post = new Post(user, request.body, request.attachment);

        PostDAO postDAO = new PostDAO();
        postDAO.makePost(post);

        PageRequest pageRequest = new PageRequest(Collections.singletonList(user.getUsername()), post);
        String feedMessageBody = gson.toJson(pageRequest);
        String feedQueueUrl = "https://sqs.us-east-2.amazonaws.com/378270840566/FeedQueue";

        SendMessageRequest feed_send_msg_request = new SendMessageRequest()
                .withQueueUrl(feedQueueUrl)
                .withMessageBody(feedMessageBody);

        AmazonSQS feed_sqs = AmazonSQSClientBuilder.defaultClient();
        SendMessageResult feed_send_msg_result = feed_sqs.sendMessage(feed_send_msg_request);

        String feed_msgId = feed_send_msg_result.getMessageId();
        System.out.println("Message ID: " + feed_msgId);

        //Put request on queue to update all my follower's feeds
        String messageBody = gson.toJson(post);
        String queueUrl = "https://sqs.us-east-2.amazonaws.com/378270840566/PostQueue";

        SendMessageRequest send_msg_request = new SendMessageRequest()
                .withQueueUrl(queueUrl)
                .withMessageBody(messageBody);

        AmazonSQS sqs = AmazonSQSClientBuilder.defaultClient();
        SendMessageResult send_msg_result = sqs.sendMessage(send_msg_request);

        String msgId = send_msg_result.getMessageId();
        System.out.println("Message ID: " + msgId);

        return new GeneralResult(msgId);
    }
}
