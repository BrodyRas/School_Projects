package DAO;

public class SQLDAOFactory extends DAOFactory {
    private static DAOFactory instance;

    public static DAOFactory getInstance() {
        if(instance == null){
            instance = new SQLDAOFactory();
        }
        return instance;
    }

    @Override
    public IInitDAO makeInitDAO() {
        return new SQLInitDAO();
    }

    @Override
    public IExpenseDAO makeExpenseDAO() {
        return new SQLExpenseDAO();
    }
}
