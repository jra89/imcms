package imcode.server.user;

import imcode.server.Imcms;
import imcode.server.document.DocumentDomainObject;
import imcode.server.document.TemplateGroupDomainObject;

import java.util.*;

public class UserDomainObject extends Hashtable {

    public UserDomainObject() {

    }

    protected int id;

    private String loginName;
    private String password;
    private String firstName = "";
    private String lastName = "";
    private String title = "";
    private String company = "";
    private String address = "";
    private String city = "";
    private String zip = "";
    private String country = "";
    private String countyCouncil = "";
    private String emailAddress = "";
    private boolean active;
    private String create_date;

    private String languageIso639_2 ;

    private TemplateGroupDomainObject templateGroup;
    private String loginType;

    private boolean imcmsExternal = false;

    private String workPhone = "";
    private String mobilePhone = "";
    private String homePhone = "";
    private String faxPhone = "";
    private String otherPhone = "";

    private Set roles = new HashSet();

    /**
     * FIXME - Kludge to get context path into template methods *
     */
    private String currentContextPath;

    /**
     * get user-id
     */
    public int getId() {
        return this.id;
    }

    /**
     * set user-id
     */
    public void setId( int id ) {
        this.id = id;
    }

    /**
     * get login name (username)
     */
    public String getLoginName() {
        return this.loginName;
    }

    /**
     * set login name (username)
     */
    public void setLoginName( String loginName ) {
        this.loginName = loginName;
    }

    /**
     * get password
     */
    public String getPassword() {
        return this.password;
    }

    /**
     * set password
     */
    public void setPassword( String password ) {
        this.password = password;
    }

    /**
     * get full name
     */
    public String getFullName() {
        return getFirstName() + " " + getLastName();
    }

    /**
     * get first name
     */
    public String getFirstName() {
        return this.firstName;
    }

    /**
     * set first name
     */
    public void setFirstName( String firstName ) {
        this.firstName = firstName;
    }

    /**
     * get last name
     */
    public String getLastName() {
        return this.lastName;
    }

    /**
     * set last name
     */
    public void setLastName( String lastName ) {
        this.lastName = lastName;
    }

    /**
     * set title
     */
    public void setTitle( String title ) {
        this.title = title;
    }

    /**
     * get title
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * set company
     */
    public void setCompany( String company ) {
        this.company = company;
    }

    /**
     * get company
     */
    public String getCompany() {
        return this.company;
    }

    /**
     * set address
     */
    public void setAddress( String address ) {
        this.address = address;
    }

    /**
     * get address
     */
    public String getAddress() {
        return this.address;
    }

    /**
     * set city
     */
    public void setCity( String city ) {
        this.city = city;
    }

    /**
     * get city
     */
    public String getCity() {
        return this.city;
    }

    /**
     * set zip
     */
    public void setZip( String zip ) {
        this.zip = zip;
    }

    /**
     * get zip
     */
    public String getZip() {
        return this.zip;
    }

    /**
     * set country
     */
    public void setCountry( String country ) {
        this.country = country;
    }

    /**
     * get country
     */
    public String getCountry() {
        return this.country;
    }

    /**
     * set county_council
     */
    public void setCountyCouncil( String countyCouncil ) {
        this.countyCouncil = countyCouncil;
    }

    /**
     * get county_council
     */
    public String getCountyCouncil() {
        return this.countyCouncil;
    }

    /**
     * Return the users e-mail address
     */
    public String getEmailAddress() {
        return this.emailAddress;
    }

    /**
     * Set the users e-mail address
     */
    public void setEmailAddress( String emailAddress ) {
        this.emailAddress = emailAddress;
    }

    /**
     * Get the users workphone
     */
    public String getWorkPhone() {
        return this.workPhone;
    }

    /**
     * Set the users workphone
     */
    public void setWorkPhone( String workphone ) {
        this.workPhone = workphone;
    }

    /**
     * Get the users mobilephone
     */
    public String getMobilePhone() {
        return this.mobilePhone;
    }

    /**
     * Set the users mobilephone
     */
    public void setMobilePhone( String mobilephone ) {
        this.mobilePhone = mobilephone;
    }

    /**
     * Get the users homephone
     */
    public String getHomePhone() {
        return this.homePhone;
    }

    /**
     * Set the users homepohne
     */
    public void setHomePhone( String homephone ) {
        this.homePhone = homephone;
    }

    /**
     * Get the users faxphone
     */
    public String getFaxPhone() {
        return this.faxPhone;
    }

    /**
     * Set the users faxpohne
     */
    public void setFaxPhone( String faxphone ) {
        this.faxPhone = faxphone;
    }

    /**
     * Get the users otherphone
     */
    public String getOtherPhone() {
        return this.otherPhone;
    }

    /**
     * Set the users otherpohne
     */
    public void setOtherPhone( String otherphone ) {
        this.otherPhone = otherphone;
    }

    /**
     * Set whether the user is allowed to log in
     */
    public void setActive( boolean active ) {
        this.active = active;
    }

    /**
     * Check whether the user is allowed to log in
     */
    public boolean isActive() {
        return this.active;
    }

    /**
     * set create_date
     */
    public void setCreateDate( String create_date ) {
        this.create_date = create_date;
    }

    /**
     * get create_date
     */
    public String getCreateDate() {
        return this.create_date;
    }

    /**
     * set template group
     */
    public void setTemplateGroup( TemplateGroupDomainObject template_group ) {
        this.templateGroup = template_group;
    }

    /**
     * get template group
     */
    public TemplateGroupDomainObject getTemplateGroup() {
        return templateGroup;
    }

    /**
     * Return the users language
     */
    public String getLanguageIso639_2() {
        return this.languageIso639_2;
    }

    /**
     * Set the users language
     */
    public void setLanguageIso639_2( String languageIso639_2 ) {
        this.languageIso639_2 = languageIso639_2;
    }

    /**
     * Get the login-type.
     */
    public String getLoginType() {
        return this.loginType;
    }

    /**
     * Set the login-type.
     */
    public void setLoginType( String loginType ) {
        this.loginType = loginType;
    }

    public boolean isImcmsExternal() {
        return this.imcmsExternal;
    }

    public void setImcmsExternal( boolean external ) {
        this.imcmsExternal = external;
    }

    public void addRole( RoleDomainObject role ) {
        roles.add( role );
    }

    public void removeRole( RoleDomainObject role ) {
        roles.remove( role );
    }

    public void setRoles( RoleDomainObject[] rolesForUser ) {
        this.roles = new HashSet( Arrays.asList( rolesForUser ) );
    }

    public boolean hasRole( RoleDomainObject role ) {
        return this.roles.contains( role );
    }

    public RoleDomainObject[] getRoles() {
        return (RoleDomainObject[])roles.toArray( new RoleDomainObject[roles.size()] );
    }

    public boolean equals( Object o ) {
        if ( this == o ) {
            return true;
        }
        if ( !( o instanceof UserDomainObject ) ) {
            return false;
        }
        if ( !super.equals( o ) ) {
            return false;
        }

        final UserDomainObject userDomainObject = (UserDomainObject)o;

        if ( id != userDomainObject.id ) {
            return false;
        }

        return true;
    }

    public int hashCode() {
        int result = super.hashCode();
        result = 29 * result + id;
        return result;
    }

    public boolean isDefaultUser() {
        return 2 == id;
    }

    public boolean isSuperAdmin() {
        return hasRole( RoleDomainObject.SUPERADMIN );
    }

    public boolean isUserAdmin() {
        return hasRole( RoleDomainObject.USERADMIN );
    }

    public boolean canEdit( DocumentDomainObject document ) {
        return Imcms.getServices().getDocumentMapper().userHasMoreThanReadPermissionOnDocument( this, document );
    }

    public boolean canAccess( DocumentDomainObject document ) {
        return Imcms.getServices().getDocumentMapper().userHasAtLeastDocumentReadPermission( this, document );
    }

    public boolean canList( DocumentDomainObject documentDomainObject ) {
        return documentDomainObject.isPublishedAndNotArchived()
               && canAccess( documentDomainObject )
               || canEdit( documentDomainObject );
    }

    public String toString() {
        return "(user " + id + " \"" + loginName + "\")";
    }

    /* FIXME: Current context path should be sent in a HttpServletRequest, not in an UserDomainObject. */
    public void setCurrentContextPath( String currentContextPath ) {
        this.currentContextPath = currentContextPath;
    }

    public String getCurrentContextPath() {
        return currentContextPath;
    }

}
