package com.cmpt276.project.walkinggroupapp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;

/**
 * This class model a message.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Message {

    private Long id;
    private Date timestamp;
    private String text;
    private User fromUser;
    private WalkingGroup toGroup;
    private boolean emergency;
    private String href;

    @JsonIgnore
    private boolean isRead;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public User getFromUser() {
        return fromUser;
    }

    public void setFromUser(User fromUser) {
        this.fromUser = fromUser;
    }

    public WalkingGroup getToGroup() {
        return toGroup;
    }

    public void setToGroup(WalkingGroup toGroup) {
        this.toGroup = toGroup;
    }

    public boolean isEmergency() {
        return emergency;
    }

    public void setEmergency(boolean emergency) {
        this.emergency = emergency;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }
}
