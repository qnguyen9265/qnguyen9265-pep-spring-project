package com.example.message;

import com.example.message.Message;
import com.example.message.MessageRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.*;

@Service
@Transactional
public class MessageService {
    
    MessageRepository messageRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository){
        this.messageRepository = messageRepository;
    }

    public Message postMessage(int postedById, String messageText, long timePosted){
        messageRepository.newMessage(postedById, messageText, timePosted);
        Message newMessage = messageRepository.findMessage(postedById, messageText, timePosted);
        messageRepository.save(newMessage);
        return newMessage;
    }

    public List<Message> getAllMessages(){
        return messageRepository.getAllMessages();
    }

    public Message getMessageByID(int id){
        return messageRepository.getMessageByID(id);
    }

    public Message deleteMessage(int id){
        return messageRepository.deleteMessage(id);
    }

    public Message updateMessage(int id, String messageText){
        return messageRepository.updateMessage(messageText, id);
    }

    public List<Message> getAllMessagesById(int accountId){
        return messageRepository.getMessageByAccount(accountId);
    }
}
