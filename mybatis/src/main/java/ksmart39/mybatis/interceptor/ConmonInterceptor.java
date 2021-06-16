package ksmart39.mybatis.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;


@Component /* 인터셉터 */
public class ConmonInterceptor implements HandlerInterceptor{
	
	
	private static final Logger log = LoggerFactory.getLogger(ConmonInterceptor.class);

	
	@Override /* false 컨트롤러 가지마! true 처리하고 컨트롤러로가!  request 요청, response 응답, handler 핸들러 맵핑 되어있는거까지*/
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
			HandlerMethod method = (HandlerMethod) handler;
			log.info("ConmonInterceptor======================================================================START");
			log.info("Acess Info========================================================START");
			log.info("PORT					::::		{}" , request.getLocalPort() );
			log.info("ServerName			::::		{}" , request.getServerName() );
			log.info("Method				::::		{}" , request.getMethod() );
			log.info("URI					::::		{}" , request.getRequestURI() );
			log.info("Controller			::::		{}" , method.getBean().getClass().getSimpleName() );
			log.info("Acess Info========================================================EMD");
		return HandlerInterceptor.super.preHandle(request, response, handler);
	}
	
	@Override /*modelAndView 모델엔 뷰에 대해서     request.해서 세션영역에 접근 가능함. */
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		log.info("ConmonInterceptor======================================================================EMD");
		
		HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
	}
	
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		log.info("ConmonInterceptor======================================================================AFTER");

		HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
	}
}
