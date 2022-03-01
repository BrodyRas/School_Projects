import java.util.ArrayList;
import java.util.List;

public class main {

    public static void main(String[] args){
        List<Transaction> transactionList = new ArrayList<>();
        transactionList.add(new Transaction("Netflix", 9.99, 10));
        transactionList.add(new Transaction("Netflix", 9.99, 20));
        transactionList.add(new Transaction("Netflix", 9.99, 30));
        transactionList.add(new Transaction("Amazon", 27.12, 32));
        transactionList.add(new Transaction("Sprint", 50.11, 45));
        transactionList.add(new Transaction("Sprint", 50.11, 55));
        transactionList.add(new Transaction("Sprint", 50.11, 65));
        transactionList.add(new Transaction("Sprint", 60.13, 77));

        TransactionChecker checker = new TransactionChecker();

        System.out.println(checker.check(transactionList));
    }
}
