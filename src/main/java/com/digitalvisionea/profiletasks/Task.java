package com.digitalvisionea.profiletasks;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;


@Entity
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String title;
    private String content;
    private int status;
    private int priority;
    private int userId;
    private Date modifiedOn;
    private int modifiedBy;


    public Task() {  }

    public Task(String title, String content,int status,int priority,int userId,Date modifiedOn,int modifiedBy) {
        this.setTitle(title);
        this.setContent(content);
        this.setStatus(status);
        this.setPriority(priority);
        this.setUserId(userId);
        this.setModifiedBy(modifiedBy);
        this.setModifiedOn(modifiedOn);
    }


    public Task(String title, String content,int status,int priority,int userId) {
        this.setTitle(title);
        this.setContent(content);
        this.setStatus(status);
        this.setPriority(priority);
        this.setUserId(userId);
    }

    public Task(int id, String title, String content) {
        this.setId(id);
        this.setTitle(title);
        this.setContent(content);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    //add setters and getters
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    //add setters and getters


    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    //add setters and getters


    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    //add setters and getters


    public Date getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(Date modifiedOn) {
        this.modifiedOn = modifiedOn;
    }


    //add setters and getters


    public int getModifiedBy() {
        return modifiedBy;
    }


    public void setModifiedBy(int modifiedBy) {
        this.modifiedBy = modifiedBy;
    }
    

 

 

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", title='" + title + "'" +
                ", content='" + content + "'" +
                '}';
    }
}