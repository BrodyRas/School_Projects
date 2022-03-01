import java.util.*;

public class TransactionChecker {
    public List<String> check(List<Transaction> transactions){
        ArrayList<String> list = new ArrayList<>();
        Map<String,List<Transaction>> transactionMap = new HashMap<>();

        for(Transaction t : transactions) {
            if(!transactionMap.containsKey(t.description)){
                transactionMap.put(t.description, new ArrayList<>());
            }
            transactionMap.get(t.description).add(t);
        }

        for (Map.Entry<String, List<Transaction>> entry : transactionMap.entrySet()) {
            if(checkRecurrency(entry.getValue())) list.add(entry.getKey());
        }

        return list;
    }

    public boolean checkRecurrency(List<Transaction> transactions){
        if(transactions.size() < 3) return false;

        double minPrice = Double.MAX_VALUE;
        double maxPrice = Double.MIN_VALUE;
        for(Transaction t : transactions) {
            if(t.price < minPrice){ minPrice = t.price; }
            if(t.price > maxPrice){ maxPrice = t.price; }
        }
        if((minPrice/maxPrice) < .8) return false;

        int minDayDiff = Integer.MAX_VALUE;
        int maxDayDiff = Integer.MIN_VALUE;
        for(int i = 0; i < transactions.size()-1; i++){
            int newDateDiff = transactions.get(i).day - transactions.get(i+1).day;
            if(newDateDiff<minDayDiff) minDayDiff = newDateDiff;
            if(newDateDiff>maxDayDiff) maxDayDiff = newDateDiff;
        }
        if(minDayDiff/maxDayDiff < .8) return false;

        return true;
    }
}
