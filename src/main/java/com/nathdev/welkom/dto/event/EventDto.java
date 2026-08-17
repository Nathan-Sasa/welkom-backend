package com.nathdev.welkom.dto.event;

import com.nathdev.welkom.enums.EventsStatus;
import com.nathdev.welkom.enums.Payment_status;
import com.nathdev.welkom.models.Event;
//import com.nathdev.welkom.models.Tables;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EventDto {
    private long id;
    private String secureId;
    private String title;
    private String description;
    private String date_event;
    private Payment_status payment_status;
    private EventsStatus event_status;
    private String image;
    private String location;
    private LocalDateTime date_event_start;
    private LocalDateTime date_event_end;
    private long estimated_guests;
    private int lat;
    private int lng;
    private String event_key;
//    private int tablesId;

    public static EventDto fromEntity(
            Event event
    ){
        EventDto eventDto = new EventDto();
        eventDto.id = event.getId();
        eventDto.title = event.getTitle();
        eventDto.description = event.getDescription();
//        eventDto.date_event = event.getDateEvent();
        eventDto.payment_status = event.getPaymentStatus();
        eventDto.event_status = event.getStatus();
        eventDto.image = event.getImage();
        eventDto.estimated_guests = event.getEstimatedGuests();
//        eventDto.location = event.getLocation().getAddress();
//        eventDto.lat = event.getLocation().getLat();
//        eventDto.lng = event.getLocation().getLon();
        eventDto.date_event_start = event.getDateEventStart();
        eventDto.date_event_end = event.getDateEventEnd();
        eventDto.event_key = event.getSecurityEventKey();

        return eventDto;
    }

}
