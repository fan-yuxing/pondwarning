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
        basePackages = "edu.cau.cn.pondwarning.dao.localdb",
        sqlSessionFactoryRef = "sqlSessionFactoryMaster",
        sqlSessionTemplateRef = "sqlSessionTemplateMaster")
public class MasterConfig {

    private DataSource masterDataSource;

    public MasterConfig(@Qualifier("masterDataSource") DataSource masterDataSource) {
        this.masterDataSource = masterDataSource;
    }

    @Bean
    public SqlSessionFactory sqlSessionFactoryMaster() throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        //设置数据源
        bean.setDataSource(masterDataSource);
        //扫描映射文件
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().
                getResources("classpath:mapper/localdb/*.xml"));
        //扫描实体类
        bean.setTypeAliasesPackage("edu.cau.cn.pondwarning.entity.localdb");
        return bean.getObject();
    }

    @Bean
    public SqlSessionTemplate sqlSessionTemplateMaster() throws Exception {
        return new SqlSessionTemplate(sqlSessionFactoryMaster());
    }
}

