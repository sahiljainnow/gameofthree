package com.gameofthree.demo.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import com.gameofthree.demo.model.ChatMessage;
import com.gameofthree.demo.service.GameService;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class GameController {

	@Autowired
	private GameService gameService;

	@MessageMapping("/play/send")
	@SendToUser("/queue/reply")
	public void sendMessage(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor simpMessageHeaderAccessor,
			Principal principal) {
		log.info("Received Send message request in controller from {} to user {}", principal.getName(),
				chatMessage.getReceiver());
		gameService.sendMessage(chatMessage, principal);
	}

	@MessageExceptionHandler
	@SendToUser("/queue/reply")
	public ChatMessage handleException(RuntimeException ex) {
		log.error(ex.getMessage(), ex);
		return new ChatMessage(null, null, ex.getMessage());
	}

}
