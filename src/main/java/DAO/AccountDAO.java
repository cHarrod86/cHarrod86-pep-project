package DAO;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

import Model.Account;
import Util.ConnectionUtil;


public class AccountDAO{

    Connection conn = ConnectionUtil.getConnection();

    static AccountDAO instance;

    public AccountDAO(){

    }

    //Singleton instantiation of AccountDAO
    public static AccountDAO getInstance(){
        if(instance == null) instance = new AccountDAO();
        return instance;
    }

    /**
     * persists the passed account to the database
     * returns null if there is an existing Account entry in the database with the same username
     * @param acct
     * @return Account
     */
    public Account createNewAccount(Account acct){
    
        String sql = "insert into account(username, password) values (? ,?)";

        try {
            PreparedStatement pStatement = conn.prepareStatement(sql);
            pStatement.setString(1, acct.getUsername());
            pStatement.setString(2, acct.getPassword());

            pStatement.executeUpdate();
            
        } 
        catch(SQLIntegrityConstraintViolationException se){
            return null;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return getAccountByUsername(acct.getUsername());
    }


    /**
     * Queries the Account table to select all columns where the username and password match the same fields on the acct param
     * if the query is successfull will return an instance of the Account class with the fields (account_id, username, password) as non default values
     * if unsuccessful the fields will be default values (0 or null)
     * @param acct
     * @return
     */
    public Account loginToAccount(Account acct){
        

        String sql = "select * from account a where a.username = ? and a.password = ?";
        Account loginChecker = new Account();
        try {
            PreparedStatement pStatement = conn.prepareStatement(sql);
            pStatement.setString(1, acct.getUsername());
            pStatement.setString(2, acct.getPassword());

            ResultSet rs = pStatement.executeQuery();

            if(rs.next()){
                loginChecker.setAccount_id(rs.getInt("account_id"));
                loginChecker.setUsername(rs.getString("username"));
                loginChecker.setPassword(rs.getString("password"));
            }

        } catch (SQLException e) {
           
            e.printStackTrace();
        }

        return loginChecker;
    }

    /**
     * Queries the Account table to find the full record for the account given the username
     * returns null if the username does not exist
     * returns an instance of Account if username exists 
     * @param username
     * @return
     */
    public Account getAccountByUsername(String username){
       
        String sql = "select * from account a where a.username = ?";

        Account a = null;
        try {
            PreparedStatement pStatement = conn.prepareStatement(sql);
            pStatement.setString(1, username);
        

            ResultSet rs = pStatement.executeQuery();

            if(rs.next()) a = new Account(rs.getInt("account_id"), rs.getString("username"), rs.getString("password"));
        

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return a;
    }

    /**
     * Queries the database to get the record of an Account given an id
     * returns null if no record exists
     * returns Account object if record exists
     * @param id
     * @return Account
     */
    public Account getAccountById(int id){
        
        Account account = null;
        String sql = "select * from account a where a.account_id = ?";

        try {
            PreparedStatement pStatement = conn.prepareStatement(sql);
            pStatement.setInt(1, id);
        

            ResultSet rs = pStatement.executeQuery();

            if(rs.next())   account =  new Account(rs.getInt("account_id"), rs.getString("username"), rs.getString("password"));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return account;
    }

    /**
     * returns a list of all accounts in the database
     * returns an empty list if there are no accounts
     * @return List<Account>
     */
    public List<Account> getAllAccounts(){
        List<Account> accounts = new ArrayList<>();
        

        String sql = "select * from account";

        try {
            PreparedStatement ps = conn.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                Account a = new Account(rs.getInt("account_id"), rs.getString("username"), rs.getString("password"));
                accounts.add(a);
            }

        } catch (SQLException e) {
            
            e.printStackTrace();
        }

        return accounts;
    }
}