package com.haiyu.manager.config.ds;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

@Configuration
// 扫描 Mapper 接口并容器管理
@MapperScan(basePackages = MngDataSourceConfig.PACKAGE, sqlSessionFactoryRef = "mngSqlSessionFactory")
public class MngDataSourceConfig {

    // 精确到 mng 目录，以便跟其他数据源隔离
    static final String PACKAGE = "com.haiyu.manager.dao.mng";
    static final String MAPPER_LOCATION = "classpath:mapper/mng/*.xml";

    private static String MYBATIS_CONFIG = "mybatis-config.xml";

    @Value("${mng.datasource.url}")
    private String url;

    @Value("${mng.datasource.username}")
    private String user;

    @Value("${mng.datasource.password}")
    private String password;

    @Value("${mng.datasource.driverClassName}")
    private String driverClass;

    @Bean(name = "mngDataSource")
    @Primary
    public DataSource mngDataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(driverClass);
        dataSource.setUrl(url);
        dataSource.setUsername(user);
        dataSource.setPassword(password);
        dataSource.setTestWhileIdle(true);
        dataSource.setValidationQuery("SELECT 1 from dual");
        return dataSource;
    }

    @Bean(name = "mngTransactionManager")
    @Primary
    public DataSourceTransactionManager mngTransactionManager() {
        return new DataSourceTransactionManager(mngDataSource());
    }

    @Bean(name = "mngSqlSessionFactory")
    @Primary
    public SqlSessionFactory mngSqlSessionFactory(@Qualifier("mngDataSource") DataSource mngDataSource)
            throws Exception {
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(mngDataSource);
        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources(MngDataSourceConfig.MAPPER_LOCATION));
        sessionFactory.setConfigLocation(new ClassPathResource(MYBATIS_CONFIG));
        return sessionFactory.getObject();
    }
}