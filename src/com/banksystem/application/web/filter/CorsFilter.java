package com.banksystem.application.web.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CorsFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException,ServletException{
        HttpServletRequest request =(HttpServletRequest) req;
        request.setCharacterEncoding("UTF-8");
        HttpServletResponse response=(HttpServletResponse) res;
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Access-Control-Allow-Origin","*");
        response.setHeader("Access-Control-Allow-Methods","POST,GET,OPTIONS,DELETE");
        response.setHeader("Access-Control-Max-Age","3600");
        response.setHeader("Access-Control-Allow-Headers","Origin,X-Requested-With,Content-Type,Accept,client_id,uuid,Authorization,token");
        chain.doFilter(req,res);
    }

}
