package com.nathdev.welkom.services;

import com.nathdev.welkom.dto.invitation.InvitationResponseDto;
import com.nathdev.welkom.enums.ScanStatus;
import com.nathdev.welkom.models.Invitation;
import com.nathdev.welkom.repositories.InvitationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CheckingInvitationService {
    private final InvitationRepository invitationRepository;

    @Transactional
    public Map<String, Object> validateQrCode(UUID invitationId) {
        Map<String, Object> result = new HashMap<>();

        // 2. Est-ce que l'invitation existe ?
        var invitationOpt = invitationRepository.findByUuid(invitationId);
        if (invitationOpt.isEmpty()) {
            result.put("status", "INVALID");
            result.put("message", "Invitation inconnue ou falsifiée !");

            return result;
        }

        Invitation invitation = invitationOpt.get();

        // 2. Est-ce que l'invité a décliné l'invitation au RSVP ?
        if ("DECLINED".equals(invitation.getRsvpStatus())) {
            result.put("status", "ACCESS_DENIED");
            result.put("message", "Cette personne a indiqué qu'elle ne viendrait pas.");
            return result;
        }

        // 3. Est-ce que le QR Code a déjà été scanné à l'entrée ? (Anti-fraude / Copie)
        if ("SCANNED".equals(invitation.getScanStatus())) {
            result.put("status", "SCANNED");
            result.put("message", "Alerte ! Ce ticket a déjà était validé ");
            result.put("time", invitation.getScannedAt());
            result.put("guestName", InvitationResponseDto.fromEntity(invitation).getGuestName());
            return result;
        }

        // 4. Tout est OK : On valide l'entrée
        invitation.setScanStatus(ScanStatus.SCANNED);
        invitation.setScannedAt(LocalDateTime.now());
        invitationRepository.save(invitation);

        result.put("status", "ACCESS_GRANTED");
        result.put("guestName", InvitationResponseDto.fromEntity(invitation).getGuestName());
        result.put("category", InvitationResponseDto.fromEntity(invitation).getGuestCategory());
        result.put("table", InvitationResponseDto.fromEntity(invitation).getTable());
        return result;

    }
}

//PENDING,
//CONFIRM,
//DECLINED,
//INACTIVE
