package ksmart39.mybatis.config;


import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan(basePackages = "ksmart39.mybatis.dao", sqlSessionFactoryRef = "mybatisSqlSessionFactory")
public class MybatisConfig {

	@Bean("mybatisSqlSessionFactory")
	public SqlSessionFactory mybatisSqlSessionFactory (DataSource dateSource
													 , ApplicationContext ctx) throws Exception {
	SqlSessionFactoryBean mybatisSqlSessionFactoryBean = new SqlSessionFactoryBean();
	mybatisSqlSessionFactoryBean.setDataSource(dateSource);
	mybatisSqlSessionFactoryBean.setMapperLocations(ctx.getResources("classpath:/mapper/**/*.xml"));
	mybatisSqlSessionFactoryBean.setTypeAliasesPackage("ksmart39.mybatis.domain");
	System.out.println();
	return mybatisSqlSessionFactoryBean.getObject();
	}
	
	@Bean("SqlSessionTemplate")
	public SqlSessionTemplate mybatisSqlSessionTemplate(@Qualifier("mybatisSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
		return new SqlSessionTemplate(sqlSessionFactory);
	}
}