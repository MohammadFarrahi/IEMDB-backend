package ie.filters;

import ie.Iemdb;
import ie.util.types.Constant;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebFilter("/*")
public class LoginFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String requestPath = request.getRequestURI();
        if (needsAuthentication(requestPath) && !Iemdb.isLoggedIn(null)) {
            // TODO : here we can't set proper http status code
            // TODO : after that, redirect to requested page
            response.sendRedirect(Constant.URLS.LOGIN);
        } else {
            filterChain.doFilter(request, response);
        }
    }
    private boolean needsAuthentication(String url) {
        return !Constant.URLS.NonAuthURLs.contains(url);
    }
}