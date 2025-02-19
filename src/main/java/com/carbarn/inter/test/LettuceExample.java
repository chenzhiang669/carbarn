package com.carbarn.inter.test;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;

public class LettuceExample {
    public static void main(String[] args) {
        RedisClient redisClient = RedisClient.create("redis://47.122.70.102:6379");
        StatefulRedisConnection<String, String> connection = redisClient.connect();
        RedisCommands<String , String> syncCommands = connection.sync();

        syncCommands.set("bbbb","bbb");
        String value = syncCommands.get("bbbb");
        System.out.println(value);
        connection.close();
        redisClient.shutdown();
    }
}
