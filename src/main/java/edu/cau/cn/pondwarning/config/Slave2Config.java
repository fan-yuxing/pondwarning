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
        basePackages = "edu.cau.cn.pondwarning.dao.qxdb",
        sqlSessionFactoryRef = "sqlSessionFactorySlave2",
        sqlSessionTemplateRef = "sqlSessionTemplateSlave2")
public class Slave2Config {

    private DataSource slave2DataSource;

    public Slave2Config(@Qualifier("slave2DataSource") DataSource slave2DataSource) {
        this.slave2DataSource = slave2DataSource;
    }

    @Bean
    public SqlSessionFactory sqlSessionFactorySlave2() throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        //设置数据源
        bean.setDataSource(slave2DataSource);
        //扫描映射文件
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().
                getResources("classpath:mapper/qxdb/*.xml"));
        //扫描实体类
        bean.setTypeAliasesPackage("edu.cau.cn.pondwarning.entity.qxdb");
        return bean.getObject();
    }

    @Bean
    public SqlSessionTemplate sqlSessionTemplateSlave2() throws Exception {
        return new SqlSessionTemplate(sqlSessionFactorySlave2());
    }
}
