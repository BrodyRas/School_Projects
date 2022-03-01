package ses;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.simpleemail.*;
import com.amazonaws.services.simpleemail.model.*;
import com.amazonaws.regions.Regions;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class EmailSender {
    public EmailResult handleRequest(EmailRequest request, Context context) {
        EmailResult myResult;

        LambdaLogger logger = context.getLogger();
        logger.log("Entering send_email");

        try {
            AmazonSimpleEmailService client =
                    AmazonSimpleEmailServiceClientBuilder.standard()
                            .withRegion(Regions.US_WEST_2).build();

            List<String> destinationList = new ArrayList<>();
            destinationList.add(request.to);
            Destination destination = new Destination(destinationList);
            Content subjectContent = new Content(request.subject);
            Content bodyContent = new Content(request.textBody);
            Body body = new Body(bodyContent);
            Message message = new Message(subjectContent, body);

            SendEmailRequest sendEmailRequest = new SendEmailRequest(request.from, destination, message);

            SendEmailResult result = client.sendEmail(sendEmailRequest);

            Timestamp stamp = new Timestamp(System.currentTimeMillis());
            myResult = new EmailResult(result.getMessageId(), stamp.toString());


            logger.log("Email sent!");
        } catch (Exception ex) {
            logger.log("The email was not sent. Error message: " + ex.getMessage());
            throw new RuntimeException(ex);
        }
        finally {
            logger.log("Leaving send_email");
        }

        return myResult;
    }

}