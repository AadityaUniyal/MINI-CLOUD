package com.minicloud.common.dto;

public class DashboardMetricsDTO {
    private long totalInstances;
    private long runningInstances;
    private long totalVolumes;
    private long totalBuckets;
    private long publicBuckets;
    private long activeDatabases;
    private long activeFunctions;
    private long activeTopics;
    private long activeQueues;
    private long vpcs;
    private long subnets;
    private long domains;
    
    // Add stable mock fields for un-implemented aspects
    private long elasticIps;
    private long securityGroups;
    private long keyPairs;
    private long automatedBackups;
    private long usedStorageTb;
    private long transferRequests;
    private long invocations30d;
    private double errorRate;
    private long avgDurationMs;
    private long subscriptions;
    private long deliveries24h;
    private long inFlightMessages;
    
    // Getters and Setters
    public long getTotalInstances() { return totalInstances; }
    public void setTotalInstances(long totalInstances) { this.totalInstances = totalInstances; }
    
    public long getRunningInstances() { return runningInstances; }
    public void setRunningInstances(long runningInstances) { this.runningInstances = runningInstances; }
    
    public long getTotalVolumes() { return totalVolumes; }
    public void setTotalVolumes(long totalVolumes) { this.totalVolumes = totalVolumes; }

    public long getTotalBuckets() { return totalBuckets; }
    public void setTotalBuckets(long totalBuckets) { this.totalBuckets = totalBuckets; }

    public long getPublicBuckets() { return publicBuckets; }
    public void setPublicBuckets(long publicBuckets) { this.publicBuckets = publicBuckets; }

    public long getActiveDatabases() { return activeDatabases; }
    public void setActiveDatabases(long activeDatabases) { this.activeDatabases = activeDatabases; }

    public long getActiveFunctions() { return activeFunctions; }
    public void setActiveFunctions(long activeFunctions) { this.activeFunctions = activeFunctions; }

    public long getActiveTopics() { return activeTopics; }
    public void setActiveTopics(long activeTopics) { this.activeTopics = activeTopics; }

    public long getActiveQueues() { return activeQueues; }
    public void setActiveQueues(long activeQueues) { this.activeQueues = activeQueues; }

    public long getVpcs() { return vpcs; }
    public void setVpcs(long vpcs) { this.vpcs = vpcs; }

    public long getSubnets() { return subnets; }
    public void setSubnets(long subnets) { this.subnets = subnets; }

    public long getDomains() { return domains; }
    public void setDomains(long domains) { this.domains = domains; }

    public long getElasticIps() { return elasticIps; }
    public void setElasticIps(long elasticIps) { this.elasticIps = elasticIps; }

    public long getSecurityGroups() { return securityGroups; }
    public void setSecurityGroups(long securityGroups) { this.securityGroups = securityGroups; }

    public long getKeyPairs() { return keyPairs; }
    public void setKeyPairs(long keyPairs) { this.keyPairs = keyPairs; }

    public long getAutomatedBackups() { return automatedBackups; }
    public void setAutomatedBackups(long automatedBackups) { this.automatedBackups = automatedBackups; }

    public long getUsedStorageTb() { return usedStorageTb; }
    public void setUsedStorageTb(long usedStorageTb) { this.usedStorageTb = usedStorageTb; }

    public long getTransferRequests() { return transferRequests; }
    public void setTransferRequests(long transferRequests) { this.transferRequests = transferRequests; }

    public long getInvocations30d() { return invocations30d; }
    public void setInvocations30d(long invocations30d) { this. invocations30d = invocations30d; }

    public double getErrorRate() { return errorRate; }
    public void setErrorRate(double errorRate) { this.errorRate = errorRate; }

    public long getAvgDurationMs() { return avgDurationMs; }
    public void setAvgDurationMs(long avgDurationMs) { this.avgDurationMs = avgDurationMs; }

    public long getSubscriptions() { return subscriptions; }
    public void setSubscriptions(long subscriptions) { this.subscriptions = subscriptions; }

    public long getDeliveries24h() { return deliveries24h; }
    public void setDeliveries24h(long deliveries24h) { this.deliveries24h = deliveries24h; }

    public long getInFlightMessages() { return inFlightMessages; }
    public void setInFlightMessages(long inFlightMessages) { this.inFlightMessages = inFlightMessages; }
}
