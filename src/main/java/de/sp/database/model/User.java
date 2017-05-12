package de.sp.database.model;

import de.sp.database.stores.SchoolTermStore;
import de.sp.main.resources.modules.InsufficientPermissionException;
import de.sp.main.resources.modules.Permission;
import de.sp.modules.admin.AdminModule;
import de.sp.tools.string.PasswordSecurity;
import de.sp.tools.string.SaltAndHash;

import java.util.*;

public class User {

    private long id;
    private String username;
    private String name;
    private String hash;
    private String salt;
    private String languageCode;
    private Long last_selected_school_term_id = null;
    private Long school_id;
    private Boolean is_admin = false;

    private List<Role> roles = new ArrayList<>();

    private Set<String> permissions = new HashSet<>();

    /**
     * We need this parameterless constructor as only this one is called by
     * SQL2o. Without this call roleMap and permissionMap doesn't get
     * initialized.
     */
    public User() {

    }

    public User(long id, String username, String name, String hash,
                String salt, String languageCode, boolean isAdmin, long school_id) {
        super();
        this.id = id;
        this.username = username;
        this.name = name;
        this.hash = hash;
        this.salt = salt;
        this.languageCode = languageCode;
        this.is_admin = isAdmin;
        this.school_id = school_id;
    }

    public long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public String getHash() {
        return hash;
    }

    public String getSalt() {
        return salt;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public Long getSchool_id() {
        return school_id;
    }

    public Boolean is_admin() {
        return is_admin;
    }

    public List<Role> getRoles() {

        return roles;

    }

    public void addRole(Role role) {

        roles.add(role);

        registerPermissions(role);

    }

    private void registerPermissions(Role role){

        for (Permission p : role.getPermissionList()) {

            String name = p.getName();

            while (name.length() > 0) {

                permissions.add(p.getName());

                int i = name.lastIndexOf(".");

                if (i > 0) {
                    name = name.substring(0, i);
                } else {
                    name = "";
                }

            }

        }

    }

    public void registerAllPermissions(){

        permissions.clear();

        for (Role role : roles) {
            registerPermissions(role);
        }
    }

    public boolean hasPermission(String permission, Long school_id) {

        if (is_admin && permission.startsWith(AdminModule.PERMISSIONADMIN)) {
            return true;
        }

        if (school_id == null) {
            return false;
        }

        if (permission == null) {
            return true;
        }

        if (!school_id.equals(this.school_id)) {
            return false;
        }

        return permissions.contains(permission);

    }

    public boolean hasPermissionForAnySchool(String permission) {

        return permissions.contains(permission);

    }

    public boolean hasPermission(String permission, School school) {

        return hasPermission(permission, school.getId());

    }

    public SchoolTerm getLastSelectedSchoolTerm() {

        if (last_selected_school_term_id == null) {
            return null;
        }

        return SchoolTermStore.getInstance().getSchoolTerm(last_selected_school_term_id);
    }

    public void setLast_selected_school_term_id(Long last_selected_school_term_id) {
        this.last_selected_school_term_id = last_selected_school_term_id;
    }

    public Long getLast_selected_school_term_id() {
        return last_selected_school_term_id;
    }

    public void setLastSelectedSchoolTermId(Long school_term_id) {
        last_selected_school_term_id = school_term_id;
    }

    public void setLastSelectedSchoolTermIfNull() {

        if (getLastSelectedSchoolTerm() == null) {

            School school = SchoolTermStore.getInstance().getSchoolById(school_id);

            SchoolTerm st = school.getCurrentSchoolTerm();
            if (st != null) {
                last_selected_school_term_id = st.getId();
            }

        }

    }

    public boolean hasAnyPermissionForSchool(School school) {
        return school_id.equals(school.getId());
    }

    public void checkPermission(String permission, Long school_id)
            throws InsufficientPermissionException {

        if (hasPermission(permission, school_id)) {
            return;
        } else {

            School school = SchoolTermStore.getInstance().getSchoolById(
                    school_id);

            throw new InsufficientPermissionException(this, permission, school);

        }

    }

    public void checkPermission(String permission, School school)
            throws InsufficientPermissionException {

        if (hasPermission(permission, school)) {
            return;
        } else {

            throw new InsufficientPermissionException(this, permission, school);

        }

    }

    public void removeRole(Role role) {

        roles.remove(role);
        permissions.clear();

        List<Role> rolesOld = roles;
        roles = new ArrayList<>();

        for (Role role1 : rolesOld) {
            addRole(role1); // fills ArrayList roles and fills Set permissions
        }

    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) throws Exception {
        SaltAndHash saltAndHash = PasswordSecurity.getSaltedHash(password);
        salt = saltAndHash.getSalt();
        hash = saltAndHash.getHash();
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public void setSchool_id(Long school_id) {
        this.school_id = school_id;
    }

    public void setIs_admin(Boolean is_admin) {
        this.is_admin = is_admin;
    }
}
