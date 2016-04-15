package imcode.server.user.saml2;

import imcode.server.Config;
import imcode.server.Imcms;
import org.opensaml.saml2.metadata.EntityDescriptor;
import org.opensaml.saml2.metadata.IDPSSODescriptor;
import org.opensaml.saml2.metadata.SingleLogoutService;
import org.opensaml.saml2.metadata.SingleSignOnService;
import org.opensaml.saml2.metadata.provider.HTTPMetadataProvider;
import org.opensaml.saml2.metadata.provider.MetadataProviderException;
import org.opensaml.xml.parse.BasicParserPool;

import java.util.Iterator;
import java.util.Map;

public class FilterConfig {
	public static final String EXCLUDED_URL_PATTERN_PARAMETER = "excludedUrlPattern";
	public static final String SP_ACS_URL_PARAMETER = "acsUrl";
	public static final String SP_ID_PARAMETER = "spProviderId";
	public static final String SP_LOGOUT_URL_PARAMETER = "logoutUrl";
	public static final String AUTHENTICATION_METHOD_NAME_PROP = "cgi";
	private String excludedUrlPattern;
	private String acsUrl;
	private String spProviderId;
	private String logoutUrl;
	private String idpSSOLoginUrl;
	private String idpSSOLogoutUrl;
	private boolean isEnabled;

	public FilterConfig(javax.servlet.FilterConfig config) {
		Config serverConfig = Imcms.getServices().getConfig();
		Map configurationMap = serverConfig.getAuthenticationConfiguration();
		isEnabled = configurationMap.containsKey(AUTHENTICATION_METHOD_NAME_PROP);
		excludedUrlPattern = config.getInitParameter(EXCLUDED_URL_PATTERN_PARAMETER);
		spProviderId = serverConfig.getServerName();
		acsUrl = this.spProviderId + config.getServletContext().getContextPath() + "/acs";
		logoutUrl = config.getServletContext().getContextPath() + "/samlv2/logout";
		if (isEnabled) {
			try { // code was decompiled because some guy made deploy but forgot to commit...
				HTTPMetadataProvider provider = new HTTPMetadataProvider(serverConfig.getCgiMetadataUrl(), 99999999);
				provider.setParserPool(new BasicParserPool());
				provider.initialize();
				EntityDescriptor entityDescriptor = (EntityDescriptor) provider.getMetadata();
				IDPSSODescriptor idpssoDescriptor = entityDescriptor.getIDPSSODescriptor("urn:oasis:names:tc:SAML:2.0:protocol");
				Iterator services = idpssoDescriptor.getSingleSignOnServices().iterator();
				if (services.hasNext()) {
					SingleSignOnService singleLogoutService = (SingleSignOnService) services.next();
					this.idpSSOLoginUrl = singleLogoutService.getLocation();
				}

				services = idpssoDescriptor.getSingleLogoutServices().iterator();
				if (services.hasNext()) {
					SingleLogoutService singleLogoutService1 = (SingleLogoutService) services.next();
					this.idpSSOLogoutUrl = singleLogoutService1.getLocation();
				}
			} catch (MetadataProviderException e) {
				e.printStackTrace();
			}
		}
	}

	public String getExcludedUrlPattern() {
		return this.excludedUrlPattern;
	}

	public String getSpProviderId() {
		return this.spProviderId;
	}

	public String getIdpSSOLoginUrl() {
		return this.idpSSOLoginUrl;
	}

	public String getLogoutUrl() {
		return this.logoutUrl;
	}

	public String getAcsUrl() {
		return this.acsUrl;
	}

	public Boolean isEnabled() {
		return this.isEnabled;
	}

	public String getIdpSSOLogoutUrl() {
		return this.idpSSOLogoutUrl;
	}

	public void setIdpSSOLogoutUrl(String idpSSOLogoutUrl) {
		this.idpSSOLogoutUrl = idpSSOLogoutUrl;
	}
}
