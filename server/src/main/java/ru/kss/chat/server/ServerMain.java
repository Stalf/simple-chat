package ru.kss.chat.server;

import lombok.extern.slf4j.Slf4j;
import ru.kss.chat.Utils;

@Slf4j
public class ServerMain {

    public static void main(String... args) {
        log.info(Utils.testMessage());
    }

}
