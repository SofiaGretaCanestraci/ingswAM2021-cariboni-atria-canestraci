package it.polimi.ingsw.controller;

import com.google.gson.Gson;
import it.polimi.ingsw.enumerations.ResourceType;
import it.polimi.ingsw.enumerations.TurnPhase;
import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.model.Depot;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.server.VirtualView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class InputChecker {
    private GameController gameController;
    private Map<String, VirtualView> connectedClients;
    private Game game;


    public InputChecker(GameController gameController, Map<String, VirtualView> connectedClients, Game game) {
        this.gameController = gameController;
        this.connectedClients = connectedClients;
        this.game = game;
    }

    public boolean checkReceivedMessage(Message message, String nickname){
        Gson gson = new Gson();
        switch(message.getCode()) {
            case BUY_DEV:
                if (gameController.getTurnController().isGameActionDone()) {
                    connectedClients.get(nickname).sendInvalidActionMessage();
                    return false;
                }
                return true;
            case RESOURCE_PAYMENT:
                Integer[] ids = gson.fromJson(message.getPayload(), Integer[].class);

                if ((checkPayment(ids, nickname))&&((gameController.getTurnPhase() == TurnPhase.BUY_DEV)||(gameController.getTurnPhase() == TurnPhase.ACTIVATE_PRODUCTION) )) {
                    return true;
                }
                return false;
            case PLACE_RESOURCE_WAREHOUSE:
            case PLACE_RESOURCE_WHEREVER:
                int[] id = gson.fromJson(message.getPayload(), int[].class);
                if (id.length == gameController.getGame().getCurrentPlayer().getPlayerBoard().getUnplacedResources().size()) {
                    return true;
                }
                return false;
            case BUY_MARKET:
                if (gameController.getTurnController().isGameActionDone()) {
                    connectedClients.get(nickname).sendInvalidActionMessage();
                    return false;
                }
                String[] dim = gson.fromJson(message.getPayload(), String[].class);
                if((dim.length != 2)||((!dim[0].equalsIgnoreCase("row"))&&(!dim[0].equalsIgnoreCase("col")))){
                    return false;
                }
                return true;
            case WHITE_MARBLES:
                int[] marb = gson.fromJson(message.getPayload(), int[].class);
                if((connectedClients.get(nickname).getFreeMarble().isEmpty())||(marb.length < connectedClients.get(nickname).getFreeMarble().size())||(gameController.getTurnPhase() != TurnPhase.BUY_MARKET)) {
                    return false;
                }
                return true;
            case SLOT_CHOICE:
                if(gameController.getTurnPhase() == TurnPhase.BUY_DEV){
                    return true;
                } return false;
            case DUMMY_STRONGBOX:
                if(gameController.getConnectedClients().get(nickname).getTempDepots().isEmpty()){
                    return false;
                }return true;
            case MOVE_RESOURCES:
                if((gameController.getConnectedClients().get(nickname).getTempDepots().isEmpty())||(!checkDepotsState(nickname))){
                    return false;
                }
                return  true;
            default:
                return true;
        }
    }

    /**
     * checks if the player has the resources he wants to pay with and if they are the same quantity
     * required by the cost of the card
     * @param ids id of the depot he wants to take the resources, -1 if it's the strongbox
     * @param nickname nickname of the player
     * @return true if the payment can be done, false otherwise
     */
    public boolean checkPayment(Integer[] ids, String nickname){
        List<Integer> depotIds =Arrays.asList(ids);
        ArrayList<Resource> cost = connectedClients.get(nickname).getFreeDevelopment().get(0).getCost();
        ArrayList<Resource> payment = cost;
        Map<Integer, Long> couterMap = depotIds.stream().collect(Collectors.groupingBy(p-> p.intValue(),Collectors.counting()));
        for (int j : couterMap.keySet()) {
            if(j != - 1){
                if(game.getCurrentPlayer().getDepotById(j).getDepot().size() < couterMap.get(j)){
                    return false;
                }else{
                    for(int i = 0; i < couterMap.get(j); i++){
                        payment.remove(game.getCurrentPlayer().getDepotById(j).getDepot().get(0));
                    }
                }
            }
        }
        ArrayList<Resource> strongBox = game.getCurrentPlayer().getPlayerBoard().getStrongBox().getRes();
        Map<ResourceType, Long> paymentMap = payment.stream().collect(Collectors.groupingBy(p -> p.getResourceType(),Collectors.counting()));
        Map<ResourceType, Long> strongBoxMap = strongBox.stream().collect(Collectors.groupingBy(p -> p.getResourceType(),Collectors.counting()));
        for(ResourceType resourceType : paymentMap.keySet()){
            if ((strongBoxMap.get(resourceType) == null) || (strongBoxMap.get(resourceType) < paymentMap.get(resourceType))) {
                return false;
            }
        }
        return true;
    }

    /**
     * checks if the new configuration of the depots is a valid one
     * @return true if it is valid false otherwise
     */
    public boolean checkDepotsState(String nickname){
        if(!gameController.getVirtualView(nickname).getTempStrongBox().getRes().isEmpty() && gameController.getVirtualView(nickname).getTempStrongBox().getRes().size() < game.getCurrentPlayer().getPlayerBoard().getStrongBox().getRes().size()){
            emptyStorage(nickname);
            return false;
        }
          ArrayList<Resource> newPlacement = new ArrayList<>();
          for(Depot depot : gameController.getVirtualView(nickname).getTempDepots()){
              newPlacement.addAll(depot.getDepot());
          }
          newPlacement.addAll(gameController.getVirtualView(nickname).getTempStrongBox().getRes());
          ArrayList<Resource> intersection;
          intersection = newPlacement;
          intersection.removeAll(game.getCurrentPlayer().getPlayerBoard().getResources());
          if(intersection.isEmpty()){
              intersection = game.getCurrentPlayer().getPlayerBoard().getResources();
              intersection.removeAll(newPlacement);
              if(intersection.isEmpty()){
                  for(Depot depot : gameController.getVirtualView(nickname).getTempDepots()){
                      if(!validDepot(depot)){
                          emptyStorage(nickname);

                          return false;
                      }
                  }
              }else{
                  emptyStorage(nickname);

                  return false;
              }
          }else{
              emptyStorage(nickname);

              return false;
          }
          return true;
    }

    /**
     * checks if the given depot is a valid depot in terms of dimension and resource type
     * @param depot
     * @return
     */
    public boolean validDepot(Depot depot){
        Depot d = game.getCurrentPlayer().getDepotById(depot.getId());
        for(Resource r: depot.getDepot()){
            if(!d.possibleToAdd(r)){
                return false;
            }
        }
        if(depot.getDepot().size() > d.getDimension()){
            return false;
        }
        return true;
    }

    /**
     * empty the storage in case of invalid move_resources request
     */
    public void emptyStorage(String nickname){
        gameController.getVirtualView(nickname).freeTempDepots();
        gameController.getVirtualView(nickname).freeStrongBox();
    }
}
