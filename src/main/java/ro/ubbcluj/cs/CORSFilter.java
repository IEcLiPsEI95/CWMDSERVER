package ro.ubbcluj.cs;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.filter.OncePerRequestFilter;

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
    private static Logger log = LogManager.getLogger(CORSFilter.class);
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException
    {
        log.info("Origin: " + request.getHeader(ORIGIN));
        log.info("Method: " + request.getMethod());
        log.info("getRequestURI: " + request.getRequestURI());
        
        if (null != request.getHeader(ORIGIN) && request.getHeader(ORIGIN).equals("null"))
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
