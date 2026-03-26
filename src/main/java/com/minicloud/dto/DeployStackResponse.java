package com.minicloud.dto;

public class DeployStackResponse {
    private String stackId;
    private String status;
    private String loadBalancerUrl;
    private String message;

    public DeployStackResponse() {}
    public DeployStackResponse(String stackId, String status, String loadBalancerUrl, String message) {
        this.stackId = stackId; this.status = status;
        this.loadBalancerUrl = loadBalancerUrl; this.message = message;
    }
    public String getStackId() { return stackId; }
    public void setStackId(String stackId) { this.stackId = stackId; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getLoadBalancerUrl() { return loadBalancerUrl; }
    public void setLoadBalancerUrl(String loadBalancerUrl) { this.loadBalancerUrl = loadBalancerUrl; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private final DeployStackResponse r = new DeployStackResponse();
        public Builder stackId(String v) { r.stackId = v; return this; }
        public Builder status(String v) { r.status = v; return this; }
        public Builder loadBalancerUrl(String v) { r.loadBalancerUrl = v; return this; }
        public Builder message(String v) { r.message = v; return this; }
        public DeployStackResponse build() { return r; }
    }
}
