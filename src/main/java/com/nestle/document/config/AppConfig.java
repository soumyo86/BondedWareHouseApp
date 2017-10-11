package com.nestle.document.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import com.nestle.document.dao.LoginDao;
import com.nestle.document.dao.LoginDaoImpl;
import com.nestle.document.dao.RejectionCodeDao;
import com.nestle.document.dao.RejectionCodeDaoImpl;;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.nestle.document")
public class AppConfig extends WebMvcConfigurerAdapter {

	@Bean
	public ViewResolver viewResolver() {
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setViewClass(JstlView.class);
		viewResolver.setPrefix("/WEB-INF/views/");
		viewResolver.setSuffix(".jsp");

		return viewResolver;
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
	}

	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	}

	@Bean
	public DataSource getDataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("com.mysql.jdbc.Driver");
		dataSource.setUrl("jdbc:mysql://us-cdbr-iron-east-03.cleardb.net:3306/ad_9cda8d74b4c1eaa");
		dataSource.setUsername("b430c1716087bc");
		dataSource.setPassword("aa1f8531");

		return dataSource;
	}

	// initialize logindaoimpl bean

	@Bean
	public LoginDao getLoginDao() {
		return new LoginDaoImpl(getDataSource());
	}

	@Bean
	public RejectionCodeDao getRejectionCodeDao() {
		return new RejectionCodeDaoImpl(getDataSource());
	}
}

/*
 * @Bean public LoginService getLoginService() { return new LoginServiceImpl();
 * }
 */
