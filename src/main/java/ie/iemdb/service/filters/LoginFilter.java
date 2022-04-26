package ie.iemdb.service.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import ie.iemdb.model.DTO.ResponseDTO;
import ie.iemdb.repository.UserRepo;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class LoginFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String requestPath = request.getRequestURI();
        if (needsAuthentication(requestPath) && UserRepo.loggedInUser == null) {
            PrintWriter out = response.getWriter();
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
//            response.setStatus(401);
            out.print(new ObjectMapper().writeValueAsString(new ResponseDTO(false, "Unauthorized")));
            out.flush();
        } else {
            filterChain.doFilter(request, response);
        }
        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }

    private boolean needsAuthentication(String url) {
        return !(url.startsWith("/auth") || url.equals("/movies") || url.equals("/movies/"));
    }

}
