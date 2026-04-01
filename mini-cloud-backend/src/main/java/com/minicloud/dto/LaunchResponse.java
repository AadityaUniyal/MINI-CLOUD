package com.minicloud.dto;

public class LaunchResponse {
    private String instanceId;
    private String status;

    public LaunchResponse() {}
    public LaunchResponse(String instanceId, String status) {
        this.instanceId = instanceId; this.status = status;
    }
    public String getInstanceId() { return instanceId; }
    public void setInstanceId(String instanceId) { this.instanceId = instanceId; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private final LaunchResponse r = new LaunchResponse();
        public Builder instanceId(String v) { r.instanceId = v; return this; }
        public Builder status(String v) { r.status = v; return this; }
        public LaunchResponse build() { return r; }
    }
}
