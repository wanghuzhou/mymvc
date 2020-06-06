package com.wanghz.mymvc.context;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * mymvc系统启动监听器
 *
 * @author wanghz
 */
public class ContextLoaderListener implements ServletContextListener {


    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ContextScanHelper.initContext();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }

}
