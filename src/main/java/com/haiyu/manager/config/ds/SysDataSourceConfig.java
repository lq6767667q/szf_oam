package com.haiyu.manager.config.ds;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

@Configuration
// 扫描 Mapper 接口并容器管理
@MapperScan(basePackages = SysDataSourceConfig.PACKAGE, sqlSessionFactoryRef = "sysSqlSessionFactory")
public class SysDataSourceConfig {

    // 精确到 sys 目录，以便跟其他数据源隔离
    static final String PACKAGE = "com.haiyu.manager.dao.sys";
    static final String MAPPER_LOCATION = "classpath:mapper/sys/*.xml";

    private static String MYBATIS_CONFIG = "mybatis-config.xml";

    @Value("${sys.datasource.url}")
    private String url;

    @Value("${sys.datasource.username}")
    private String user;

    @Value("${sys.datasource.password}")
    private String password;

    @Value("${sys.datasource.driverClassName}")
    private String driverClass;

    @Bean(name = "sysDataSource")
    public DataSource sysDataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(driverClass);
        dataSource.setUrl(url);
        dataSource.setUsername(user);
        dataSource.setPassword(password);
        return dataSource;
    }

    @Bean(name = "sysTransactionManager")
    public DataSourceTransactionManager sysTransactionManager() {
        return new DataSourceTransactionManager(sysDataSource());
    }

    @Bean(name = "sysSqlSessionFactory")
    public SqlSessionFactory sysSqlSessionFactory(@Qualifier("sysDataSource") DataSource sysDataSource)
            throws Exception {
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(sysDataSource);
        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources(SysDataSourceConfig.MAPPER_LOCATION));
//        sessionFactory.setConfigLocation(new ClassPathResource(MYBATIS_CONFIG));
        return sessionFactory.getObject();
    }
}