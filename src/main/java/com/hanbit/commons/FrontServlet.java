package com.hanbit.commons;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

//import com.mysql.jdbc.StringUtils;


public class FrontServlet extends HttpServlet {	
	
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String uri = request.getRequestURI();
		
		if("/favicon.icon".equals(uri)){
			response.sendError(404,"not exist yet");
			return;
		}
		//FilenameUtils.get
		//StringUtils.split
		
		String extension = FilenameUtils.getExtension(uri);
		
		if("jpg".equals(extension)){

			response.setContentType("image/jpeg");
			
			try{
				File image = new File(getClass().getClassLoader().getResource("java.jpg").toURI());
				response.getOutputStream().write(FileUtils.readFileToByteArray(image));
			}
			catch(Exception e){
			
			}
			response.flushBuffer();
			return;
		}
		else{
			// uri -> /hanbit/jsp -> HanbitController.jsp();
			
			
			String[] uriPatterns = StringUtils.split(uri,"/");
			String className = "com.hanbit.controller.";
			
			for(int i=0; i<uriPatterns.length-1; i++){
				className += StringUtils.capitalize(uriPatterns[i]);
			}
			className += "Controller";
			
			String methodName = uriPatterns[uriPatterns.length-1];
			
			try{
				
				Class controllerClass = getClass().getClassLoader().loadClass(className);
				Method method = controllerClass.getMethod(methodName); 
				Object returnValue = method.invoke(controllerClass.newInstance());
				
				response.setContentType("text/html");
				response.getOutputStream().println("result:" + returnValue);
				response.flushBuffer();
				return;
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
		
		//response.setContentType("text/html");
		//response.flushBuffer();
	}

}
