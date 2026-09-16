package replication;

import java.io.Serializable;

public class CityResource implements Serializable {
    private String resourceId;
    private String statusValue;
    private long timestamp;

    public CityResource(String resourceId, String statusValue, long timestamp) {
        this.resourceId = resourceId;
        this.statusValue = statusValue;
        this.timestamp = timestamp;
    }

    public String getResourceId() { return resourceId; }
    public String getStatusValue() { return statusValue; }
    public long getTimestamp() { return timestamp; }
}