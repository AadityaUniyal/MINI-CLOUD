package com.minicloud.compute.model;

import jakarta.persistence.*;

@Entity
@Table(name = "images")
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String imageId; // ami-xxxxxx
    
    private String name;
    private String osType;
    private String version;
    private String dockerImage; // e.g. ubuntu:latest
    private boolean isPublic;

    public Image() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getImageId() { return imageId; }
    public void setImageId(String imageId) { this.imageId = imageId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getOsType() { return osType; }
    public void setOsType(String osType) { this.osType = osType; }
    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }
    public String getDockerImage() { return dockerImage; }
    public void setDockerImage(String dockerImage) { this.dockerImage = dockerImage; }
    public boolean isPublic() { return isPublic; }
    public void setPublic(boolean isPublic) { this.isPublic = isPublic; }

    public static ImageBuilder builder() {
        return new ImageBuilder();
    }

    public static class ImageBuilder {
        private final Image img = new Image();
        public ImageBuilder imageId(String id) { img.imageId = id; return this; }
        public ImageBuilder name(String name) { img.name = name; return this; }
        public ImageBuilder osType(String osType) { img.osType = osType; return this; }
        public ImageBuilder dockerImage(String di) { img.dockerImage = di; return this; }
        public Image build() { return img; }
    }
}
