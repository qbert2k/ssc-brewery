package guru.sfg.brewery.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class RestHeaderAuthFilter extends AbstractAuthenticationProcessingFilter {

    public RestHeaderAuthFilter(RequestMatcher requiresAuthenticationRequestMatcher) {
        super(requiresAuthenticationRequestMatcher);
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        if (this.logger.isDebugEnabled()) {
            this.logger.debug("Request is to process authentication");
        }

        try {
            Authentication authResult = this.attemptAuthentication(request, response);

            if (authResult != null) {
                this.successfulAuthentication(request, response, chain, authResult);
            } else {
                chain.doFilter(request, response);
            }
        } catch (AuthenticationException e) {
            log.error("Authentication Failed", e);
            unsuccessfulAuthentication(request, response, e);
        }
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
        SecurityContextHolder.clearContext();

        if (log.isDebugEnabled()) {
            log.debug("Authentication request failed: {}", failed.toString(), failed);
            log.debug("Updated SecurityContextHolder to contain null Authentication");
        }

        response.sendError(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase());
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String userName = getUserName(request);
        String password = getPassword(request);

        if (userName == null) {
            userName = "";
        }
        if (password == null) {
            password = "";
        }

        log.debug("Authentication User: {}", userName);

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userName, password);

        if (!StringUtils.isEmpty(userName) && !StringUtils.isEmpty(password)) {
            return this.getAuthenticationManager().authenticate(token);
        } else {
            return null;
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) {
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("Authentication success. Updating SecurityContextHolder to contain: " + authResult);
        }

        SecurityContextHolder.getContext().setAuthentication(authResult);
    }

    private String getUserName(HttpServletRequest request) {
        if (request.getHeader("Api-Key") != null) {
            return request.getHeader("Api-Key");
        } else {
            return request.getParameter("apiKey");
        }
    }

    private String getPassword(HttpServletRequest request) {
        if (request.getHeader("Api-Secret") != null) {
            return request.getHeader("Api-Secret");
        } else {
            return request.getParameter("apiSecret");
        }
    }
}
