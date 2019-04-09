package cn.itcast.store.web.filter;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class MyRequest extends HttpServletRequestWrapper{

	private boolean encoded = false;
	public MyRequest(HttpServletRequest request) {
		super(request);
		// TODO Auto-generated constructor stub
	}
	@Override
	public String getParameter(String name) {
		String []all = getParameterValues(name);
		if(all ==null){
			return null;
		}
		return all[0];
	}
	@Override
	public String[] getParameterValues(String name) {
		// TODO Auto-generated method stub
		return getParameterMap().get(name);
	}
	@Override
	public Map<String, String[]> getParameterMap() {
		try{
			Map<String,String[]> map = super.getParameterMap();
			if(!encoded){
				if("GET".equalsIgnoreCase(super.getMethod())){
					for(Map.Entry<String, String[]> entry:map.entrySet()){
						String []allValue = entry.getValue();
						for(int i =0;i<allValue.length;i++){
							String encoding = super.getCharacterEncoding();
							if(encoding == null){
								encoding="UTF-8";
							}
							allValue[i]=new String(allValue[i].getBytes("ISO-8859-1"),encoding);
						}
					}
					encoded = true;
				}
			}
			return map;
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	

}
