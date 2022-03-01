package handlers;

import DAO.PostDAO;
import DAO.UserDAO;
import Model.Post;
import Model.PostSkel;
import Model.User;
import Requests.GeneralRequest;
import Result.PostListResult;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;

import java.text.ParseException;
import java.util.*;

public class GetFeed {
    public GetFeed(){}

    public PostListResult handle(GeneralRequest request, Context context) throws ParseException {
        LambdaLogger logger = context.getLogger();
        logger.log("Entering GetFeed...\n");

        UserDAO userDAO = new UserDAO();
        User user = userDAO.getUser(request.request);

        if(user == null){
            return new PostListResult("Bad User");
        }
        else{
            PostDAO postDAO = new PostDAO();
            ArrayList<PostSkel> list = new ArrayList<>(postDAO.getFeed(user.getUsername()));
            return new PostListResult(list);
        }
    }
}