package handlers;

import DAO.UserDAO;
import Model.Post;
import Model.User;
import Requests.LoginRequest;
import Requests.UserRequest;
import Result.AuthResult;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Register {
    public AuthResult handle(UserRequest request, Context context) {
        LambdaLogger logger = context.getLogger();
        logger.log("Entering Register...\n");
        UserDAO userDAO = new UserDAO();
        String result = userDAO.register(request.user);

        return new AuthResult(request.user.getUsername(), result);
    }
}