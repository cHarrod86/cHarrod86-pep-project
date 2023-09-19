package Service;

import java.util.List;

import DAO.AccountDAO;
import DAO.MessageDAO;
import Model.Message;

public class MessageService{
    
    MessageDAO messageDAO = MessageDAO.getInstance();

    /**
     * calls the createNewMessage() method in the MessageDAO class
     * returns the newly persisted Message
     * @param m
     * @return Message
     */
    public Message createNewMessage(Message m){
        return messageDAO.createNewMessage(m);
    }

    /**
     * calls the getAllMessages method in the MessageDAO class
     * returns a list of all messages currently stored in the database
     * list is empty if there are no stored messages
     * @param m
     * @return List<Message>
     */
    public List<Message> getAllMessages(){
        return messageDAO.getAllMessages();
    }

    /**
     * calls the getMessageById() method in the MessageDAO class
     * returns the resulting Message object
     * @param id
     * @return Message
     */
    public Message getMessageById(int id){
        return messageDAO.getMessageById(id);
    }

    /**
     * calls the deleteMessageById() method in the MessageDAO class
     * returns the message to be deleted
     * @param id
     * @return Message
     */
    public Message deleteMessageById(int id){
        return messageDAO.deleteMessageByID(id);
    }

    /**
     * calls the updateMessageTextById() method in the MessageDAO class
     * returns the update message
     * @param id, messageText
     * @return Message
     */
    public Message updateMessageTextById(int id, String messageText){
        return messageDAO.updateMessageTextById(id, messageText);
    }

    /**
     * calls the getAllMessagesFromUser() method in the MessageDAO class
     * returns a list of all messages with the passed accountId
     * list is empty if no messages with accountId
     * @param accountId
     * @return List<Message>
     */
    public List<Message> getAllMessagesForUser(int accountId){
        return messageDAO.getAllMessagesFromUser(accountId);
    }
}