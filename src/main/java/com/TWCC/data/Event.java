package com.TWCC.data;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.CreationTimestamp;
import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator="native")
    @GenericGenerator(name="native", strategy="native")
    @Column(name = "id")
    private int id;

    @Column(name = "address")
    private String address;

    @Column(name = "age_limit")
    private int ageLimit;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "long")
    private double longitude;

    @Column(name = "lat")
    private double latitude;

    @Column(name = "cost")
    private float cost;

    @Column(name = "media")
    private String media;

    @CreationTimestamp
    @Column(name = "creation_timestamp")
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss.SSZ")
    private Timestamp creationTimestamp;

    @Column(name = "start_timestamp")
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss.SSZ")
    private Timestamp startTimestamp;

    @Column(name = "end_timestamp")
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss.SSZ")
    private Timestamp endTimestamp;

    public Event() {}
    
    public Event(int id, String address, int ageLimit, String name, String description, double longitude, double latitude,
			float cost, String media, Timestamp creationTimestamp, Timestamp startTimestamp, Timestamp endTimestamp) {
		super();
		this.id = id;
		this.address = address;
		this.ageLimit = ageLimit;
		this.name = name;
		this.description = description;
		this.longitude = longitude;
		this.latitude = latitude;
		this.cost = cost;
		this.media = media;
		this.creationTimestamp = creationTimestamp;
		this.startTimestamp = startTimestamp;
		this.endTimestamp = endTimestamp;
	}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getAgeLimit() {
        return ageLimit;
    }

    public void setAgeLimit(int ageLimit) {
        this.ageLimit = ageLimit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public float getCost() {
        return cost;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }

    public String getMedia() {
        return media;
    }

    public void setMedia(String media) {
        this.media = media;
    }

    public Timestamp getCreationTimestamp() {
        return creationTimestamp;
    }

    public void setCreationTimestamp(Timestamp creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }

    public Timestamp getStartTimestamp() {
        return startTimestamp;
    }

    public void setStartTimestamp(Timestamp startTimestamp) {
        this.startTimestamp = startTimestamp;
    }

    public Timestamp getEndTimestamp() {
        return endTimestamp;
    }

    public void setEndTimestamp(Timestamp endTimestamp) {
        this.endTimestamp = endTimestamp;
    }

    @Override
    public String toString() {
        return "Event [id=" + id + ", address=" + address + ", ageLimit=" + ageLimit + ", name=" + name
                + ", description=" + description + ", longitude=" + longitude + ", latitude=" + latitude + ", cost="
                + cost + ", media=" + media + ", creationTimestamp=" + creationTimestamp + ", startTimestamp="
                + startTimestamp + ", endTimestamp=" + endTimestamp + "]";
    }

    
}
