package DAO;

public abstract class DAOFactory {
    private static DAOFactory instance;

    public static DAOFactory getInstance(){
        return instance;
    }
    public static void setInstance(DAOFactory f){
        instance = f;
    }
    public abstract IInitDAO makeInitDAO();
    public abstract IExpenseDAO makeExpenseDAO();
}
