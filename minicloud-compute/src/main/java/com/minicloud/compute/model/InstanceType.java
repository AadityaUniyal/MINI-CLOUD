package com.minicloud.compute.model;

import jakarta.persistence.*;

@Entity
@Table(name = "instance_types")
public class InstanceType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;
    
    private Integer vcpu;
    private Integer memoryGb;
    private Integer diskGb;
    private Double pricePerHour;

    public InstanceType() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Integer getVcpu() { return vcpu; }
    public void setVcpu(Integer vcpu) { this.vcpu = vcpu; }
    public Integer getMemoryGb() { return memoryGb; }
    public void setMemoryGb(Integer memoryGb) { this.memoryGb = memoryGb; }
    public Integer getDiskGb() { return diskGb; }
    public void setDiskGb(Integer diskGb) { this.diskGb = diskGb; }
    public Double getPricePerHour() { return pricePerHour; }
    public void setPricePerHour(Double pricePerHour) { this.pricePerHour = pricePerHour; }

    public static InstanceTypeBuilder builder() {
        return new InstanceTypeBuilder();
    }

    public static class InstanceTypeBuilder {
        private final InstanceType it = new InstanceType();
        public InstanceTypeBuilder name(String name) { it.name = name; return this; }
        public InstanceTypeBuilder vcpu(Integer vcpu) { it.vcpu = vcpu; return this; }
        public InstanceTypeBuilder memoryGb(Integer memory) { it.memoryGb = memory; return this; }
        public InstanceTypeBuilder diskGb(Integer disk) { it.diskGb = disk; return this; }
        public InstanceTypeBuilder pricePerHour(Double price) { it.pricePerHour = price; return this; }
        public InstanceType build() { return it; }
    }
}
