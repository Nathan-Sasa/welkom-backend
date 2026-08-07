package com.nathdev.welkom.services;

import com.nathdev.welkom.dto.invitation.InvitationResponseDto;
import com.nathdev.welkom.repositories.InvitationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class InvitationService {

    @Autowired
    private InvitationRepository invitationRepository;

    public Optional<InvitationResponseDto> findInvitation(UUID id){

        return invitationRepository.findByUuid(id)
                .map(InvitationResponseDto::fromEntity);
    }
}
