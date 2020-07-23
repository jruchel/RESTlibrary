package org.whatever.library.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.whatever.library.services.RentalService;

@RestController
@RequestMapping("/rental")
public class RentalController {

    @Autowired
    private RentalService rentalService;

    @CrossOrigin
    @PostMapping("/reserve")
    public void reserveBook(@RequestBody int bid) {
        rentalService.reserveBook(getPrincipalUsername(), bid);
    }

    @CrossOrigin
    @DeleteMapping("/reserve")
    public void cancelReservation(@RequestBody int bid) {
        rentalService.cancelReservation(getPrincipalUsername(), bid);
    }


    // ===== private methods =================
    private String getPrincipalUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails)
            return ((UserDetails) (SecurityContextHolder.getContext().getAuthentication().getPrincipal())).getUsername();
        return principal.toString();
    }


}
