package ksmart39.mybatis.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import ksmart39.mybatis.interceptor.ConmonInterceptor;
import ksmart39.mybatis.interceptor.LoginInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer{

	private final ConmonInterceptor conmonInterceptor;
	private final LoginInterceptor loginInterceptor;
	
	public WebConfig(ConmonInterceptor conmonInterceptor, LoginInterceptor loginInterceptor) {
		this.conmonInterceptor = conmonInterceptor;
		this.loginInterceptor = loginInterceptor;
	}
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		
		/*registry 뭔가에 설정파일 등록한다. Interceptor를 등록을 한다.
		 * handerInterceptor 상속을 받아서 구현한 클래스를 been으로 등록을 시켜주고, InterceptorRegistry에 넣어줘*/
		
		registry.addInterceptor(conmonInterceptor)
				.addPathPatterns("/**")/*어디까지 지정을 할것이냐*/
				.excludePathPatterns("/css/**") /*어떤 URL을 뺼래?*/
				.excludePathPatterns("/favaicon.ico") /*어떤 URL을 뺼래?*/
				.excludePathPatterns("/js/**"); /*어떤 URL을 뺼래?*/
		/*localhost 이후 주소부터 잘 명시를 해줘야한다.*/
		
		registry.addInterceptor(loginInterceptor)
				.addPathPatterns("/**")
				.excludePathPatterns("/")
				.excludePathPatterns("/addMember")
				.excludePathPatterns("/memberIdCheck")
				.excludePathPatterns("/login")
				.excludePathPatterns("/css/**")
				.excludePathPatterns("/favaicon.ico") /*어떤 URL을 뺼래?*/
				.excludePathPatterns("/js/**"); /*어떤 URL을 뺼래?*/
				
		
		
		WebMvcConfigurer.super.addInterceptors(registry);/*최종적으로 이런걸 시켜주겠다.*/
	}
}
