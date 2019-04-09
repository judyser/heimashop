package cn.itcast.store.utils;

import javax.servlet.http.Cookie;

public class CookieUtil {
	public static Cookie getCookie(Cookie[] allCookie,String cookieName){
		if(cookieName ==null){
			return null;
		}
		if(allCookie != null){
			for(Cookie c :allCookie){
				if(cookieName.equals(c.getName()))
					return c;
			}
		}
		return null;
	}
	
}
