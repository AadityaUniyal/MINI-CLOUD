package com.minicloud.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "volumes")
public class Volume {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String volumeId;

    @Column(nullable = false)
    private Integer size; // GB

    private String volumeType; // gp2, gp3, io1, etc.
    private Integer iops;
    private String state; // creating, available, in-use, deleting
    private String availabilityZone;
    private String attachedInstanceId; // volume handles attachment tracking
    private String deviceName; // /dev/sda1, etc.
    private boolean deleteOnTermination;
    private String owner;
    private LocalDateTime creationTime;

    public Volume() {}

    private Volume(Builder builder) {
        this.id = builder.id;
        this.volumeId = builder.volumeId;
        this.size = builder.size;
        this.volumeType = builder.volumeType;
        this.iops = builder.iops;
        this.state = builder.state;
        this.availabilityZone = builder.availabilityZone;
        this.attachedInstanceId = builder.attachedInstanceId;
        this.deviceName = builder.deviceName;
        this.deleteOnTermination = builder.deleteOnTermination;
        this.owner = builder.owner;
        this.creationTime = builder.creationTime;
    }

    public static Builder builder() { return new Builder(); }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getVolumeId() { return volumeId; }
    public void setVolumeId(String volumeId) { this.volumeId = volumeId; }
    public Integer getSize() { return size; }
    public void setSize(Integer size) { this.size = size; }
    public String getVolumeType() { return volumeType; }
    public void setVolumeType(String volumeType) { this.volumeType = volumeType; }
    public Integer getIops() { return iops; }
    public void setIops(Integer iops) { this.iops = iops; }
    public String getState() { return state; }
    public void setState(String state) { this.state = state; }
    public String getAvailabilityZone() { return availabilityZone; }
    public void setAvailabilityZone(String availabilityZone) { this.availabilityZone = availabilityZone; }
    public String getAttachedInstanceId() { return attachedInstanceId; }
    public void setAttachedInstanceId(String attachedInstanceId) { this.attachedInstanceId = attachedInstanceId; }
    public String getDeviceName() { return deviceName; }
    public void setDeviceName(String deviceName) { this.deviceName = deviceName; }
    public boolean isDeleteOnTermination() { return deleteOnTermination; }
    public void setDeleteOnTermination(boolean deleteOnTermination) { this.deleteOnTermination = deleteOnTermination; }
    public String getOwner() { return owner; }
    public void setOwner(String owner) { this.owner = owner; }
    public LocalDateTime getCreationTime() { return creationTime; }
    public void setCreationTime(LocalDateTime creationTime) { this.creationTime = creationTime; }

    public static class Builder {
        private Long id;
        private String volumeId;
        private Integer size;
        private String volumeType;
        private Integer iops;
        private String state;
        private String availabilityZone;
        private String attachedInstanceId;
        private String deviceName;
        private boolean deleteOnTermination;
        private String owner;
        private LocalDateTime creationTime;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder volumeId(String volumeId) { this.volumeId = volumeId; return this; }
        public Builder size(Integer size) { this.size = size; return this; }
        public Builder volumeType(String volumeType) { this.volumeType = volumeType; return this; }
        public Builder iops(Integer iops) { this.iops = iops; return this; }
        public Builder state(String state) { this.state = state; return this; }
        public Builder availabilityZone(String availabilityZone) { this.availabilityZone = availabilityZone; return this; }
        public Builder attachedInstanceId(String attachedInstanceId) { this.attachedInstanceId = attachedInstanceId; return this; }
        public Builder deviceName(String deviceName) { this.deviceName = deviceName; return this; }
        public Builder deleteOnTermination(boolean deleteOnTermination) { this.deleteOnTermination = deleteOnTermination; return this; }
        public Builder owner(String owner) { this.owner = owner; return this; }
        public Builder creationTime(LocalDateTime creationTime) { this.creationTime = creationTime; return this; }

        public Volume build() { return new Volume(this); }
    }
}
