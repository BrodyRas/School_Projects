package handlers;

import DAO.UserDAO;
import Requests.PictureRequest;
import Result.GeneralResult;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;


public class ChangePicture {

    public GeneralResult handle(PictureRequest request, Context context) {
        LambdaLogger logger = context.getLogger();
        logger.log("Entering ChangePicture...\n");

        String username = request.username;
        String URL = request.URL;

        UserDAO userDAO = new UserDAO();
        String result = userDAO.changePicture(username,URL);
        return new GeneralResult(result);

    }
}
