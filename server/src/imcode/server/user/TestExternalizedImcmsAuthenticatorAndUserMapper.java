package imcode.server.user;

import org.apache.log4j.Logger;

import java.util.Arrays;

public class TestExternalizedImcmsAuthenticatorAndUserMapper extends UserBaseTestCase {
   private ExternalizedImcmsAuthenticatorAndUserMapper externalizedImcmsAndUserMapper;
   private MockIMCServiceInterface mockImcmsService;
   private ImcmsAuthenticatorAndUserMapper imcmsAuthenticatorAndUserMapper;
   private LdapUserMapper ldapUserMapper;

   public void testDummy() {
      assertTrue( true );
   }

   public void setUp() throws LdapUserMapper.LdapInitException {
      Logger logger = Logger.getLogger( this.getClass() );
      mockImcmsService = new MockIMCServiceInterface();
      String ldapServerURL = "ldap://loke:389/CN=Users,DC=imcode,DC=com";
      String ldapAuthenticationType = "simple";
      String ldapUserName = "imcode\\hasbra";
      String ldapPassword = "hasbra";
      ldapUserMapper = new LdapUserMapper( ldapServerURL, ldapAuthenticationType, ldapUserName, ldapPassword, new String[0] );
      imcmsAuthenticatorAndUserMapper = new ImcmsAuthenticatorAndUserMapper( mockImcmsService, logger );
      externalizedImcmsAndUserMapper = new ExternalizedImcmsAuthenticatorAndUserMapper( imcmsAuthenticatorAndUserMapper, new SmbAuthenticator(), ldapUserMapper, "se" );
   }

   public void testImcmsOnlyExisting() {
      mockImcmsService.addExpectedSQLProcedureCall( SPROC_GETUSERBYLOGIN, SQL_RESULT_ADMIN );

      boolean userAuthenticates = externalizedImcmsAndUserMapper.authenticate( LOGIN_NAME_ADMIN, LOGIN_NAME_ADMIN );
      assertTrue( userAuthenticates );

      mockImcmsService.addExpectedSQLProcedureCall( SPROC_GETUSERBYLOGIN, SQL_RESULT_ADMIN );
      User user = externalizedImcmsAndUserMapper.getUser( LOGIN_NAME_ADMIN );

      assertNotNull( user );
      assertTrue( user.getFirstName().equalsIgnoreCase( LOGIN_NAME_ADMIN ) );
      mockImcmsService.verify();
   }

   public void testLdapOnlyAuthentication() {
      boolean userAuthenticates = externalizedImcmsAndUserMapper.authenticate( LOGIN_NAME_HASBRA, LOGIN_NAME_HASBRA );

      assertTrue( userAuthenticates );
      mockImcmsService.verify();
   }

   public void testLdapOnlyExisting() {
      mockImcmsService.addExpectedSQLProcedureCall( SPROC_GETUSERBYLOGIN, new String[]{} );
      mockImcmsService.addExpectedSQLProcedureCall( SPROC_GETUSERBYLOGIN, SQL_RESULT_HASBRA );

      User user = externalizedImcmsAndUserMapper.getUser( LOGIN_NAME_HASBRA );

      assertTrue( "hasse".equalsIgnoreCase( user.getFirstName() ) );
      mockImcmsService.verify();
   }

   public void testLdapAndImcmsUpdateSynchronization() {
      mockImcmsService.addExpectedSQLProcedureCall( SPROC_GETUSERBYLOGIN, SQL_RESULT_HASBRA );
      mockImcmsService.addExpectedSQLProcedureCall( SPROC_GETUSERBYLOGIN, SQL_RESULT_HASBRA );
      mockImcmsService.addExpectedSQLProcedureCall( SPROC_GETUSERBYLOGIN, SQL_RESULT_HASBRA );

      User user = externalizedImcmsAndUserMapper.getUser( LOGIN_NAME_HASBRA );

      assertTrue( "hasse".equalsIgnoreCase( user.getFirstName() ) );
      mockImcmsService.verify();
   }

   public void testAlwaysExistingImcmsRole() {
      mockImcmsService.addExpectedSQLProcedureCall( SPROC_GETUSERBYLOGIN, SQL_RESULT_HASBRA );
      mockImcmsService.addExpectedSQLProcedureCall( SPROC_GETUSERBYLOGIN, SQL_RESULT_HASBRA );
      mockImcmsService.addExpectedSQLProcedureCall( SPROC_GETUSERBYLOGIN, SQL_RESULT_HASBRA );
      mockImcmsService.addExpectedSQLProcedureCall( SPROC_GETUSERROLES, new String[]{ImcmsAuthenticatorAndUserMapper.ALWAYS_EXISTING_USERS_ROLE} );

      User user = externalizedImcmsAndUserMapper.getUser( LOGIN_NAME_HASBRA );
      String[] userRoles = externalizedImcmsAndUserMapper.getRoleNames( user );

      assertTrue( Arrays.asList( userRoles ).contains( ImcmsAuthenticatorAndUserMapper.ALWAYS_EXISTING_USERS_ROLE ) );
      assertTrue( Arrays.asList( userRoles ).contains( LdapUserMapper.DEFAULT_LDAP_ROLE ) );
      mockImcmsService.verify();
   }

   public void testAddRoleFromOtherIntoImcms() {
      mockImcmsService.addExpectedSQLUpdateProcedureCall( SPROC_ROLEADDNEW );
      externalizedImcmsAndUserMapper = new ExternalizedImcmsAuthenticatorAndUserMapper( imcmsAuthenticatorAndUserMapper, new SmbAuthenticator(), ldapUserMapper, "se" );
      mockImcmsService.verify();
   }

   public void testGetRoles() {
      mockImcmsService.addExpectedSQLProcedureCall( SPROC_GETALLROLES, new String[]{"0", "Superadmin", "1", "Useradmin"} );
      String[] roles = externalizedImcmsAndUserMapper.getAllRoleNames();
      assertTrue( Arrays.asList( roles ).contains( ImcmsAuthenticatorAndUserMapper.ALWAYS_EXISTING_USERS_ROLE ) );
      assertTrue( Arrays.asList( roles ).contains( ImcmsAuthenticatorAndUserMapper.ALWAYS_EXISTING_ADMIN_ROLE ) );
      assertTrue( Arrays.asList( roles ).contains( LdapUserMapper.DEFAULT_LDAP_ROLE ) );
      mockImcmsService.verify();
   }
}
