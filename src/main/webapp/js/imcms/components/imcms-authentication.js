/**
 * @author Serhii Maksymchuk from Ubrainians for imCode
 * 13.07.18
 */
define('imcms-authentication', ['imcms-auth-providers-rest-api'], function (authProvidersAPI) {

    return {
        getAuthProviders: () => authProvidersAPI.read()
    };
});
