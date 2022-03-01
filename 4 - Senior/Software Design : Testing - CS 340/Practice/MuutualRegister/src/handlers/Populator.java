package handlers;

import DAO.FollowsDAO;
import DAO.UserDAO;
import Model.User;
import Requests.GeneralRequest;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;

public class Populator {
    public void handle(GeneralRequest request, Context context) {
        LambdaLogger logger = context.getLogger();
        logger.log("Entering Populator..." + request.request + "\n");

        User mainUser = new User(
                "MainFirst",
                "MainLast",
                "MainUsername",
                "MainPassword",
                "MainURL");

        UserDAO userDAO = new UserDAO();
        FollowsDAO followsDAO = new FollowsDAO();
        //userDAO.register(mainUser);

        for(int i = 5997; i < 10000; i++){
            User newUser = new User(
                    "fn" + i,
                    "ln" + i,
                    "un" + i,
                    "pw" + i,
                    "URL" + i);
            userDAO.register(newUser);
            followsDAO.setFollow(newUser.getUsername(), mainUser.getUsername(), true);
        }

    }
}
