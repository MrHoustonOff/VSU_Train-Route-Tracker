package com.trains.ui.servlet;

import com.trains.repository.impl.jdbc.DatabaseConnection;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class DatabaseInitializer implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("Веб-приложение запускается. Инициализация базы данных...");
        DatabaseConnection.initDatabase();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("Веб-приложение останавливается.");
    }
}
