package com.example.financialmanager;

import com.example.financialmanager.model.Category;
import com.example.financialmanager.storage.CategoryStorage;
import com.example.financialmanager.view.MainView;
import com.formdev.flatlaf.FlatIntelliJLaf;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.intellijthemes.FlatSolarizedLightIJTheme;
import javax.swing.*;
import java.io.IOException;
import java.util.List;

@SpringBootApplication
public class FinancialManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(FinancialManagerApplication.class, args);
        System.setProperty("java.awt.headless", "false");


        try {
            UIManager.setLookAndFeel(new FlatSolarizedLightIJTheme());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> {
            MainView mainView = new MainView();
            mainView.setVisible(true);
        });
    }
}
