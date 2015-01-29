package dk.lynspitti.Bank;

import java.io.BufferedReader;
import java.io.Console;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

enum Action {
    RegistreCustomer,EditCustomer, CreateAccount, EditAccount, Transaction, WithDraw, Deposit, ShowAccount;
    public static Action get(int i){
        return values()[i];
    }
}

//Results message
class Results {
    enum ResultType {
        Error,Accepted;
    }
    public ResultType Type;
    public String Message;
    public Results(ResultType type, String message){
        Type = type;
        Message = message;
    }
}

//Available Acount Types
enum AccountType{
    Savings, Kredit;
    public static AccountType get(int i){
        return values()[i];
    }
}

//Main System loop --- for creating a new system user or login
public class Main {
    private static String answer = "";
    private static List<User> Users = new ArrayList<User>();

    public static void main(String[] args) {
        //A static User -
        if (Users.toArray().length == 0){
            User user = new User("daniel","home","1234","admin");
            Users.add(user);
            Bank.RegistreCustomer(user);
            Bank.CreateAccount("Saving",user,AccountType.Savings,5000);
            Bank.CreateAccount("Kredit",user,AccountType.Kredit,-2000);
        }
        Boolean open = true;
        while (open) {
            System.out.flush();
            System.out.println("Hey and Welcome To Lynspitti´s bank console demo.");
            System.out.println("Are u a new user in system?");
            answer = readLine();
            //If new user
            if (answer.equals("y") || answer.equals("Y") ||
                    answer.equals("yes") || answer.equals("Yes") ||
                    answer.equals("YES")) {
                System.out.flush();
                System.out.println("What is your name?");
                String Name = readLine();
                if (!Name.equals("")){
                    System.out.flush();
                    System.out.println("What is your Address?");
                    String Address = readLine();
                    if (!Address.equals("")){
                        System.out.flush();
                        System.out.println("What is your Cpr-number?");
                        String Cpr = readLine();
                        if (!Cpr.equals("")){
                            System.out.flush();
                            System.out.println("What Password do you want?");
                            String Password = readLine();
                            User newUser = new User(Name, Address, Cpr, Password);
                            Users.add(newUser);
                            System(newUser);
                        }
                    }
                }
            }
            //If old user
            else if (answer.equals("n") || answer.equals("N") ||
                    answer.equals("no") || answer.equals("No") ||
                    answer.equals("NO")) {
                answer = "";
                do {
                    System.out.flush();
                    System.out.println("Available actions");
                    System.out.println("1. list users");
                    System.out.println("q/exit. restart console");
                    System.out.println("Please type a Username to log in.");
                    answer = readLine();
                    if (answer.equals("1")) {
                        ListUsers(null);
                        System.out.println("Press \'enter\' to continue.");
                        readLine();
                    }
                    else {
                        User user = FindUser(answer);
                        System.out.println("Enter password");
                        answer = readLine();
                        if (user.Pass.equals(answer)){
                            System(user);
                            answer="q";
                        }
                        break;
                    }
                }while (!answer.equals("q") && !answer.equals("exit"));
            }
        }
    }

    //Display Users
    private static void ListUsers(User[] notusers){
        System.out.flush();
        if (notusers == null) notusers = new User[]{};
        for (Iterator<User> i = Users.iterator(); i.hasNext(); ) {
            User user = i.next();
            boolean Not = false;
            for(int x =0;x<notusers.length;x++){
                if (notusers[x].GetInformation().split("#")[0].equals(user.GetInformation().split("#")[0]))Not=true;
            }
            if (Not)continue;
            System.out.println(user.GetInformation().split("#")[0]);
        }
    }

    //Find Specefic user in mem
    private static User FindUser(String UserName){
        for (Iterator<User> i = Users.iterator(); i.hasNext(); ) {
            User user = i.next();
            String[] UserInfo = user.GetInformation().split("#");
            if (UserInfo[0].equals(UserName)){
                return user;
            }
        }
        return null;
    }

    //User System
    public static void System(User user){
        answer = "";
        do {
            System.out.flush();
            //if rerun
            if (answer != ""){
                answer="";
                System.out.println("Press \'enter\' to continue.");
                readLine();
            }
            //display all actions
            System.out.println("Available actions");
            int i = 1;
            for (Action d : Action.values()) {
                System.out.println(i+". "+d);
                i++;
            }
            System.out.println("q/exit. restart console");
            answer = readLine();

            //Do action
            try{
                Action action = Action.get(Integer.parseInt(answer)-1);

                Boolean run = true;
                List<Object> arg = new ArrayList<Object>();
                do {
                    //Try do action in bank... with current arg
                    Results res = user.ContactBank(action, arg.toArray());
                    //If result from action contains error
                    if (res.Type == Results.ResultType.Error){
                        //If Error is missing a amount param
                        if (res.Message.equals("Missing Amount")){
                            System.out.println("Please enter an amount");
                            arg.add(Double.parseDouble(readLine()));
                        }
                        //If Error is missing From or to account param
                        else if (res.Message.equals("From which Account") || res.Message.equals("To which Account")){
                            String Answer2 = "";
                            //Display message
                            if (res.Message.equals("From which Account")) {
                                    //System.out.println("Please choose a account");
                            }
                            else {
                                System.out.println("To own account or an other user?");
                                Answer2 = readLine();
                            }
                            //If get param to user´s own account
                            if (Answer2.equals(1) || Answer2.equals("own") || res.Message.equals("From which Account")){
                                System.out.println("Please choose a account");
                                List<BankAccount> Accounts = Bank.CustomerAccounts(user);
                                i=1;
                                for (Iterator<BankAccount> x = Accounts.iterator(); x.hasNext(); ) {
                                    BankAccount Account = x.next();
                                    System.out.println(i+". "+Account.AccountName);
                                    i++;
                                }
                                int Answer3 = Integer.parseInt(readLine())-1;
                                if (Accounts.toArray().length > Answer3) arg.add(Accounts.toArray()[Answer3]);
                            }
                            //If get param to other user´s account
                            else if (Answer2.equals(2) || Answer2.equals("other")){
                                System.out.println("1. list users");
                                System.out.println("Please type a Username");
                                String answer3 = readLine();
                                while(answer3.equals("1")){
                                    ListUsers(new User[]{user});
                                    answer3 = readLine();
                                }
                                User username = FindUser(answer3);
                                if (username != null){
                                    List<BankAccount> Accounts;
                                    int Answer3;
                                    Accounts = Bank.CustomerAccounts(username);
                                    i=1;
                                    for (Iterator<BankAccount> x = Accounts.iterator(); x.hasNext(); ) {
                                        BankAccount Account = x.next();
                                        System.out.println(i+". "+Account.AccountName);
                                        i++;
                                    }
                                    Answer3 = Integer.parseInt(readLine())-1;
                                    if (Accounts.toArray().length > Answer3) arg.add(Accounts.toArray()[Answer3]);
                                }
                            }
                        }
                        //If Error is missing a name param
                        else if (res.Message.equals("Name")){
                            System.out.println("Please type a Name");
                            arg.add(readLine());
                        }
                        //If Error is missing AccountType param
                        else if (res.Message.equals("Which AccountType")){
                            System.out.println("Available AccountTypes");

                            int x = 1;
                            for (AccountType d : AccountType.values()) {
                                System.out.println(x+". "+d);
                                x++;
                            }

                            System.out.println("Please choose an Account Type");
                            String answer2 = readLine();
                            arg.add(AccountType.get(Integer.parseInt(answer2) - 1));
                        }
                        //Else an unknown error --- display error message
                        else {
                            System.out.println(res.Message);
                            run = false;
                        }
                    }
                    //If actions from action is Accepted
                    else{
                        System.out.println(res.Message);
                        run = false;
                    }
                }while(run);
            }catch (Exception ex){
                // if any error occurs
                //ex.printStackTrace();
            }
        } while (!(answer.equals("q")) && !(answer.equals("exit")));
    }

    //Read Console line
    public static final String readLine()
    {
        try{
            Console console = System.console();
            if (console != null)
            {
                return console.readLine();
            }
            else
            {
                return new BufferedReader(new InputStreamReader(System.in)).readLine();
            }
        }catch(Exception ex){
            // if any error occurs
            ex.printStackTrace();
        }
        return  "";
    }
}