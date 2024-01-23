module javafx {

    requires javafx.controls;
    requires javafx.fxml;
    requires lombok;
    requires org.apache.logging.log4j;
    requires jakarta.inject;
    requires jakarta.cdi;
    requires io.vavr;
    requires jakarta.xml.bind;
    requires java.sql;
    requires commons.dbcp2;
    requires com.zaxxer.hikari;
    requires spring.jdbc;
    requires spring.tx;
    requires jakarta.annotation;
    requires jakarta.persistence;
//    requires jakarta.jakartaee.web.api;

    exports ui.main to javafx.graphics;
    exports ui.screens.principal;
    exports ui.screens.login;
    exports ui.screens.common;
    exports ui.screens.customers;
    exports ui.screens.orders;
    exports model;
    exports dao.impl;
    exports ui.screens.welcome;
    exports model.errors;
    exports model.xml;
    exports model.hibernate;


    opens model.xml to jakarta.xml.bind;
    opens model to jakarta.xml.bind;
    opens ui.screens.orders;
    opens ui.screens.login;
    opens ui.screens.principal;
    opens ui.screens.customers;
    opens ui.main;
    opens css;
    opens fxml;
    opens services;
    opens common;
    opens configFiles;
    opens model.hibernate to org.hibernate.orm.core;

    exports dao;

}
