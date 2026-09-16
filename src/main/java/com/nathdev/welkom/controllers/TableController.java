package com.nathdev.welkom.controllers;

import com.nathdev.welkom.dto.event.EventResponse;
import com.nathdev.welkom.dto.tables.AssignerTableRequest;
import com.nathdev.welkom.dto.tables.CreateTableRequest;
import com.nathdev.welkom.dto.tables.TableResponse;
import com.nathdev.welkom.services.TableService;
import com.sun.jdi.request.EventRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/events")
@Tag(name = "Table", description = "Endpoint pour créer de tables et assigner table aux invités")
@RequiredArgsConstructor
public class TableController {

    private final TableService tableService;

    @PostMapping("/{eventUuid}/table")
    @Operation(summary = "Créer une table")
    public ResponseEntity<@NotNull TableResponse> createTable(
            @RequestBody CreateTableRequest request,
            @PathVariable UUID eventUuid
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(tableService.createTable(request, eventUuid));
    }

    @GetMapping("/{eventUuid}/table")
    @Operation(summary = "list de tables")
    public ResponseEntity<@NotNull List<TableResponse>> getTables(
            @PathVariable UUID eventUuid
    ) {
        return ResponseEntity
                .ok()
                .body(tableService.getTables(eventUuid));
    }

    @PostMapping("/{eventUuid}/table/{tableUuid}")
    @Operation(summary = "assigner une table à un invité")
    public ResponseEntity<@NotNull TableResponse> assignTable(
            @PathVariable UUID eventUuid,
            @RequestBody AssignerTableRequest request
            ){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(tableService.assignerTable(request, eventUuid));
    }

    @GetMapping("/{eventUuid}/table/{tableUuid}")
    @Operation(summary = "Voir le details d'une table")
    public ResponseEntity<@NotNull TableResponse> getTableByUuid(
            @PathVariable UUID eventUuid,
            @PathVariable UUID tableUuid
    ){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(tableService.getById(eventUuid, tableUuid));
    }
}
