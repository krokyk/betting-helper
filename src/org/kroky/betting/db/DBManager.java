/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kroky.betting.db;

import java.sql.DriverManager;
import java.sql.SQLException;
import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 *
 * @author Kroky
 */
public class DBManager {
    
    private static final Logger LOG = Logger.getLogger(DBManager.class);
    
    private static SessionFactory sessionFactory;
    
    public static SessionFactory getSessionFactory() {
        if(sessionFactory == null) {
            final Configuration configuration = new Configuration().configure("hibernate.cfg.xml");
            sessionFactory = configuration.buildSessionFactory();
        }
        return sessionFactory;
    }

    public static void shutdownDB() {
        if(sessionFactory != null) {
            sessionFactory.getCurrentSession().close();
            sessionFactory.getCurrentSession().disconnect();
        }
        String message = "Shutting down DB...";
        LOG.info(message);
        try {
            // the shutdown=true attribute shuts down Derby
            DriverManager.getConnection("jdbc:derby:;shutdown=true");

            // To shut down a specific database only, but keep the
            // engine running (for example for connecting to other
            // databases), specify a database in the connection URL:
            //DriverManager.getConnection("jdbc:derby:" + dbName + ";shutdown=true");
        } catch (SQLException ex) {
            if (((ex.getErrorCode() == 50000)
                    && ("XJ015".equals(ex.getSQLState())))) {
                // we got the expected exception
                message = "Derby shut down normally";
                LOG.info(message);
                // Note that for single database shutdown, the expected
                // SQL state is "08006", and the error code is 45000.
            } else {
                // if the error code or SQLState is different, we have
                // an unexpected exception (shutdown failed)
                message = "Derby did not shut down normally";
                LOG.error(message, ex);
            }
        }
    }
}
