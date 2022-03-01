import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.PutObjectRequest;

import java.io.File;

public class S3 {
    public static void main(String[] args) {

        // Create AmazonS3 object for doing S3 operations
        AmazonS3 s3 = AmazonS3ClientBuilder
                .standard()
                .withRegion("us-west-2")
                .build();

        // Write code to do the following:
        // 1. get name of file to be copied from the command line
        String fileName = args[1];
        // 2. get name of S3 bucket from the command line
        String s3Bucket = args[0];
        // 3. copy file to the specified S3 bucket using the file name as the S3 key
        PutObjectRequest request = new PutObjectRequest(s3Bucket, fileName, new File(fileName));
        s3.putObject(request);
    }
}