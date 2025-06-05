// src/main/java/com/example/backend/model/Profile.java
package com.example.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "profiles")
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private Integer age;
    private String gender;
    private String bio;
    private String location;

    public Profile() { }

    private Profile(Builder builder) {
        this.id       = builder.id;
        this.name     = builder.name;
        this.age      = builder.age;
        this.gender   = builder.gender;
        this.bio      = builder.bio;
        this.location = builder.location;
    }

    // Getters și Setters (poți genera cu IDE):
    public Long getId()        { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName()           { return name; }
    public void setName(String name)  { this.name = name; }

    public Integer getAge()           { return age; }
    public void setAge(Integer age)   { this.age = age; }

    public String getGender()             { return gender; }
    public void setGender(String gender)  { this.gender = gender; }

    public String getBio()          { return bio; }
    public void setBio(String bio)  { this.bio = bio; }

    public String getLocation()              { return location; }
    public void setLocation(String location) { this.location = location; }

    // --- iată clasa internă Builder ---
    public static class Builder {
        private Long id;
        private String name;
        private Integer age;
        private String gender;
        private String bio;
        private String location;

        public Builder() { }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }
        public Builder name(String name) {
            this.name = name;
            return this;
        }
        public Builder age(Integer age) {
            this.age = age;
            return this;
        }
        public Builder gender(String gender) {
            this.gender = gender;
            return this;
        }
        public Builder bio(String bio) {
            this.bio = bio;
            return this;
        }
        public Builder location(String location) {
            this.location = location;
            return this;
        }

        public Profile build() {
            return new Profile(this);
        }
    }
}
