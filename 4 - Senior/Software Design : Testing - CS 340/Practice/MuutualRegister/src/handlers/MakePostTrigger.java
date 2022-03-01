package handlers;

import DAO.FollowsDAO;
import DAO.PostDAO;
import DAO.ResultsPage;
import DAO.UserDAO;
import Model.Post;
import Model.User;
import Requests.PageRequest;
import Requests.PostRequest;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;
import com.google.gson.Gson;

public class MakePostTrigger implements RequestHandler<SQSEvent, Void> {

    @Override
    public Void handleRequest(SQSEvent event, Context context) {
        LambdaLogger logger = context.getLogger();
        logger.log("Entering MakePostTrigger...\n");

        String msgBody = event.getRecords().get(0).getBody();
        System.out.println(msgBody);
        Gson gson = new Gson();
        Post post = gson.fromJson(msgBody, Post.class);
        post.parseTime();

        FollowsDAO followsDAO = new FollowsDAO();

        //Push post onto FeedQueue to update follow table
        ResultsPage page = new ResultsPage();
        page.setLastKey(null);
        int pageSize = 25;
        int totalWritten = 0;
        do {
            System.out.println("Last key: " + page.getLastKey());
            page = followsDAO.getFollowers(post.getUsername(), pageSize, page.getLastKey());

            PageRequest pageRequest = new PageRequest(page.getValues(), post);
            String messageBody = gson.toJson(pageRequest);
            String queueUrl = "https://sqs.us-east-2.amazonaws.com/378270840566/FeedQueue";

            SendMessageRequest send_msg_request = new SendMessageRequest()
                    .withQueueUrl(queueUrl)
                    .withMessageBody(messageBody);

            AmazonSQS sqs = AmazonSQSClientBuilder.defaultClient();
            sqs.sendMessage(send_msg_request);
            totalWritten += page.getValues().size();
            System.out.println("totalWritten: " + totalWritten);

        }
        while (page.hasLastKey());

        return null;
    }
}