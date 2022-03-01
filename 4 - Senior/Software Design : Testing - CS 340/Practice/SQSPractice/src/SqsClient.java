import com.amazonaws.services.sqs.AmazonSQS;
        import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
        import com.amazonaws.services.sqs.model.SendMessageRequest;
        import com.amazonaws.services.sqs.model.SendMessageResult;

public class SqsClient {
    public static void main(String[] args) {
        String messageBody = "newDood";
        String queueUrl = "https://sqs.us-east-2.amazonaws.com/378270840566/PostQueue";

        SendMessageRequest send_msg_request = new SendMessageRequest()
                .withQueueUrl(queueUrl)
                .withMessageBody(messageBody)
                .withDelaySeconds(1);

        AmazonSQS sqs = AmazonSQSClientBuilder.defaultClient();
        SendMessageResult send_msg_result = sqs.sendMessage(send_msg_request);

        String msgId = send_msg_result.getMessageId();
        System.out.println("Message ID: " + msgId);
    }
}
