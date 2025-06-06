package com.example.project3.unittesting;

import org.testng.annotations.Test;
import com.example.project3.util.Date;

//import static org.junit.Assert.*;
import static org.testng.AssertJUnit.assertFalse;
import static org.testng.AssertJUnit.assertTrue;

public class DateTest {

    @Test
    public void isValid() {
        //Year is too small
        Date d1 = new Date("1/17/900");
        assertFalse(d1.isValid());

        //Month is out of range (0 is not a valid month)
        Date d2 = new Date("0/11/2001");
        assertFalse(d2.isValid());

        //February 29 on a non-leap year (2018)
        Date d3 = new Date("2/29/2018");
        assertFalse(d3.isValid());

        //Day is out of range (March 32 does not exist)
        Date d4 = new Date("3/32/2003");
        assertFalse(d4.isValid());

        //February 29 on a leap year (2000)
        Date d5 = new Date("2/29/2000");
        assertTrue(d5.isValid());

        //Last day of the year
        Date d6 = new Date("12/31/2001");
        assertTrue(d6.isValid());
    }
}