package ro.ubbcluj.cs;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.*;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by hlupean on 16-Nov-16.
 */


public class CORSFilter extends OncePerRequestFilter
{
    static final String ORIGIN = "Origin";
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException
    {
        System.out.println(request.getHeader(ORIGIN));
        System.out.println(request.getMethod());
        if (request.getHeader(ORIGIN).equals("null"))
        {
            String origin = request.getHeader(ORIGIN);
            response.setHeader("Access-Control-Allow-Origin", "*");//* or origin as u prefer
            response.setHeader("Access-Control-Allow-Credentials", "true");
            response.setHeader("Access-Control-Allow-Headers",
                    request.getHeader("Access-Control-Request-Headers"));
        }
        
        if (request.getMethod().equals("OPTIONS"))
        {
            try
            {
                response.getWriter().print("OK");
                response.getWriter().flush();
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        } 
        else
        {
            filterChain.doFilter(request, response);
        }
    }
}

//@Component
//@Order(Ordered.HIGHEST_PRECEDENCE)
//public class CORSFilter implements Filter
//{
//    
//    @Override
//    public void init(FilterConfig filterConfig) throws ServletException
//    {
//    }
//    
//    @Override
//    public void doFilter(ServletRequest request, ServletResponse res, FilterChain chain) throws IOException, ServletException
//    {
//        HttpServletResponse response = (HttpServletResponse) res;
//        response.setHeader("Access-Control-Allow-Origin", "*");
//        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
//        response.setHeader("Access-Control-Max-Age", "3600");
//        response.setHeader("Access-Control-Allow-Headers", "x-requested-with");
//        chain.doFilter(request, response);
//    }
//
//    public void destroy()
//    {
//    }
//}
