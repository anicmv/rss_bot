package com.github.anicmv.kafka;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.concurrent.*;


/**
 * @author anicmv
 * @date 2024/7/23 20:50
 * @description kafka消费者
 */
@Log4j2
@Service
public class KafkaConsumerService {

    @Resource
    private TelegramBot telegramBot;

    private final ScheduledExecutorService scheduler;
    private final BlockingQueue<String> messageQueue;

    @Value("${bot.retry}")
    private int retry;

    public KafkaConsumerService() {
        this.scheduler = Executors.newScheduledThreadPool(4);
        this.messageQueue = new LinkedBlockingQueue<>();
    }

    @PostConstruct
    public void init() {
        scheduler.scheduleAtFixedRate(this::sendMessages, 0, 1, TimeUnit.SECONDS);
    }


    @KafkaListener(topics = "rss_bot_topic", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeMessages(String message) {
        try {
            messageQueue.put(message);
        } catch (InterruptedException e) {
            log.error("consumeMessages error", e);
            Thread.currentThread().interrupt();
        }
    }

    private void sendMessages() {
        for (int i = 0; i < 30; i++) {
            String message = messageQueue.poll();
            if (message == null) {
                break;
            }

            // 消息格式为 "chatId:message"
            String[] parts = message.split(":", 2);

            long chatId = Long.parseLong(parts[0].replaceAll("\"", ""));

            String text = parts[1];
            if (text.endsWith("\"")) {
                text = text.substring(0, text.lastIndexOf("\""));
            }
            // 发送消息到Telegram，带有重试机制
            sendTelegramMessageWithRetry(chatId, text, retry);
        }
    }

    private void sendTelegramMessageWithRetry(long chatId, String text, int retry) {
        int attempt = 0;
        boolean success = false;
        while (attempt < retry && !success) {
            attempt++;
            try {
                SendMessage sendMessage = new SendMessage(chatId, text)
                        .parseMode(ParseMode.Markdown);
                SendResponse response = telegramBot.execute(sendMessage);
                if (response.isOk()) {
                    log.info("ChatId: {}, success to send message: {}.", chatId, text);
                    success = true;
                } else {
                    log.error("ChatId: {}, failed to send message: {}. telegram bot response code: {}", chatId, text, response.errorCode());
                }
            } catch (Exception e) {
                log.error("Exception while sending message: {}. Attempt: {}", text, attempt, e);
            }
        }
        if (!success) {
            log.error("Failed to send message after {} attempts: {}", retry, text);
        }
    }
}
