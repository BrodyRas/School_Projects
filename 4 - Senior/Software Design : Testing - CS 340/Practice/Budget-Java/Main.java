import DAO.DAOFactory;
import DAO.IInitDAO;
import DAO.SQLDAOFactory;
import view.Navigator;
import view.main.MainView;

public class Main {

    public static void main(String[] args) {
        DAOFactory.setInstance(new SQLDAOFactory());
        IInitDAO initDAO = DAOFactory.getInstance().makeInitDAO();

        if (initDAO.initialize()) {
            System.out.println("database initialized");
            Navigator.push(MainView.class, null);
        }
    }
}
