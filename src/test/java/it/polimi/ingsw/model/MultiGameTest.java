package it.polimi.ingsw.model;


import com.google.gson.Gson;
import it.polimi.ingsw.enumerations.CardColor;
import it.polimi.ingsw.exceptions.CannotAdd;
import it.polimi.ingsw.exceptions.JsonFileNotFoundException;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.cards.DevelopmentCardDeck;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.cards.LeaderDeck;
import it.polimi.ingsw.model.cards.effects.ProductionPower;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Sofia Canestraci
 */

public class MultiGameTest {
    private static ArrayList<Player> players1;
    private static LeaderDeck deckLeader;
    private static DevelopmentCardDeck[][] deckDevelopment;
    private static FaithTrack faithTrack;
    private static MarketTray marketTray;
    private static ArrayList<Player> winners;
    private static int currentPlayer;

    @BeforeAll
    public static void init()   {
        players1 = new ArrayList<>();

        String player_1 = "player1";
        String player_2 = "player2";
        String player_3 = "player3";
        String player_4 = "player4";
        ArrayList<LeaderCard> leaderCards1 = new ArrayList<>();
        ArrayList<LeaderCard> leaderCards2= new ArrayList<>();
        ArrayList<LeaderCard> leaderCards3 = new ArrayList<>();
        ArrayList<LeaderCard> leaderCards4 = new ArrayList<>();
        PlayerBoard playerBoard1 = new PlayerBoard(new WareHouse(), new StrongBox());
        PlayerBoard playerBoard2 = new PlayerBoard(new WareHouse(), new StrongBox());
        PlayerBoard playerBoard3 = new PlayerBoard(new WareHouse(), new StrongBox());
        PlayerBoard playerBoard4 = new PlayerBoard(new WareHouse(), new StrongBox());
        Player player1 = new Player(true, player_1, 0, leaderCards1, playerBoard1);
        Player player2 = new Player(false, player_2, 1, leaderCards2, playerBoard2);
        Player player3 = new Player(false, player_3, 2, leaderCards3, playerBoard3);
        Player player4 = new Player(false, player_4, 3, leaderCards4, playerBoard4);
        players1.add(player1);
        players1.add(player2);
        players1.add(player3);
        players1.add(player4);
    }

    /**
     * it controls if MultiGame is correctly instantiated
     */
    @Test
    void getterTest(){
        assertEquals(players1.get(0).getNickName(), "player1");
        assertEquals(players1.get(1).getNickName(), "player2");
        assertEquals(players1.get(2).getNickName(), "player3");
        assertEquals(players1.get(3).getNickName(), "player4");
        assertEquals(players1.get(0).getVictoryPoints(), 0);
    }

    /**
     * it controls that the players are correctly added in the array list
     */
    @Test
    void addPlayerTest() throws JsonFileNotFoundException {
        MultiGame multiGame = new MultiGame();
        ArrayList<Player> players2 = new ArrayList<>();

        multiGame.addPlayer(players1.get(0));
        multiGame.addPlayer(players1.get(1));
        multiGame.addPlayer(players1.get(2));
        multiGame.addPlayer(players1.get(3));

        players2 = multiGame.getPlayers();

        assertEquals(players1.get(0), players2.get(0));
        assertEquals(players1.get(1), players2.get(1));
        assertEquals(players1.get(2), players2.get(2));
        assertEquals(players1.get(3), players2.get(3));

    }

    /**
     * it controls that the class return the right player as next player
     */
    @Test
    void nextPlayerTest() throws JsonFileNotFoundException {
        MultiGame multiGame = new MultiGame();
        multiGame.addPlayer(players1.get(0));
        multiGame.addPlayer(players1.get(1));
        multiGame.addPlayer(players1.get(2));
        multiGame.addPlayer(players1.get(3));
        Player p2 = multiGame.nextPlayer(players1.get(0));
        assertEquals(p2, players1.get(1));
        Player p3 = multiGame.nextPlayer(players1.get(1));
        assertEquals(p3, players1.get(2));
        Player p4 = multiGame.nextPlayer(players1.get(2));
        assertEquals(p4, players1.get(3));
        Player p1 = multiGame.nextPlayer(players1.get(3));
        assertEquals(p1, players1.get(0));
    }

    /**
     * it controls that the class return the right players as winners
     */
    @Test
    void addWinnerTest() throws JsonFileNotFoundException {
        MultiGame multiGame = new MultiGame();
        multiGame.addPlayer(players1.get(0));
        multiGame.addPlayer(players1.get(1));
        multiGame.addPlayer(players1.get(2));
        multiGame.addPlayer(players1.get(3));
        multiGame.addWinner();
        players1.get(3).addVictoryPoints(20);
        assertEquals(players1.get(3).getVictoryPoints(), 23);
        assertEquals(multiGame.addWinner().size(), 1);
        assertEquals(players1.get(3), multiGame.addWinner().get(0));

        ArrayList<Player> players2 = new ArrayList<>();
        String player_1 = "player1";
        String player_2 = "player2";
        ArrayList<LeaderCard> leaderCards1 = new ArrayList<>();
        ArrayList<LeaderCard> leaderCards2= new ArrayList<>();
        PlayerBoard playerBoard1 = new PlayerBoard();
        PlayerBoard playerBoard2 = new PlayerBoard();
        Player player1 = new Player(true, player_1, 1, leaderCards1, playerBoard1);
        Player player2 = new Player(false, player_2, 1, leaderCards2, playerBoard2);
        players2.add(player1);
        players2.add(player2);

        MultiGame multiGame2 = new MultiGame();
        multiGame2.addPlayer(players2.get(0));
        multiGame2.addPlayer(players2.get(1));
        assertEquals(multiGame2.addWinner().size(), 2);
        assertEquals(players2.get(0), multiGame2.addWinner().get(0));
        assertEquals(players2.get(1), multiGame2.addWinner().get(1));
    }

    /**
     *
     * @throws JsonFileNotFoundException
     */
    @Test
    void getDevCardTest() throws JsonFileNotFoundException {
        MultiGame multiGame = new MultiGame();
        multiGame.addPlayer(players1.get(0));
        multiGame.addPlayer(players1.get(1));
        multiGame.addPlayer(players1.get(2));
        multiGame.addPlayer(players1.get(3));
        DevelopmentCard developmentCard = multiGame.getCardFrom(0,0);
        assertFalse(multiGame.getDeckDevelopment()[0][0].getCardDeck().contains(developmentCard));
    }

    /**
     * it controls if the method add accurately the victory points to the player when is report section is true
     * @throws JsonFileNotFoundException
     */
    @Test
    void getPopePointsTest() throws JsonFileNotFoundException {
        MultiGame multiGame = new MultiGame();
        multiGame.addPlayer(players1.get(0));
        multiGame.addPlayer(players1.get(1));
        multiGame.addPlayer(players1.get(2));
        multiGame.addPlayer(players1.get(3));
        players1.get(0).getPlayerBoard().moveFaithMarker(5);
        FaithTrack faithTrack = new FaithTrack();
        assertTrue(faithTrack.isReportSection(players1.get(0).getPlayerBoard().getFaithMarker()));
        multiGame.getPopePoints();
        assertEquals(players1.get(0).getVictoryPoints(), 2);
    }

    /**
     * it controls if the method return true when the requests are satisfied
     * @throws JsonFileNotFoundException
     * @throws CannotAdd
     */
    @Test
    void checkEndGameTest() throws JsonFileNotFoundException, CannotAdd {
        MultiGame multiGame = new MultiGame();
        multiGame.addPlayer(players1.get(0));
        multiGame.addPlayer(players1.get(1));
        multiGame.addPlayer(players1.get(2));
        multiGame.addPlayer(players1.get(3));
        ArrayList<Resource> cost = new ArrayList<>();
        ProductionPower productionPower = new ProductionPower();
        DevelopmentCard developmentCard1 = new DevelopmentCard(0, cost, 1, CardColor.PURPLE, productionPower, 0 );
        DevelopmentCard developmentCard2 = new DevelopmentCard(1, cost, 2, CardColor.PURPLE, productionPower, 0 );
        DevelopmentCard developmentCard3 = new DevelopmentCard(2, cost, 3, CardColor.PURPLE, productionPower, 0 );
        DevelopmentCard developmentCard4 = new DevelopmentCard(3, cost, 1, CardColor.YELLOW, productionPower, 0 );
        DevelopmentCard developmentCard5 = new DevelopmentCard(4, cost, 2, CardColor.YELLOW, productionPower, 0 );
        DevelopmentCard developmentCard6 = new DevelopmentCard(5, cost, 3, CardColor.YELLOW, productionPower, 0 );
        DevelopmentCard developmentCard7 = new DevelopmentCard(6, cost, 1, CardColor.GREEN, productionPower, 0 );

        players1.get(1).getPlayerBoard().addDevCard(developmentCard1, 0);
        players1.get(1).getPlayerBoard().addDevCard(developmentCard2, 0);
        players1.get(1).getPlayerBoard().addDevCard(developmentCard3, 0);
        players1.get(1).getPlayerBoard().addDevCard(developmentCard4, 1);
        players1.get(1).getPlayerBoard().addDevCard(developmentCard5, 1);
        players1.get(1).getPlayerBoard().addDevCard(developmentCard6, 1);
        players1.get(1).getPlayerBoard().addDevCard(developmentCard7, 2);

        assertEquals(players1.get(1).getPlayerBoard().getCountDevCards(), 7);
        assertTrue(multiGame.checkEndGame());

        players1.get(1).getPlayerBoard().moveFaithMarker(23);
        assertEquals(players1.get(1).getPlayerBoard().getFaithMarker(), 24);
        assertTrue(multiGame.checkEndGame());
    }

    /**
     * it controls the last player to play the game is the last player for players
     * @throws JsonFileNotFoundException
     */
    @Test
    void endGameTest() throws JsonFileNotFoundException {
        MultiGame multiGame = new MultiGame();
        multiGame.addPlayer(players1.get(0));
        multiGame.addPlayer(players1.get(1));
        multiGame.addPlayer(players1.get(2));
        multiGame.addPlayer(players1.get(3));
        multiGame.endGame(players1.get(0));
        assertEquals(multiGame.getCurrentPlayer(), 3);
    }
}

