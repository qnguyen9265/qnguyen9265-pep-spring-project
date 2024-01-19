package com.example.repository;

import com.example.entity.Message;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer>{
    
    @Query(value = "SELECT * FROM Message WHERE posted_by = :postedByVar AND message_text = :messageTextVar AND time_posted_epoch = :timePostedVar", nativeQuery = true)
    Message findMessage(@Param("postedByVar") int posted_by, @Param("messageTextVar") String message_text, @Param("timePostedVar") long time_posted_epoch);

    @Modifying
    @Query(value = "INSERT INTO Message (posted_by, message_text, time_posted_epoch) VALUES (:idVar, :messageTextVar, :timePostedVar)", nativeQuery = true)
    void newMessage(@Param("idVar") int postedById, @Param("messageTextVar") String messageText, @Param("timePostedVar") long timePosted);

    @Query(value = "SELECT * FROM Message", nativeQuery = true)
    List<Message> getAllMessages();

    @Query(value = "FROM Message WHERE message_id = :messageIdVar")
    Message getMessageByID(@Param("messageIdVar") int id);

    @Modifying
    @Query(value = "DELETE FROM Message WHERE message_id = :messageIdVar", nativeQuery = true)
    int deleteMessage(@Param("messageIdVar") int id);

    @Modifying
    @Query(value = "UPDATE Message SET message_text = :messageTextVar WHERE message_id = :idVar", nativeQuery = true)
    int updateMessage(@Param("messageTextVar") String messageText, @Param("idVar") int id);

    @Query(value = "SELECT * FROM Message WHERE posted_by = :accountIdVar", nativeQuery = true)
    List<Message> getMessageByAccount(@Param("accountIdVar") int accountId);
}