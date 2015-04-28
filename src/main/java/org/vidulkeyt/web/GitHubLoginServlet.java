package org.vidulkeyt.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.common.OAuthProviderType;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Servlet implementation class OAuthTestServlet
 */
@WebServlet("/github/login")
public class GitHubLoginServlet extends HttpServlet {

    private static final long serialVersionUID = -4334482255412703992L;

    final static Logger logger = LoggerFactory.getLogger(GitHubLoginServlet.class);

       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GitHubLoginServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    Config cfg = Config.getInstance();
	    try {
	        logger.debug("Build URL to github..");
            OAuthClientRequest authReq = OAuthClientRequest
                    .authorizationProvider(OAuthProviderType.GITHUB)
                    .setClientId(cfg.getClientId())
                    .setRedirectURI(cfg.getRedirectURI())
                    .setScope("user:email")
                    .buildQueryMessage();
            logger.debug("Redirect to " + authReq.getLocationUri());
            response.sendRedirect(authReq.getLocationUri());
        } catch (OAuthSystemException e) {
            logger.error("Error while redirect", e);
            throw new ServletException("OAuth exception", e);
        }
	}

}
