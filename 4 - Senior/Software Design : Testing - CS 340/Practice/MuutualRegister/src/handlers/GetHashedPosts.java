package handlers;

import DAO.PostDAO;
import Model.Post;
import Model.PostSkel;
import Model.User;
import Requests.GeneralRequest;
import Result.PostListResult;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;

import java.text.ParseException;
import java.util.*;

public class GetHashedPosts {
    public GetHashedPosts(){}

    public PostListResult handle(GeneralRequest request, Context context) throws ParseException {
        LambdaLogger logger = context.getLogger();
        logger.log("Entering GetHashedPosts...");

        String hash = request.request;

        PostDAO postDAO = new PostDAO();

        return new PostListResult(new ArrayList<>(postDAO.getHashedPosts(hash)));

    }
}