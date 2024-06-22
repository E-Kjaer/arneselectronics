module sdu.group1.e_commerce_prototype {
    opens PIM.Domain to javafx.base;
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.databind;
    requires java.desktop;
    requires org.json;
    requires json.simple;
    requires mail;
    requires org.postgresql.jdbc;
    requires org.apache.pdfbox;
    requires jfreechart;
    requires jcommon;
    requires org.mongodb.bson;
    requires org.mongodb.driver.sync.client;
    requires org.mongodb.driver.core;
    //requires postgresql;
    requires org.controlsfx.controls;
    requires java.sql;

    opens PIM.Presentation to javafx.fxml;
    exports PIM.Presentation;
    exports PIM.Domain;

    opens SHOP.presentation to javafx.fxml;
    exports SHOP.presentation;

    opens SHOP.presentation.subcontrollers to javafx.fxml;
    exports SHOP.presentation.subcontrollers;

    opens SHOP.data.models to com.fasterxml.jackson.databind;
    exports SHOP.data.models;
    exports SHOP.data.dummies.PIM;
    exports SHOP.domain.order;
    opens SHOP.data.dummies.PIM to com.fasterxml.jackson.databind;

    opens OMS.Domain to com.fasterxml.jackson.databind;
    exports OMS.Domain;

    opens DAM.g1.app to javafx.fxml;
    exports DAM.g1.app;

    opens CMS to javafx.fxml;
    exports CMS;



    //----------------------------------------
    requires spring.boot.autoconfigure;
    requires com.fasterxml.jackson.core;
    requires java.naming;
    opens OMS.Presentation to javafx.fxml, com.fasterxml.jackson.databind;
    exports OMS.Presentation;

}







