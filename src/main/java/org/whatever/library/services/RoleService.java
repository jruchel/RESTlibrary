package org.whatever.library.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.whatever.library.models.Role;
import org.whatever.library.repositories.RoleRepository;

import java.util.HashSet;
import java.util.Set;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;


    public Set<Role> getAllRoles() {
        return roleRepository.getAllRoles();
    }

    public Role getRoleByName(String name) {
        return roleRepository.getRoleByName(name);
    }

    public Set<Role> getUserRoles() {
        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.getRoleByName("ROLE_USER"));
        return roles;
    }

    public Set<Role> getModeratorRoles() {
        Set<Role> roles = getUserRoles();
        roles.add(roleRepository.getRoleByName("ROLE_MODERATOR"));
        return roles;
    }

    public Set<Role> getAdminRoles() {
        Set<Role> roles = getModeratorRoles();
        roles.add(roleRepository.getRoleByName("ROLE_ADMIN"));
        return roles;
    }


}
