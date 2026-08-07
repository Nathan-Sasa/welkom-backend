package com.nathdev.welkom.dto.invitation;

import com.nathdev.welkom.models.Invitation;
import lombok.Data;

import java.util.Map;

@Data
public class InvitationResponseDto {
    String uuid;
    String guestName;
//    String guestFirstName;
    String guestCategory;
    String rsvpStatus;
    String table;
    long templateId;

    String customImage1;
    String customImage2;
    String customImage3;

    String customFontTitle;
    String customFontBody;
    String customColorPrimary;
    String customColorAccent;
    boolean customHasCadre;
    String customCadreUrl;
    Map <String, Object> customData;

    public static InvitationResponseDto fromEntity(
            Invitation invitation

    ) {
        InvitationResponseDto invitationResponseDto = new InvitationResponseDto();
        invitationResponseDto.uuid = invitation.getUuid();
        invitationResponseDto.guestName = invitation.getGuest().getFirst_name() + " " + invitation.getGuest().getLast_name();
        invitationResponseDto.guestCategory = invitation.getGuest().getCategory();
        invitationResponseDto.rsvpStatus = invitation.getRsvpStatus().toString();

        if (invitation.getGuest().getTables() != null) {
            invitationResponseDto.table = invitation.getGuest().getTables().getTableName();
        } else {
            invitationResponseDto.table = "Aucun table assigné";
        }

//        invitationResponseDto.templateId = invitation.getCustomizedTemplate().getId();
//        invitationResponseDto.customImage1 = invitation.getGuest().getEvent().getCustomizedTemplates().getCustomImage1();
//        invitationResponseDto.customImage2 = invitation.getGuest().getEvent().getCustomizedTemplates().getCustomImage2();
//        invitationResponseDto.customImage3 = invitation.getGuest().getEvent().getCustomizedTemplates().getCustomImage3();
//        invitationResponseDto.customFontTitle = invitation.getGuest().getEvent().getCustomizedTemplates().getCustomFontTitle();
//        invitationResponseDto.customFontBody = invitation.getGuest().getEvent().getCustomizedTemplates().getCustomFontBody();
//        invitationResponseDto.customColorPrimary = invitation.getGuest().getEvent().getCustomizedTemplates().getCustomColorPrimary();
//        invitationResponseDto.customColorAccent = invitation.getGuest().getEvent().getCustomizedTemplates().getCustomColorAccent();
//        invitationResponseDto.customHasCadre = invitation.getGuest().getEvent().getCustomizedTemplates().getCustomHasCadre;
//        invitationResponseDto.customData = invitation.getGuest().getEvent().getCustomizedTemplates().getContentData();

        return  invitationResponseDto;
    }
}
