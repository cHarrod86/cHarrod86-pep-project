package Controller;

import java.util.List;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import DAO.AccountDAO;
import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {

    AccountService accountService = new AccountService();
    MessageService messageService = new MessageService();
    


    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.get("example-endpoint", this::exampleHandler);

        app.post("/register",this::registerHandler);
        app.post("/login",this::loginHandler);
        app.post("/messages",this::newMessageHandler);
        app.get("/messages",this::getAllMessagesHandler);
        app.get("/messages/{message_id}",this::getMessageByIdHandler);
        app.delete("/messages/{message_id}",this::deleteMessageByIdHandler);
        app.patch("/messages/{message_id}",this::updateMessageHandler);
        app.get("/accounts/{account_id}/messages",this::getAllMessagesByAccountHandler);
        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }

    /**
     * handler for the register endpoint 
     * will take the body of the context and persist the Account object to the database 
     * @param context
     */
    private void registerHandler(Context context){
        Account account = context.bodyAsClass(Account.class);

        Account returnedAccount = accountService.registerUserAccount(account);
        if(returnedAccount == null){
            context.status(400);
        }

        else{
            context.status(200);
            context.json(returnedAccount);
        }
    }

    /**
     * handler method for the login endpoint
     * @param context
     */
    private void loginHandler(Context context){
        Account account = accountService.loginUser(context.bodyAsClass(Account.class));

        if(account == null) context.status(401);
        else{
            context.json(account);
            context.status(200);
        }
    }

    /**
     * handler method for creating new methods from the messages endpoint
     * @param context
     */
    private void newMessageHandler(Context context){

        Message message = context.bodyAsClass(Message.class);
        Message newMessage;

        if(message.getMessage_text().length() >= 255 || message.getMessage_text().isBlank()) context.status(400);
        else if(AccountDAO.getInstance().getAccountById(message.getPosted_by()) == null) context.status(400);
        else{
            newMessage = messageService.createNewMessage(message);
            context.json(newMessage, Message.class);
            context.status(200);
        }
    }

    /**
     * handler method for get request for messages endpoint
     * @param context
     */
    private void getAllMessagesHandler(Context context){
        context.json(messageService.getAllMessages(),Message.class);
    }

    /**
     * handler method for get request for messages/ {message_id}
     * @param context
     */
    private void getMessageByIdHandler(Context context){
        int messageId = Integer.parseInt(context.pathParam("message_id"));

        Message msg = messageService.getMessageById(messageId);

        context.status(200);
        if(msg == null){
            context.json("");
        }
        else context.json(msg, Message.class);
        
    }

    /**
     * handler method for delete request for messages/{message_id} endpoint
     * @param context
     */
    private void deleteMessageByIdHandler(Context context){
        int messageId =  Integer.parseInt(context.pathParam("message_id"));;
        
        Message message = messageService.deleteMessageById(messageId);
        if(message == null){
            context.json("");
        }
        else{
            context.json(message, Message.class);
            context.status(200);
        }
        
    }

    /**
     * handler method for patch request for messages/{message_id} endpoint
     * @param context
     */
    private void updateMessageHandler(Context context) throws JsonMappingException, JsonProcessingException{

        //stores the pathParam of the message_id to a variable
        int messageId = Integer.parseInt(context.pathParam("message_id"));
        
        //gets the value from the request body
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(context.body());
        String messageText = jsonNode.get("message_text").asText();

        Message msg = messageService.updateMessageTextById(messageId, messageText);
        
        if(messageText.length() >= 255 || messageText.isBlank() || msg == null){
            context.status(400);
        }
        
        else{
            context.status(200);
            context.json(msg, Message.class);
        }
    }

    /**
     * handler method for get request for accounts/{account_id}/messages endpoint
     * @param context
     */
    private void getAllMessagesByAccountHandler(Context context){
        context.json(messageService.getAllMessagesForUser(Integer.parseInt(context.pathParam("account_id"))));
    }





}