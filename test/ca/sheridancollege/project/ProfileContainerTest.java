/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.sheridancollege.project;

import java.util.ArrayList;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author sbelb
 */
public class ProfileContainerTest {
    
    public ProfileContainerTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        System.out.println("In setup class");
    }
    
    @AfterClass
    public static void tearDownClass() {
        System.out.println("In tear down class");
    }
    
    @Before
    public void setUp() {
        System.out.println("In setup");
    }
    
    @After
    public void tearDown() {
        System.out.println("In tear down");
    }

    // start tests for player name length
    @Test
    public void testValidatePlayerNameLengthGood() {
        System.out.println("validatePlayerNameLengthGood");
        String pendingName = "Samantha";
        ProfileContainer instance = new ProfileContainer();
        boolean expResult = true;
        boolean result = instance.validatePlayerName(pendingName);
        assertEquals(expResult, result);
    }
    @Test
    public void testValidatePlayerNameLengthBad() {
        System.out.println("validatePlayerNameLengthBad");
        String pendingName = "Samantha Belbeck";
        ProfileContainer instance = new ProfileContainer();
        boolean expResult = false;
        boolean result = instance.validatePlayerName(pendingName);
        assertEquals(expResult, result);
    }
    @Test
    public void testValidatePlayerNameLengthBoundary() {
        System.out.println("validatePlayerNameLengthBoundary");
        String pendingName = "Sam";
        ProfileContainer instance = new ProfileContainer();
        boolean expResult = true;
        boolean result = instance.validatePlayerName(pendingName);
        assertEquals(expResult, result);
    }
    // end tests for player name length
    
    // start tests for unique player name
    @Test
    public void testValidatePlayerNameUniqueGood() {
        System.out.println("validatePlayerNameUniqueGood");
        ProfileContainer instance = new ProfileContainer();
        Game game = new WarGame("War");
        String testName = "Sam";
        instance.createPlayerProfile(testName);
        game.getPlayers().add(new WarPlayer(testName));
        boolean expResult = true;
        String pendingName = "Samantha";
        boolean result = instance.validatePlayerName(pendingName);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testValidatePlayerNameUniqueBad() {
        System.out.println("validatePlayerNameUniqueBad");
        ProfileContainer instance = new ProfileContainer();
        Game game = new WarGame("War");
        String testName = "Samantha";
        instance.createPlayerProfile(testName);
        game.getPlayers().add(new WarPlayer(testName));
        boolean expResult = false;
        String pendingName = "Samantha";
        boolean result = instance.validatePlayerName(pendingName);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testValidatePlayerNameUniqueBoundary() {
        System.out.println("validatePlayerNameUniqueBoundary");
        ProfileContainer instance = new ProfileContainer();
        Game game = new WarGame("War");
        String testName = "Samantha ";
        instance.createPlayerProfile(testName);
        game.getPlayers().add(new WarPlayer(testName));
        boolean expResult = false;
        String pendingName = "Samantha";
        boolean result = instance.validatePlayerName(pendingName);
        assertEquals(expResult, result);
    }   
    // end tests for unique player name
}
