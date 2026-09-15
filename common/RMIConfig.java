package common;

public class RMIConfig {

    public static final String REGISTRY_HOST =
            System.getenv().getOrDefault(
                    "RMI_HOST",
                    "localhost"
            );

    public static final int REGISTRY_PORT = 1099;

    public static String getRmiUrl(String serviceName) {

        return "rmi://"
                + REGISTRY_HOST
                + ":"
                + REGISTRY_PORT
                + "/"
                + serviceName;
    }
}