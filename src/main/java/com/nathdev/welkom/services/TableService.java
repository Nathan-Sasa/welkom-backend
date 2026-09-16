package com.nathdev.welkom.services;

import com.nathdev.welkom.components.AuthenticateUser;
import com.nathdev.welkom.dto.tables.AssignerTableRequest;
import com.nathdev.welkom.dto.tables.CreateTableRequest;
import com.nathdev.welkom.dto.tables.TableGuestResponse;
import com.nathdev.welkom.dto.tables.TableResponse;
import com.nathdev.welkom.exceptions.accessDenied.AccessDeniedCustomException;
import com.nathdev.welkom.exceptions.badRequest.BadRequestCustomException;
import com.nathdev.welkom.exceptions.event.EventNotFoundException;
import com.nathdev.welkom.exceptions.exist.AlreadyExistCustomException;
import com.nathdev.welkom.exceptions.notFound.NotFoundCustomException;
import com.nathdev.welkom.models.Event;
import com.nathdev.welkom.models.Guest;
import com.nathdev.welkom.models.Tables;
import com.nathdev.welkom.models.User;
import com.nathdev.welkom.repositories.EventRepository;
import com.nathdev.welkom.repositories.GuestRepository;
import com.nathdev.welkom.repositories.TableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TableService {

    private final TableRepository tableRepository;
    private final AuthenticateUser authenticateUser;
    private final EventRepository eventRepository;
    private final GuestRepository guestRepository;

    public TableResponse createTable(CreateTableRequest request, UUID eventUuid) {

        User user =  authenticateUser.getUser();

        Event event = eventRepository
                .findByUuidAndDeletedAtIsNull(eventUuid)
                .orElseThrow(() -> new EventNotFoundException(eventUuid));

        if (!event.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedCustomException("Cet événement ne vous appartient pas !");
        }

        if (tableRepository.findByTableName(request.tableName()).isPresent()) {
            throw new AlreadyExistCustomException("La table " + request.tableName() + "existe déjà !");
        }

        Tables table = new Tables();
        table.setTableName(request.tableName());
        table.setMaxSeats(request.maxSeats());
        table.setCountSeats(request.maxSeats());
        table.setEvent(event);
        tableRepository.save(table);

        return toResponse(table);
    }

    public TableResponse assignerTable(AssignerTableRequest request, UUID eventUuid) {

        User user =   authenticateUser.getUser();

        Event event = eventRepository
                .findByUuidAndDeletedAtIsNull(eventUuid)
                .orElseThrow(() -> new EventNotFoundException(eventUuid));

        if (!event.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedCustomException("Cet événement ne vous appartient pas !");
        }

        Tables table = tableRepository
                .findByTableName(request.tableName())
                .orElseThrow(() -> new NotFoundCustomException("Aucune table "+request.tableName()+" trouvée !"));

        if (!table.getEvent().getUser().getId().equals(user.getId())) {
            throw new AccessDeniedCustomException("Cet événement ne vous appartient pas !");
        }

        Guest guest = guestRepository
                .findByUuidAndDeletedAtIsNull(request.guestUuid())
                .orElseThrow(() -> new NotFoundCustomException("Aucun invité trouvé"));

        if (guest.getTables() != null){
            throw new BadRequestCustomException(guest.getFirstName() + guest.getLastName() + "est déjà assigné à une table !");
        }

        if (table.getCountSeats() < 1){
            throw new AccessDeniedCustomException("Vous avez atteint les places maximum"+" ("+table.getMaxSeats()+") "+"pour la table "+request.tableName()+" !");
        }

        guest.setTables(table);
        table.setCountSeats(table.getCountSeats() - 1);

        Tables saved = tableRepository.save(table);
        return toResponse(saved);
    }


    public List <TableResponse> getTables(UUID eventUuid) {
        User user =  authenticateUser.getUser();

        Event event = eventRepository
                .findByUuidAndDeletedAtIsNull(eventUuid)
                .orElseThrow(() -> new  EventNotFoundException(eventUuid));

        if (!event.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedCustomException("Cet événement ne vous appartient pas !");
        }

        return tableRepository
                .findAllByEvent(event)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public TableResponse getById(UUID eventUuid, UUID tableUuid) {
        User user =  authenticateUser.getUser();

        Event event = eventRepository
                .findByUuidAndDeletedAtIsNull(eventUuid)
                .orElseThrow(() -> new  EventNotFoundException(eventUuid));

        if (!event.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedCustomException("Cet événement ne vous appartient pas !");
        }

        Tables table = tableRepository
                .findByUuid(tableUuid)
                .orElseThrow(() -> new NotFoundCustomException("Aucune table trouvé"));

        return toResponse(table);
    }

    private TableResponse toResponse(Tables table) {
        return new TableResponse(
                table.getUuid(),
                table.getTableName(),
                table.getMaxSeats(),
                table.getCountSeats(),
                guestRepository
                        .findAllByTablesAndDeletedAtIsNull(table)
                        .stream()
                        .map(this::toGuestResponse)
                        .toList()

        );
    }

    private TableGuestResponse toGuestResponse(Guest guest) {
        return new TableGuestResponse(
                guest.getFirstName(),
                guest.getLastName()
        );
    }

}
