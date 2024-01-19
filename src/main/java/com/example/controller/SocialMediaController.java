package com.example.controller;

import com.example.entity.Account;
import com.example.service.AccountService;
import com.example.entity.Message;
import com.example.service.MessageService;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


import java.util.ArrayList;
import java.util.*;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */

@RestController
//@RequestMapping("/localhost:8080")
public class SocialMediaController {

    private AccountService accountService;
    private MessageService messageService;

    @Autowired
    public SocialMediaController(AccountService accountService, MessageService messageService){
        this.accountService = accountService;
        this.messageService = messageService;
    }

    @PostMapping("/register")
    public ResponseEntity<Account> registerAccount(@RequestBody Account account){
        Account loginAccount = accountService.checkAccountExistsByUsername(account.getUsername());
        if (!account.getUsername().isEmpty() && account.getPassword().length() >= 4 && loginAccount == null){
            Account newAccount = accountService.registerAccount(account.getUsername(), account.getPassword());
            return new ResponseEntity<Account>(newAccount, HttpStatus.OK);
        }
        else if (loginAccount != null){
            return new ResponseEntity<Account>(HttpStatus.CONFLICT);
        }
        else{
            return new ResponseEntity<Account>(HttpStatus.BAD_REQUEST);
        }  
    }
    
    @PostMapping("/login")
    public ResponseEntity<Account> login(@RequestBody Account account){
        //System.out.println("Output-----" + account.getUsername());
        Account loginAccount = accountService.checkAccountExistsByUsernameAndPassword(account.getUsername(), account.getPassword());
        if (loginAccount != null){
            Account newAccountLogin = accountService.loginAccount(account.getUsername(), account.getPassword());
            return new ResponseEntity<Account>(newAccountLogin, HttpStatus.OK);
        }
        else{
            return new ResponseEntity<Account>(HttpStatus.UNAUTHORIZED);
        }
    }
    
    @PostMapping(value = "/messages")
    public ResponseEntity<Message> newMessage(@RequestBody Message message){
        Account postedByAccount = accountService.checkAccountExistsById(message.getPosted_by());
        if (message.getMessage_text() != null && !message.getMessage_text().isEmpty() && message.getMessage_text().length() < 255 && postedByAccount != null){
            Message newMessage = messageService.postMessage(message.getPosted_by(), message.getMessage_text(), message.getTime_posted_epoch());
            return new ResponseEntity<Message>(newMessage, HttpStatus.OK);
        }
        else{
            return new ResponseEntity<Message>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/messages")
    public List<Message> getAllMessages(){
        return messageService.getAllMessages();
    }

    @GetMapping("/messages/{message_id}")
    public ResponseEntity<Message> getMessageByID(@PathVariable int message_id){
        return new ResponseEntity<Message>(messageService.getMessageByID(message_id), HttpStatus.OK);
    }

    @DeleteMapping("/messages/{message_id}")
    public ResponseEntity<Integer> deleteMessage(@PathVariable int message_id){
        int numMessagesDeleted = 0;
        numMessagesDeleted = messageService.deleteMessage(message_id);
        if(numMessagesDeleted != 0){
            return new ResponseEntity<Integer>(numMessagesDeleted, HttpStatus.OK);
        }
        else{
            return new ResponseEntity<Integer>(HttpStatus.OK);
        }
        
    }

    @PatchMapping("/messages/{message_id}")
    public ResponseEntity<Integer> updateMessageText(@PathVariable int message_id, @RequestBody Map<String, String> messageTextBody){
        String messageText = messageTextBody.get("message_text");
        Message foundMessage = messageService.getMessageByID(message_id);
        System.out.println("Output---- " + messageText.toString());

        if (messageText != null && !messageText.isBlank() && messageText.length() < 255 && foundMessage != null){
            return new ResponseEntity<Integer>(messageService.updateMessage(message_id, messageText), HttpStatus.OK);
        }
        else{
            return new ResponseEntity<Integer>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/accounts/{account_id}/messages")
    public ResponseEntity<List<Message>> getMessageByAccount(@PathVariable int account_id){
        List<Message> messageList = messageService.getAllMessagesById(account_id);
        return new ResponseEntity<List<Message>>(messageList, HttpStatus.OK);
    }
}