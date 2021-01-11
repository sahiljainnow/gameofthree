# Game of Three

When a player starts, it incepts a random (whole) number and sends it to the second player as an approach of starting the game. The receiving player can now always choose between adding one of {1, 0, 1} to get to a number that is divisible by 3. Divide it by three. The resulting whole number is then sent back to the original sender. The same rules are applied until one player reaches the number 1(after the division) (player win).

Technical Description:

App is created using Spring Boot. You can run the server by maven command : mvn spring-boot:run

Then you have to open two separate windows in browser with the url : http://localhost:8080

I have created two users by which you can login user1 and user2.

Username : user1, Passwor: user1
Username : user2, Password : user2

After login, you will see a screenasking the recepient user with whom you want to play and the number you want to send.

You can fill the form on both the browser windows and start playing.

Also, i have added radio button for manual and auto, through which you can select which mode you want to use.
