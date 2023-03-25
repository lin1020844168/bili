package com.lin.bili.chat;

import com.lin.bili.chat.config.NettyProperties;
import com.lin.bili.chat.server.ChatServer;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class ChatApplication implements CommandLineRunner {
    @Autowired
    private ChatServer chatServer;

    @Autowired
    private NettyProperties nettyProperties;


    public static void main(String[] args) {
        SpringApplication.run(ChatApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        chatServer.start(nettyProperties.port);
    }
}
