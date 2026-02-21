package com.orangehrm.utilities;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

public class LoggerManager {

    // this method return a logger instance for the provided class
    public static Logger getLogger(Class<?> clazz) {
        return (Logger) LogManager.getLogger();
    }
}
