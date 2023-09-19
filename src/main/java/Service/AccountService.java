package Service;

import DAO.AccountDAO;
import Model.Account;

public class AccountService{
    
    AccountDAO accountDAO = AccountDAO.getInstance();

    /**
     * checks if the passed account's username is blank or the password's length is less than 4 if so will return null
     * else will call the createNewAccount() method in the AccountDAO class 
     * which will return the persisted version of the acct param with all attributes(account_id, username, password)
     * @param acct
     * @return Account
     */
    public Account registerUserAccount(Account acct){

        if(acct.getUsername().isBlank() || acct.getPassword().length() < 4){
            return null;
        }

        Account accountRegistered = accountDAO.createNewAccount(acct);
        
        return accountRegistered;
    }

    /**
     * checks the validity of the provided Account param by querying the database
     * returns null if username or password of acct is invalid
     * returns an Account object if username and password are valid
     * @param acct
     * @return Account
     */
    public Account loginUser(Account acct){
        Account loginCheckerAccount = accountDAO.loginToAccount(acct);

        if(!acct.getUsername().equals(loginCheckerAccount.getUsername()) || !acct.getPassword().equals(loginCheckerAccount.getPassword())){
            return null;
        }
        else return loginCheckerAccount;

    }
}