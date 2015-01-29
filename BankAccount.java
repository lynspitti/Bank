package dk.lynspitti;

import javax.swing.text.StyledEditorKit;
import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by DBJ on 28-01-2015.
 */

interface IAccount{
    public String LossAccount(Boolean Yearly);
}

public abstract class BankAccount implements IAccount{
    public String AccountName;
    public int OwnerId;
    private double Balance;
    private double InterestRate = 0.5;

    protected double GetBalance() {  // define getter
        return Balance;
    }

    protected void SetBalance(double newBalance) {  // define setter
        this.Balance = newBalance;
    }
    protected double GetInterestRate() {  // define getter
        return InterestRate;
    }
    protected void SetInterestRate(double newInterestRate) {  // define setter
        this.InterestRate = newInterestRate;
    }

    public BankAccount(String accountName,int UserId, double balance){
        AccountName = accountName;
        Balance = balance;
        OwnerId = UserId;
    }

    public void ShowAccount(){
        System.out.println(GetBalance());
    }

    public Results Deposit(double Amount){
        Balance+=Amount;
        return new Results(Results.ResultType.Accepted,"Your money have been Deposited.");
    }

    public Results Withdrawl(double Amount){
        Balance-=Amount;
        return new Results(Results.ResultType.Accepted,"Your money have been Withdrawn.");
    }

    public Results DepositInterests(){
        Balance = Balance+Balance*InterestRate;
        return new Results(Results.ResultType.Accepted,"The Account have been raise by the interests.");
    }
}

class SavingsAccount extends BankAccount{
    private int YearlyWithdrawls = 3;
    private int Withdrawls = 0;
    private double WithdrawlMax = 5000;

    public SavingsAccount(String AccountName, int UserId, double balance, int interestRate){
        super(AccountName,UserId,balance);
    }
    public SavingsAccount(String AccountName,int UserId, double balance){
        super(AccountName,UserId,balance);
    }
    public SavingsAccount(String AccountName,int UserId, double balance, int interestRate, int yearlyWithdrawls){
        super(AccountName,UserId,balance);
        super.SetInterestRate(interestRate);
        YearlyWithdrawls = yearlyWithdrawls;
    }

    public Results DepositInterests(){
        super.SetBalance(super.GetBalance()+super.GetBalance()*super.GetInterestRate());
        return new Results(Results.ResultType.Accepted,"The Account have been raise by the interests.");
    }

    @Override
    public Results Withdrawl(double Amount){
        if (Amount <= WithdrawlMax){
            if (Withdrawls < YearlyWithdrawls) {
                super.SetBalance(super.GetBalance() - Amount);
                Withdrawls++;
                return new Results(Results.ResultType.Accepted, "Your money have been Withdrawn.");
            }
            else return new Results(Results.ResultType.Error,"You can only Withdraw from your savings account, "+YearlyWithdrawls + " times a year.");
        }
        else return new Results(Results.ResultType.Error,"Your max Withdrawable amount is " + WithdrawlMax);
    }

    //uses Super Deposit

    @Override
    public String LossAccount(Boolean Yearly){
        if (Yearly)super.DepositInterests();
        Withdrawls = 0;
        return super.GetBalance()+"#"+super.GetInterestRate()+"#"+YearlyWithdrawls+"#"+WithdrawlMax;
    }
}

class KreditAccount extends BankAccount{
    private double installment;
    public KreditAccount(String AccountName,int UserId, Double balance){
        super(AccountName,UserId,balance);
    }

    //uses Super Withdrawl

    @Override
    public Results Deposit(double Amount){
        super.SetBalance(super.GetBalance()+Amount);
        return new Results(Results.ResultType.Accepted,"Your money have been Deposited");
    }

    @Override
    public String LossAccount(Boolean Yearly){
        if (Yearly )super.DepositInterests();
        return super.GetBalance()+"#"+super.GetInterestRate();
    }
}
