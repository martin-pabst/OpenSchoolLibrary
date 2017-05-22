package de.sp.database.model;

/**
 * Created by Martin on 09.05.2017.
 */
public class EventRestriction {

    private Long id;
    private Long event_id;
    private Long role_id;
    private Long user_id;

    private transient Event event;

    public EventRestriction(Long role_id, Long user_id, Event event) {
        this.role_id = role_id;
        this.user_id = user_id;
        this.event = event;
        this.event_id = event.getId();
    }

    public Long getId() {
        return id;
    }

    public Long getEvent_id() {
        return event_id;
    }

    public Long getRole_id() {
        return role_id;
    }

    public Long getUser_id() {
        return user_id;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setEvent_id(Long event_id) {
        this.event_id = event_id;
    }
}
