package handlers;

import DAO.PostDAO;
import Model.Post;
import Requests.PageRequest;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.google.gson.Gson;
import java.util.List;

public class UpdateFeedTrigger implements RequestHandler<SQSEvent, Void> {

    @Override
    public Void handleRequest(SQSEvent event, Context context) {
        LambdaLogger logger = context.getLogger();
        logger.log("Entering UpdateFeedTrigger...\n");

        for(SQSEvent.SQSMessage msg : event.getRecords()) {
            String msgBody = msg.getBody();
            System.out.println(msgBody);
            Gson gson = new Gson();
            PageRequest pageRequest = gson.fromJson(msgBody, PageRequest.class);
            List<String> values = pageRequest.values;
            Post post = pageRequest.post;
            post.parseTime();
            System.out.println(values);

            PostDAO postDAO = new PostDAO();
            System.out.println("Page size " + values.size() + " : " + postDAO.batchWriteFollow(values, post));

        }

        return null;
    }
}