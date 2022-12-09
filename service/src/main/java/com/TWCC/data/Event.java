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
@Builder
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
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

    @Column(name = "longitude")
    private double longitude;

    @Column(name = "latitude")
    private double latitude;

    @Column(name = "cost")
    private float cost;

    @Column(name = "media")
    private String media;

    @Column(name = "host")
    private int host;

    @Column(name = "categories")
    private String categories;

    @CreationTimestamp
    @Column(name = "creation_timestamp")
    @JsonFormat(shape = JsonFormat.Shape.STRING,
            pattern = "yyyy-MM-dd'T'HH:mm:ss.SSZ")
    private Timestamp creationTimestamp;

    @Column(name = "start_timestamp")
    @JsonFormat(shape = JsonFormat.Shape.STRING,
            pattern = "yyyy-MM-dd'T'HH:mm:ss.SSZ")
    private Timestamp startTimestamp;

    @Column(name = "end_timestamp")
    @JsonFormat(shape = JsonFormat.Shape.STRING,
            pattern = "yyyy-MM-dd'T'HH:mm:ss.SSZ")
    private Timestamp endTimestamp;

    public Event() {
    }

    @SuppressWarnings("checkstyle:ParameterNumber")
    public Event(final int id, final String address, final int ageLimit,
                 final String name, final String description,
                 final double longitude, final double latitude,
                 final float cost, final String media, int host,
                 final String categories,
                 final Timestamp creationTimestamp,
                 final Timestamp startTimestamp,
                 final Timestamp endTimestamp) {
        this.id = id;
        this.address = address;
        this.ageLimit = ageLimit;
        this.name = name;
        this.description = description;
        this.longitude = longitude;
        this.latitude = latitude;
        this.cost = cost;
        this.media = media;
        this.host = host;
        this.categories = categories;
        this.creationTimestamp = creationTimestamp;
        this.startTimestamp = startTimestamp;
        this.endTimestamp = endTimestamp;
    }

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(final String address) {
        this.address = address;
    }

    public int getAgeLimit() {
        return ageLimit;
    }

    public void setAgeLimit(final int ageLimit) {
        this.ageLimit = ageLimit;
    }
 
    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(final double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(final double latitude) {
        this.latitude = latitude;
    }

    public float getCost() {
        return cost;
    }

    public void setCost(final float cost) {
        this.cost = cost;
    }

    public String getMedia() {
        return media;
    }

    public void setMedia(final String media) {
        this.media = media;
    }

    public int getHost() {
        return host;
    }

    public void setHost(final int host) {
        this.host = host;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public Timestamp getCreationTimestamp() {
        return creationTimestamp;
    }

    public void setCreationTimestamp(final Timestamp creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }

    public Timestamp getStartTimestamp() {
        return startTimestamp;
    }

    public void setStartTimestamp(final Timestamp startTimestamp) {
        this.startTimestamp = startTimestamp;
    }

    public Timestamp getEndTimestamp() {
        return endTimestamp;
    }

    public void setEndTimestamp(final Timestamp endTimestamp) {
        this.endTimestamp = endTimestamp;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Event other = (Event) obj;
        if (id != other.id)
            return false;
        if (address == null) {
            if (other.address != null)
                return false;
        } else if (!address.equals(other.address))
            return false;
        if (ageLimit != other.ageLimit)
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (description == null) {
            if (other.description != null)
                return false;
        } else if (!description.equals(other.description))
            return false;
        if (Double.doubleToLongBits(longitude) != Double.doubleToLongBits(other.longitude))
            return false;
        if (Double.doubleToLongBits(latitude) != Double.doubleToLongBits(other.latitude))
            return false;
        if (Float.floatToIntBits(cost) != Float.floatToIntBits(other.cost))
            return false;
        if (media == null) {
            if (other.media != null)
                return false;
        } else if (!media.equals(other.media))
            return false;
        if (creationTimestamp == null) {
            if (other.creationTimestamp != null)
                return false;
        } else if (!creationTimestamp.equals(other.creationTimestamp))
            return false;
        if (startTimestamp == null) {
            if (other.startTimestamp != null)
                return false;
        } else if (!startTimestamp.equals(other.startTimestamp))
            return false;
        if (endTimestamp == null) {
            if (other.endTimestamp != null)
                return false;
        } else if (!endTimestamp.equals(other.endTimestamp))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Event [id=" + id
                + ", address=" + address
                + ", ageLimit=" + ageLimit
                + ", name=" + name
                + ", description=" + description
                + ", longitude=" + longitude
                + ", latitude=" + latitude
                + ", cost=" + cost
                + ", media=" + media
                + ", host=" + host
                + ", categories=" + categories
                + ", creationTimestamp=" + creationTimestamp
                + ", startTimestamp=" + startTimestamp
                + ", endTimestamp=" + endTimestamp + "]";
    }

}
