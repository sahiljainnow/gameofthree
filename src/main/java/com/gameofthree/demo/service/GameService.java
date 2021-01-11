package com.gameofthree.demo.service;

import java.security.Principal;

import com.gameofthree.demo.model.ChatMessage;

public interface GameService {

	/**
	 * Send play number to the recipient.
	 * 
	 * @param chatMessage
	 * @param fromUser
	 */
	void sendMessage(ChatMessage chatMessage, Principal fromUser);
	
}
