package edu.cau.cn.pondwarning.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

@Configuration
@MapperScan(
        basePackages = "edu.cau.cn.pondwarning.dao.wqdb",
        sqlSessionFactoryRef = "sqlSessionFactorySlave1",
        sqlSessionTemplateRef = "sqlSessionTemplateSlave1")
public class Slave1Config {

    private DataSource slave1DataSource;

    public Slave1Config(@Qualifier("slave1DataSource") DataSource slave1DataSource) {
        this.slave1DataSource = slave1DataSource;
    }

    @Bean
    public SqlSessionFactory sqlSessionFactorySlave1() throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        //设置数据源
        bean.setDataSource(slave1DataSource);
        //扫描映射文件
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().
                getResources("classpath:mapper/wqdb/*.xml"));
        //扫描实体类
        bean.setTypeAliasesPackage("edu.cau.cn.pondwarning.entity.wqdb");
        return bean.getObject();
    }

    @Bean
    public SqlSessionTemplate sqlSessionTemplateSlave1() throws Exception {
        return new SqlSessionTemplate(sqlSessionFactorySlave1());
    }
}
