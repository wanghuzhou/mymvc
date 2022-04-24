package com.wanghz.mymvc.webtest.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.datasource.unpooled.UnpooledDataSourceFactory;

/**
 * HikariCP连接池
 *
 * @author whz
 * @date 2022/4/24 16:51
 */
public class HikariDataSourceFactory extends UnpooledDataSourceFactory {

    public HikariDataSourceFactory() {
        HikariConfig config = new HikariConfig("/db-hikari.properties");
        this.dataSource = new HikariDataSource(config);
    }

}
