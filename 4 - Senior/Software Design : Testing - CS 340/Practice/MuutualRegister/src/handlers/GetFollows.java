package handlers;

import DAO.UserDAO;
import Model.Post;
import Model.PostSkel;
import Model.User;
import Requests.FollowRequest;
import Requests.FollowingRequest;
import Requests.GeneralRequest;
import Result.FollowResult;
import Result.PostListResult;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;

import java.text.ParseException;
import java.util.*;

public class GetFollows {
    public GetFollows(){}

    public FollowResult handle(FollowingRequest request, Context context) throws ParseException {
        LambdaLogger logger = context.getLogger();
        logger.log("Entering GetFollows...");

        UserDAO userDAO = new UserDAO();
        List<User> follows = userDAO.getFollows(request.username, request.type);

        return new FollowResult(follows);
    }
}