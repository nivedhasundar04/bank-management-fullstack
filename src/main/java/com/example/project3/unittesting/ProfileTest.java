package com.example.project3.unittesting;

import org.junit.jupiter.api.Test;
import com.example.project3.sourcefiles.Profile;
import com.example.project3.util.Date;

//import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ProfileTest {

    @Test
    public void compareTo() {
        // Creating Date objects for testing
        Date d1 = new Date("1/15/1995");
        Date d2 = new Date("7/22/2000");
        Date d3 = new Date("5/10/1998");
        Date d4 = new Date("7/22/2001"); // Same name as p2, different DOB
        Date d5 = new Date("4/25/1981");

        // Creating Profile objects for testing
        Profile p1 = new Profile("Robert", "Allen", d1);
        Profile p2 = new Profile("Amelia", "Stone", d2);
        Profile p3 = new Profile("Robert", "Smith", d3);
        Profile p4 = new Profile("Amelia", "Stone", d4); // Same name as p2, different DOB
        Profile p5 = new Profile("Barron", "Smith", d5);
        Profile p6 = new Profile("Barron", "Smith", d5); // Identical to p5

        // Test cases based on compareTo()
        assertTrue(p1.compareTo(p2) < 0);
        assertTrue(p1.compareTo(p3) < 0);
        assertTrue(p2.compareTo(p4) < 0);
        assertTrue(p2.compareTo(p1) > 0);
        assertTrue(p3.compareTo(p5) > 0);
        assertTrue(p4.compareTo(p2) > 0);
        assertEquals(0, p5.compareTo(p6));
    }
}