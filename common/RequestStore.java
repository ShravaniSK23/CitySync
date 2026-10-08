package common;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class RequestStore {

    private static final String FILE_NAME =
            "/app/data/citysync_requests.txt";

    public static void saveRequests(
            List<String> requests)
            throws IOException {

        File file = new File(FILE_NAME);

        File parent = file.getParentFile();

        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }

        try (BufferedWriter writer =
                     new BufferedWriter(
                             new FileWriter(file))) {

            for (String request : requests) {
                writer.write(request);
                writer.newLine();
            }
        }
    }

    public static List<String> loadRequests()
            throws IOException {

        List<String> requests =
                new ArrayList<>();

        File file = new File(FILE_NAME);

        if (!file.exists()) {
            return requests;
        }

        try (BufferedReader reader =
                     new BufferedReader(
                             new FileReader(file))) {

            String line;

            while ((line = reader.readLine()) != null) {

                if (!line.trim().isEmpty()) {
                    requests.add(line);
                }
            }
        }

        return requests;
    }
}