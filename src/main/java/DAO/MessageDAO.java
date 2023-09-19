package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

import Model.Message;
import Util.ConnectionUtil;

public class MessageDAO {

    static MessageDAO instance;

    Connection conn = ConnectionUtil.getConnection();

    public MessageDAO() {

    }

    //Singleton instantiation of MessageDAO
    public static MessageDAO getInstance() {
        if (instance == null)
            instance = new MessageDAO();
        return instance;
    }

    /**
     * Persists the message object to the database
     * returns null if there is no entry in Account table with the posted_by value of the passed Message object
     * returns the newly created message if it has been persisted
     * @param message
     * @return
     */
    public Message createNewMessage(Message message) {
       
        String sql = "insert into message(posted_by, message_text, time_posted_epoch) values (?,?,?)";

        Message m = null;
        try {
            PreparedStatement pStatement = conn.prepareStatement(sql);

            pStatement.setInt(1, message.getPosted_by());
            pStatement.setString(2, message.getMessage_text());
            pStatement.setLong(3, message.getTime_posted_epoch());

            pStatement.executeUpdate();

            m = this.getMessageFromUserTextAndPostedTime(message);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return m;  
    }

    /**
     * Queries the database to get the entry of the newly created Message
     * returns a null object if there is no Account entry in the database with an account_id matching the posted_by value
     * otherwise returns the resulting Message object
     * @param message
     * @return
     */
    public Message getMessageFromUserTextAndPostedTime(Message message){
        
        String sql = "select * from message where posted_by = ? and message_text = ? and time_posted_epoch = ?";

        Message m = null;

        try {
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, message.getPosted_by());
            ps.setString(2, message.getMessage_text());
            ps.setLong(3,message.getTime_posted_epoch());

            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                m = new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"), rs.getLong("time_posted_epoch"));
            }
            

        } 
        catch(SQLIntegrityConstraintViolationException e){
            return m;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return m;
    }

    /**
     * Queries the database to find the entry in the Message table with a message_id value matching the id param
     * returns null if no entry exists
     * returns a Message object otherwise
     * @param id
     * @return
     */
    public Message getMessageById(int id) {

        

        String sql = "select * from message where message_id = ?";

        Message m = null;
        // System.out.println("Message m = " + m.toString());

        try {
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();
            if(rs.next()){
            m = new Message(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getLong(4));
        }

            

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return m;
    }

    /**
     * Queries the database to get all entries in the Message table
     * returns a List of all messages in the table
     * returns a empty list if no entries in the table
     * @return
     */
    public List<Message> getAllMessages() {

        
        List<Message> messages = new ArrayList<>();

        String sql = "select * from message";

        try {
            PreparedStatement ps = conn.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                messages.add(new Message(rs.getInt("message_id"), rs.getInt("posted_by"),
                        rs.getString("message_text"), rs.getLong("time_posted_epoch")));
            }

            return messages;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return messages;
    }

    /**
     * Deletes the message entry with the given id
     * Returns the entry to be deleted
     * @param id
     * @return
     */
    public Message deleteMessageByID(int id) {

        String sql = "delete from message where message_id = ?";

        Message toBeDeleted = null;

        try {

            toBeDeleted = this.getMessageById(id);
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, id);

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return toBeDeleted;

    }

    /**
     * Updates the specified entry's message_text column with the passed message_text value
     * returns the updated Message object
     * @param id
     * @param message_text
     * @return
     */
    public Message updateMessageTextById(int id, String message_text) {

        String sql = "update message set message_text = ? where message_id = ?";

        Message updatedMessage = null;
        try {
            PreparedStatement pStatement = conn.prepareStatement(sql);

            
            pStatement.setString(1, message_text);
            pStatement.setInt(2, id);

            pStatement.executeUpdate();

            updatedMessage = getMessageById(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return updatedMessage;
    }

    /**
     * Queries the database to get all entries with a posted_by value matching the passed accountId param
     * Returns all entries of the query as a List of Messages
     * List will be returned as empty if there are no entries in the table
     * @param accountId
     * @return
     */
    public List<Message> getAllMessagesFromUser(int accountId){
        List<Message> userMessages = new ArrayList<>();

        String sql = "select * from message where posted_by = ?";

        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, accountId);

            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                userMessages.add(new Message(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getLong(4)));
            }

            return userMessages;

        } catch (SQLException e) {
            return null;
        }
    }
}
