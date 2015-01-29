package dk.lynspitti;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DBJ on 28-01-2015.
 */
public final class User{
    private String Name = "";
    private String Address ="";
    private String Cpr = "";
    public String Pass = "";
    private List<BankAccount> Accounts = new ArrayList<BankAccount>();

    public User(String name,String address,String cpr, String pass){
        Name = name;
        Cpr = cpr;
        Pass = pass;
        if(address!=null) Address = address;
        else Address = "Empty";
    }
    public String GetInformation(){
        return Name+"#"+Address+"#"+Cpr;
    }

    //Make User contact bank and do action
    public Results ContactBank(Action action,Object arg[]){
        switch (action) {
            case WithDraw:
                if (arg.length==0|| !(arg[0] instanceof BankAccount))return new Results(Results.ResultType.Error,"From which Account");
                if (arg.length==1|| !(arg[1] instanceof Double))return new Results(Results.ResultType.Error,"Missing Amount");
                return Bank.Withdrawl(this,(BankAccount)arg[0],(Double)arg[1]);
            case Deposit:
                if (arg.length==0|| !(arg[0] instanceof BankAccount))return new Results(Results.ResultType.Error,"To which Account");
                if (arg.length==1|| !(arg[1] instanceof Double))return new Results(Results.ResultType.Error,"Missing Amount");
                return Bank.Deposit((BankAccount)arg[0],(Double)arg[1]);
            case RegistreCustomer:
                return Bank.RegistreCustomer(this);
            case EditCustomer:
                Bank.EditCustomer(this);
                break;
            case CreateAccount:
                if (arg.length==0 || !(arg[0] instanceof String))return new Results(Results.ResultType.Error,"Name");
                if (arg.length==1 || !(arg[1] instanceof AccountType))return new Results(Results.ResultType.Error,"Which AccountType");
                if (arg.length==2 || !(arg[2] instanceof Double))return new Results(Results.ResultType.Error,"Missing Amount");
                return Bank.CreateAccount((String)arg[0],this,(AccountType)arg[1],(Double)arg[2]);
            case EditAccount:
                if (arg.length==0 || !(arg[0] instanceof BankAccount))return new Results(Results.ResultType.Error,"From which Account");
                return Bank.EditAccount((BankAccount)arg[0]);
            case Transaction:
                if (arg.length==0 || !(arg[0] instanceof BankAccount))return new Results(Results.ResultType.Error,"From which Account");
                if (arg.length==1 || !(arg[1] instanceof BankAccount))return new Results(Results.ResultType.Error,"To which Account");
                if (arg.length==2 || !(arg[2] instanceof Double))return new Results(Results.ResultType.Error,"Missing Amount");
                return Bank.Transaction(this,(BankAccount)arg[0],(BankAccount)arg[1],(Double)arg[2]);
            case ShowAccount:
                if (arg.length==0 || !(arg[0] instanceof BankAccount))return new Results(Results.ResultType.Error,"From which Account");
                Bank.ShowAccount(this,(BankAccount)arg[0]);
                break;
            default:
                return new Results(Results.ResultType.Error,"Unknown action");
        }
        return new Results(Results.ResultType.Accepted,"");
    }
}
