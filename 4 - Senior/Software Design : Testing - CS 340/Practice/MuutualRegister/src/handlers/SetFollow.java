package handlers;

import DAO.FollowsDAO;
import Requests.FollowRequest;
import Result.GeneralResult;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;

public class SetFollow {
    public GeneralResult handle(FollowRequest request, Context context) {
        LambdaLogger logger = context.getLogger();
        logger.log("Entering login...");
        FollowsDAO followsDAO = new FollowsDAO();
        return new GeneralResult(followsDAO.setFollow(request.username, request.target, request.state));
    }
}
