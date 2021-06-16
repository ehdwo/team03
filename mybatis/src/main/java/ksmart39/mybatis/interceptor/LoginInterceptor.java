package ksmart39.mybatis.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component /*been으로 등록*/
public class LoginInterceptor implements HandlerInterceptor{
	
	@Override /*요청 전*/
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		HttpSession session = request.getSession();
		String sessionId = (String) session.getAttribute("SID"); /*session.getAttribute로 가져오면 오브젝트 이기때문에 다운케스팅*/
//		String sessionLevel = (String) session.getAttribute("SLEVEL"); /*session.getAttribute로 가져오면 오브젝트 이기때문에 다운케스팅*/
		if(sessionId == null) {
			response.sendRedirect("/login");
			return false;
//		상품까지 만들고 나서 
//		}else {
//			if(sessionLevel == "1") {
//				response.sendRedirect("/");
//			}else if(sessionLevel == "2") {
//				response.sendRedirect("/");
//			}else {
//				response.sendRedirect("/");
//			}
		}
		return true;
	}
	
	@Override /*요청 후*/
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
	}
	
	@Override /*view 전*/
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub
		HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
	}
		
}
