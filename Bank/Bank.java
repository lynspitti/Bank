package dk.lynspitti.Bank;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by DBJ on 28-01-2015.
 */
public final class Bank {
    private static ArrayList<ArrayList<Object>> AccountList = new ArrayList<ArrayList<Object>>();

    //Verify what customer is registred in system
    private static int VerifyCustomer(User user){
        return VerifyCustomer(user.GetInformation());
    }
    private static int VerifyCustomer(String UserInformation){
        for (int i = 0;i <AccountList.toArray().length; i++){
            if (((User)AccountList.get(i).get(0)).GetInformation().equals(UserInformation)){
                return i;
            }
        }
        return -1;
    }

    //new Customer
    public static Results RegistreCustomer(User user){
        if (VerifyCustomer(user.GetInformation()) == -1){
            ArrayList<Object> newuser = new ArrayList<Object>();
            newuser.add(user);
            AccountList.add(newuser);
            //Customers.add(user);
            return new Results(Results.ResultType.Accepted,"U have been registred in our system");
        }
        return new Results(Results.ResultType.Error,"We are sry but a user have already registred in this name");
    }
    public static Results EditCustomer(User user){
        return new Results(Results.ResultType.Accepted,"This function is temporarily down");
    }

    //New Account
    public static Results CreateAccount(String AccountName,User user, AccountType account, double Balance) {
        int UserId = VerifyCustomer(user);
        if (UserId == -1) return new Results(Results.ResultType.Error, "U are not a user in our system");
        BankAccount Acc = null;
        switch (account) {
            case Savings:
                Acc = new SavingsAccount(AccountName, UserId, Balance);
                break;
            case Kredit:
                Acc = new KreditAccount(AccountName, UserId, Balance);
                break;
        }
        if (Acc == null) return new Results(Results.ResultType.Error, "Sry we could not create this account");
        AccountList.get(UserId).add(Acc);
        return new Results(Results.ResultType.Accepted, "Your account have been created");
    }
    public static Results EditAccount(BankAccount account){
        return new Results(Results.ResultType.Accepted, "This function is temporarily down");
    }

    public static void ShowAccount(User user, BankAccount account){
        int UserId = VerifyCustomer(user);
        if (UserId != -1 && account.OwnerId == UserId) {
            account.ShowAccount();
        }
    }

    //Find All Account owned by Customer
    public static List<BankAccount> CustomerAccounts(User user){
        List<BankAccount> Result = new ArrayList<BankAccount>();
        int UserId = VerifyCustomer(user);
        for (int i = 1; i < AccountList.get(UserId).toArray().length; i++){
            Result.add((BankAccount)AccountList.get(UserId).get(i));
        }
        return Result;
    }

    public static Results Transaction(User user, BankAccount FromAcc, BankAccount ToAcc, double Amount){
        int UserId = VerifyCustomer(user);
        if (UserId == -1) return new Results(Results.ResultType.Error,"You are not a user in our system");
        if (FromAcc.OwnerId != UserId) return new Results(Results.ResultType.Error,"Sry but this is´t your account");
        Results res = FromAcc.Withdrawl(Amount);
        if (res.Type == Results.ResultType.Accepted){
            res = ToAcc.Deposit(Amount);
            if (res.Type == Results.ResultType.Accepted){
                return new Results(Results.ResultType.Accepted,"Your money have been Transacted");
            }
            else return res;
        }
        else return res;
    }

    public static Results Deposit(BankAccount ToAcc, double Amount){
        return ToAcc.Deposit(Amount);
    }

    public static Results Withdrawl(User user, BankAccount FromAcc, double Amount){
        int UserId = VerifyCustomer(user);
        if (UserId == -1) return new Results(Results.ResultType.Error,"You are not a user in our system");
        if (FromAcc.OwnerId != UserId) return new Results(Results.ResultType.Error,"Sry but this is´t your account");
        return FromAcc.Withdrawl(Amount);
    }
}
