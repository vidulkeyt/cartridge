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

import com.jcabi.github.Github;
import com.jcabi.github.Repo;
import com.jcabi.github.RtGithub;

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
	    Config cfg = Config.getInstance();
	    logger.debug("Got callback request " + request);
	    OAuthAuthzResponse oar;
        try {
            oar = OAuthAuthzResponse.oauthCodeAuthzResponse(request);
            String code = oar.getCode();
            logger.debug("Auth code: " + code);

            OAuthClientRequest tokenReq = OAuthClientRequest
                    .tokenProvider(OAuthProviderType.GITHUB)
                    .setGrantType(GrantType.AUTHORIZATION_CODE)
                    .setClientId(cfg.getClientId())
                    .setClientSecret(cfg.getClientSecret())
                    .setRedirectURI(cfg.getRedirectURI())
                    .setCode(code)
                    .buildQueryMessage();
     
                OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());

                GitHubTokenResponse oAuthResponse = oAuthClient.accessToken(tokenReq, GitHubTokenResponse.class);
     
                String accessToken = oAuthResponse.getAccessToken();
                Long expiresIn = oAuthResponse.getExpiresIn();
                logger.info(String.format("Got access token '%s' which expires in %d", accessToken, expiresIn));
                response.getWriter().format("Got access token '%s' which expires in %d\n", accessToken, expiresIn);
                Github github = new RtGithub(accessToken);
                Iterable<Repo> repos = github.repos().iterate("");
                for (Repo r: repos) {
                    response.getWriter().append(r.coordinates().toString() + "\n");
                }
        } catch (Exception e) {
            logger.error("Error while redirect", e);
            throw new ServletException("OAuth exception", e);
        }
	}

}
