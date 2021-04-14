package it.polimi.ingsw;
import it.polimi.ingsw.model.Depot;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.model.WareHouse;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static it.polimi.ingsw.enumerations.ResourceType.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Alessandra Atria
 */

public class WareHouseTest {
    private WareHouse wareHouse;
    private static Depot depot1;
    private static Depot depot2;



    @Before
    public void setUp() {
        ArrayList<WareHouse> w = new ArrayList<>();
        wareHouse = new WareHouse();

    }

    /**
     * checks if warehouse can get depots
     */

    @Test
    public void getDepots() {
        ArrayList<Resource> res = new ArrayList<>();
        ArrayList<Resource> res2 = new ArrayList<>();
        Resource r1 = new Resource(SHIELD);
        res.add(r1);
        wareHouse = new WareHouse();
        depot1 = new Depot( 2, res);
        depot2 = new Depot( 3, res2);
        assertTrue(depot1.getDepot().contains(r1));
        assertFalse(depot2.getDepot().contains(r1));
    }



    /**
     * checks if resources can be moved from a depot to another one
     */
    @Test
    public void moveRes() {
        ArrayList<Resource> res = new ArrayList<>();
        ArrayList<Resource> res2 = new ArrayList<>();
        Resource r1 = new Resource(COIN);
        res.add(r1);
        wareHouse = new WareHouse();
        depot1 = new Depot( 2, res);
        depot2 = new Depot( 3, res2);
        assertTrue(depot1.getDepot().contains(r1));
        assertFalse(depot2.getDepot().contains(r1));
        wareHouse.moveResouces(depot1, depot2, r1);
        assertTrue(depot2.getDepot().contains(r1));
        assertFalse(depot1.getDepot().contains(r1));
    }

    /**
     * checks if depots can be added to Warehouse
     */
    @Test
    public void addDepot(){
        ArrayList<Resource> res = new ArrayList<>();
        Resource r1 = new Resource(SERVANT);
        res.add(r1);
        wareHouse = new WareHouse();
        depot1 = new Depot( 2, res);
        wareHouse.addDepot(depot1);
        assertTrue(wareHouse.getDepots().contains(depot1));
        assertTrue(wareHouse.hasResource(r1));
        depot1.removeResource(r1);
        assertFalse(depot1.getDepot().contains(r1));
        assertFalse(wareHouse.hasResource(r1));
    }


    /**
     * checks if depots have the resource
     */
    @Test
    public void hasRes() {
        ArrayList<Resource> res = new ArrayList<>();
        Resource r1 = new Resource(SERVANT);
        res.add(r1);
        wareHouse = new WareHouse();
        depot1 = new Depot( 2, res);
        wareHouse.addDepot(depot1);
        assertTrue(wareHouse.getDepots().contains(depot1));
        assertTrue(wareHouse.hasResource(r1));
        depot1.removeResource(r1);
        assertFalse(depot1.getDepot().contains(r1));
        assertFalse(wareHouse.hasResource(r1));
    }

}
