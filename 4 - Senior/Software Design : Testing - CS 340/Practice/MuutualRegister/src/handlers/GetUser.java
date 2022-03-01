package handlers;

import DAO.UserDAO;
import Model.User;
import Requests.GeneralRequest;
import Result.UserResult;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;


public class GetUser {
    public UserResult handle(GeneralRequest request, Context context) {
        LambdaLogger logger = context.getLogger();
        logger.log("Entering GetUser...\n");

        UserDAO userDAO = new UserDAO();
        User user = userDAO.getUser(request.request);
        if(user == null){
            return new UserResult("No such user");
        }
        else{
            return new UserResult(user);
        }
    }
}
