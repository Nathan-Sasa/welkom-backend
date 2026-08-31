package com.nathdev.welkom.controllers;

import com.nathdev.welkom.services.InvitationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/public")
public class PublicInvitationController {

    private  final InvitationService invitationService;
}
