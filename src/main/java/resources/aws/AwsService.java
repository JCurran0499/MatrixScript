package resources.aws;

import io.github.cdimascio.dotenv.Dotenv;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.ssm.SsmClient;
import software.amazon.awssdk.services.ssm.model.GetParameterRequest;
import software.amazon.awssdk.services.ssm.model.GetParameterResponse;
import software.amazon.awssdk.regions.Region;

public class AwsService {

    private static final String ERROR_MESSAGE = """
        An error has occurred while a user was using MatrixScript.

        The command causing the error was "%s".
        Error message:
        %s""";

    private static final String ERROR_SUBJECT = "[ALERT] MatrixScript Error";

    private static final SnsClient sns = SnsClient.builder()
        .region(Region.US_EAST_1)
        .build();

    private static final SsmClient ssm = SsmClient.builder()
        .region(Region.US_EAST_1)
        .build();

    private static final Dotenv env = Dotenv.load();


    public static boolean authorize(String auth) {
        if (devEnvironment())
            return true;

        GetParameterResponse response = ssm.getParameter(GetParameterRequest.builder()
            .name(env.get("SSM"))
            .withDecryption(true)
            .build()
        );

        return auth != null && auth.equals(response.parameter().value());
    }

    public static void publishError(String topicArn, String command, Exception e) {
        if (!devEnvironment()) {
            sns.publish(PublishRequest.builder()
                .topicArn(topicArn)
                .message(String.format(ERROR_MESSAGE, command, e.getLocalizedMessage()))
                .subject(ERROR_SUBJECT)
                .build()
            );
        }
    }

    private static boolean devEnvironment() {
        return env.get("ENVIRONMENT", "").equals("dev");
    }
}
