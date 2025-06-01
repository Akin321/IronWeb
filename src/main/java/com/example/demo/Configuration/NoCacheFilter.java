package com.example.demo.Configuration;

import java.io.IOException;

import org.springframework.stereotype.Component;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class NoCacheFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		// TODO Auto-generated method stub
		HttpServletResponse httpResponse =(HttpServletResponse) response;
		
		httpResponse.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
		 httpResponse.setHeader("Pragma", "no-cache");
	        httpResponse.setDateHeader("Expires", 0);

	        chain.doFilter(request, response);
	}

}
