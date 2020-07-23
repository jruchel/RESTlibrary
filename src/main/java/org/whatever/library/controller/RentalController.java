package org.whatever.library.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.whatever.library.services.RentalService;

import java.util.Map;

@RestController
@RequestMapping("/rental")
public class RentalController {

    @Autowired
    private RentalService rentalService;

    @CrossOrigin
    @PostMapping("/reserve")
    public void reserveBook(@RequestBody Map<String, Integer> ids) {
        rentalService.reserveBook(getPrincipalUsername(), ids.get("aid"), ids.get("bid"));
    }


    // ===== private methods =================
    private String getPrincipalUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails)
            return ((UserDetails) (SecurityContextHolder.getContext().getAuthentication().getPrincipal())).getUsername();
        return principal.toString();
    }


}
