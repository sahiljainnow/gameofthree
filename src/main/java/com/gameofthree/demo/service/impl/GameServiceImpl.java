package com.gameofthree.demo.service.impl;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.gameofthree.demo.exception.SameUserException;
import com.gameofthree.demo.exception.UserUnAvailableException;
import com.gameofthree.demo.model.ChatMessage;
import com.gameofthree.demo.service.GameService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class GameServiceImpl extends TextWebSocketHandler implements GameService {

	@Autowired
	private SimpUserRegistry simpUserRegistry;

	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;

	@Override
	public void sendMessage(ChatMessage chatMessage, Principal fromUser) {
		log.info("Send play number {} from {}  to user {}", chatMessage.getPlayNumber(), fromUser.getName(),
				chatMessage.getReceiver());
		List<String> connectedUsrs = this.simpUserRegistry.getUsers().stream().map(SimpUser::getName).collect(Collectors.toList());
		if(fromUser.getName().equals(chatMessage.getReceiver())) {
			throw new SameUserException("Cannot play with same user");
		}
		if(!connectedUsrs.contains(chatMessage.getReceiver())) {
			throw new UserUnAvailableException(chatMessage.getReceiver() + " is not available.");
		}
		simpMessagingTemplate.convertAndSendToUser(chatMessage.getReceiver(), "/queue/reply", chatMessage);
		log.info("Play Number Sent");
	}

}
