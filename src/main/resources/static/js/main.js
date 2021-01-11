'use strict';

let recipient = document.querySelector('#recipient');
let playNumber = document.querySelector('#playNumber');
let playForm = document.querySelector('#playForm');
let msgList = document.querySelector('#msgList');
let manual = document.querySelector('#manual');
let auto = document.querySelector('#auto');
let sbmt = document.querySelector('#sbmt');

let socket = new SockJS('/game');
let stompClient = Stomp.over(socket);
let initialized = false;

stompClient.connect({}, onConnected, onError);

function onConnected() {
	stompClient.subscribe('/user/queue/reply', onMessageReceived);
}

function onError(error) {
	alert('Error in connection');
}

function send(event) {
	event.preventDefault();

	if (initialized && playNumber.value % 3 !== 0) {
		alert('Number should be divisible by 3');
		return;
	}
	let chatMessage = {
		receiver : recipient.value,
		playNumber : playNumber.value / 3
	};

	if (!initialized) {
		chatMessage.playNumber = playNumber.value;
	}

	stompClient.send("/app/play/send", {}, JSON.stringify(chatMessage));
	initialized = true;
	appendToUl('Sent ' + playNumber.value);
	if (chatMessage.playNumber == 1) {
		alert('You Won');
		initialized = false;
		msgList.innerHTML = "";
		playNumber.removeAttribute("max");
		playNumber.min = 1;
		playNumber.value = null;
	}

	
	
}

function onMessageReceived(payload) {
	initialized = true;
	let message = JSON.parse(payload.body);
	if(message.errorMsg){
		msgList.removeChild(msgList.lastElementChild);
		initialized = false;
		setTimeout(function() {
			alert(message.errorMsg);
		}, 500);
		return;
	}
	if (message.playNumber == 1) {
		alert(recipient.value + ' won');
		initialized = false;
		msgList.innerHTML = "";
		playNumber.value = null;
		playNumber.removeAttribute("max");
		playNumber.min = 1;
		return;
	}
	playNumber.min = message.playNumber - 1;
	playNumber.max = message.playNumber + 1;
	appendToUl('Received ' + message.playNumber);
	if (auto.checked) {
		if (message.playNumber % 3 === 0) {
			playNumber.value = message.playNumber;
		} else if ((message.playNumber - 1) % 3 === 0) {
			playNumber.value = message.playNumber - 1;
		} else {
			playNumber.value = message.playNumber + 1;
		}
		setTimeout(function() {
			sbmt.click();
		}, 500);
	}
}

function appendToUl(msg) {
	let li = document.createElement("li");
	li.appendChild(document.createTextNode(msg));
	msgList.appendChild(li);
}

playForm.addEventListener('submit', send, true);
