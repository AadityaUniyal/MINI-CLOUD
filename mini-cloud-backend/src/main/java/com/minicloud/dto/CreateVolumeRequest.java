package com.minicloud.dto;

public class CreateVolumeRequest {
    private Integer size;
    private String volumeType; // gp2, gp3, io1
    private Integer iops;
    private String availabilityZone;

    public CreateVolumeRequest() {}

    public Integer getSize() { return size; }
    public void setSize(Integer size) { this.size = size; }
    public String getVolumeType() { return volumeType; }
    public void setVolumeType(String volumeType) { this.volumeType = volumeType; }
    public Integer getIops() { return iops; }
    public void setIops(Integer iops) { this.iops = iops; }
    public String getAvailabilityZone() { return availabilityZone; }
    public void setAvailabilityZone(String availabilityZone) { this.availabilityZone = availabilityZone; }
}
