package org.vidulkeyt.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.oltu.oauth2.client.OAuthClient;
import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.response.GitHubTokenResponse;
import org.apache.oltu.oauth2.client.response.OAuthAuthzResponse;
import org.apache.oltu.oauth2.common.OAuthProviderType;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Servlet implementation class AuthServlet
 */
@WebServlet("/github/auth")
public class GitHubAuthServlet extends HttpServlet {
    private static final long serialVersionUID = -7531059242414695169L;

    final static Logger logger = LoggerFactory.getLogger(GitHubAuthServlet.class);

    /**
     * @see HttpServlet#HttpServlet()
     */
    public GitHubAuthServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    logger.debug("Got callback request " + request);
	    OAuthAuthzResponse oar;
        try {
            oar = OAuthAuthzResponse.oauthCodeAuthzResponse(request);
            String code = oar.getCode();
            logger.debug("Auth code: " + code);

            OAuthClientRequest tokenReq = OAuthClientRequest
                    .tokenProvider(OAuthProviderType.GITHUB)
                    .setGrantType(GrantType.AUTHORIZATION_CODE)
                    .setClientId("730d694f9478dc921d40")
                    .setClientSecret("765dd1c85ae46466f53423833f7e28854af8ac6d")
                    .setRedirectURI("http://helloworld2-vidulkeyt.rhcloud.com/github/auth")
                    .setCode(code)
                    .buildQueryMessage();
     
                OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());

                GitHubTokenResponse oAuthResponse = oAuthClient.accessToken(tokenReq, GitHubTokenResponse.class);
     
                String accessToken = oAuthResponse.getAccessToken();
                Long expiresIn = oAuthResponse.getExpiresIn();
                logger.info(String.format("Got access token '%s' which expires in %d", accessToken, expiresIn));
        } catch (Exception e) {
            logger.error("Error while redirect", e);
            throw new ServletException("OAuth exception", e);
        }
	}

}
