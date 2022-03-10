package ie;

import ie.exception.CustomException;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class IemdbContext implements ServletContextListener {
    private Iemdb iemdb;
    public void contextInitialized(ServletContextEvent event) {
        iemdb = new Iemdb();
        try {
            iemdb.fetchData();
        } catch (CustomException e) {
            // TODO : handle exception
            e.printStackTrace();
        }
        // TODO : is it really necessary ?
        event.getServletContext().setAttribute("iemdb", iemdb);
    }

    public void contextDestroyed(ServletContextEvent event) {
        event.getServletContext().setAttribute("iemdb", null);
        iemdb.clearDatabase();
        iemdb = null;
    }

}