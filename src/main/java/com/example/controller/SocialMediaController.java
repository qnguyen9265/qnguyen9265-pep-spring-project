package com.example.controller;

import com.example.account.Account;
import com.example.account.AccountService;
import com.example.message.Message;
import com.example.message.MessageService;

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
    public ResponseEntity<Account> registerAccount(@RequestParam String username, @RequestParam String password){
        Account loginAccount = accountService.checkAccountExistsByUsername(username);
        if (username != null && !username.isEmpty() && password.length() > 4 && loginAccount == null){
            Account newAccount = accountService.registerAccount(username, password);
            return new ResponseEntity<Account>(newAccount, HttpStatus.OK);
        }
        else if (loginAccount.getUsername() == username){
            return new ResponseEntity<Account>(HttpStatus.CONFLICT);
        }
        else{
            return new ResponseEntity<Account>(HttpStatus.BAD_REQUEST);
        }  
    }
    
    @GetMapping("/login")
    public ResponseEntity<Account> login(@RequestParam String username, @RequestParam String password){
        Account loginAccount = accountService.checkAccountExistsByUsernameAndPassword(username, password);
        if (loginAccount.getUsername().equals(username) && loginAccount.getPassword().equals(password)){
            Account newAccountLogin = accountService.loginAccount(username, password);
            return new ResponseEntity<Account>(newAccountLogin, HttpStatus.OK);
        }
        else{
            return new ResponseEntity<Account>(HttpStatus.UNAUTHORIZED);
        }
    }
    
    @PostMapping(value = "/messages")
    public ResponseEntity<Message> newMessage(@RequestParam int posted_by, @RequestParam String message_text, @RequestParam long time_posted_epoch){
        Account postedByAccount = accountService.checkAccountExistsById(posted_by);
        if (message_text != null && !message_text.isEmpty() && message_text.length() < 255 && postedByAccount != null){
            Message newMessage = messageService.postMessage(posted_by, message_text, time_posted_epoch);
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
    public Message getMessageByID(@PathVariable int id){
        return messageService.getMessageByID(id);
    }

    @DeleteMapping("/messages/{message_id}")
    public Message deleteMessage(@PathVariable int id){
        return messageService.deleteMessage(id);
    }

    @PatchMapping("/messages/{message_id}")
    public ResponseEntity<Message> updateMessageText(@PathVariable int id, @RequestParam String messageText){
        Message foundMessage = messageService.getMessageByID(id);
        if (!messageText.equals(null) && messageText.length() < 255 && foundMessage != null){
            Message newMessage = messageService.updateMessage(id, messageText);
            return new ResponseEntity<Message>(newMessage, HttpStatus.OK);
        }
        else{
            return new ResponseEntity<Message>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/accounts/{account_id}/messages")
    public List<Message> getMessageByAccount(@PathVariable int accountId){
        return messageService.getAllMessagesById(accountId);
    }
}
