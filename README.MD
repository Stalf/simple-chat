# The Simple Chat

The simple chat implementation using pure Java and a few helper libraries: Google Guava, Project Lombok, and Logback.
The Transport Layer is TCP Sockets, but it is possible to use any other technology with some refinement.
For now, the messages are stored in the server memory and are lost after the restart. 

Project consists of three modules:
1. **server** - chat server with main runnable class `ru.kss.chat.server.ServerMain`.
1. **client** - simple console-based client with main runnable class `ru.kss.chat.client.ClientMain`.
Client module also introduces bot mode runnable class `ru.kss.chat.client.BotMain` useful for [stress testing](./stress-test/stress-test.md)        
1. **core** - module with shared interfaces, abstract, and helper classes. 

To try the chat live - just build the Root Maven project with `mvn clean package`. 
After that, the **server instance** can be run as `java -jar server/target/server-1.0.jar`.
**Client instance** can be run as `java -jar client/target/client-1.0.jar`.

#### Known limitations
1. As the chat client is a simple console application - it doesn`t provide the functionality to output 
chat messages to the screen and input user data at the same time. 
So, after successfully logging to the chat server you get into the *read* mode. 
Press Enter to switch to *input* mode and type in your message (or chat command)   

#### Plans and intentions
    - [ ] Increase unit test coverage 
    - [ ] Implement chat messages coloring and an ability to choose color through a special client chat command 
    - [ ] Extend the number of client chat commands
    - [ ] Add persistent storage for chat messages
