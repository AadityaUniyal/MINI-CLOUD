package com.minicloud.dto;

public class StatsResponse {
    private String containerId;
    private double cpuUsage;
    private long memoryUsage;
    private long memoryLimit;
    private String status;

    public StatsResponse() {}
    public StatsResponse(String containerId, double cpuUsage, long memoryUsage, long memoryLimit, String status) {
        this.containerId = containerId; this.cpuUsage = cpuUsage;
        this.memoryUsage = memoryUsage; this.memoryLimit = memoryLimit; this.status = status;
    }
    public String getContainerId() { return containerId; }
    public void setContainerId(String containerId) { this.containerId = containerId; }
    public double getCpuUsage() { return cpuUsage; }
    public void setCpuUsage(double cpuUsage) { this.cpuUsage = cpuUsage; }
    public long getMemoryUsage() { return memoryUsage; }
    public void setMemoryUsage(long memoryUsage) { this.memoryUsage = memoryUsage; }
    public long getMemoryLimit() { return memoryLimit; }
    public void setMemoryLimit(long memoryLimit) { this.memoryLimit = memoryLimit; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private final StatsResponse r = new StatsResponse();
        public Builder containerId(String v) { r.containerId = v; return this; }
        public Builder cpuUsage(double v) { r.cpuUsage = v; return this; }
        public Builder memoryUsage(long v) { r.memoryUsage = v; return this; }
        public Builder memoryLimit(long v) { r.memoryLimit = v; return this; }
        public Builder status(String v) { r.status = v; return this; }
        public StatsResponse build() { return r; }
    }
}
