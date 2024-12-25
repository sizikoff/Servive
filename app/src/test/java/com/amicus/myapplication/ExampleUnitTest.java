package com.amicus.myapplication;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class ExampleUnitTest {
    @Test
    public void testCalc(){
        Calculator calculator = new Calculator();

        assertThrows(ArithmeticException.class,()->calculator.devide(5,0));
    }
}