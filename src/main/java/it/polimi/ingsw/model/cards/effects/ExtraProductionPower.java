package it.polimi.ingsw.model.cards.effects;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Resource;

/**
 * this effect is an extra production power that produces a resource resourcetype and a faith point with any resource type as entry resource
 * @author Alice Cariboni
 */
public class ExtraProductionPower implements LeaderEffect{
    private final Resource resourceType;
    private final int quantity;


    public ExtraProductionPower(Resource resourceType, int quantity) {
        this.resourceType = resourceType;
        this.quantity = quantity;
    }

    @Override
    public void applyEffect(Player p) {

    }

    public Resource getResourceType() {
        return resourceType;
    }

    public int getQuantity() {
        return quantity;
    }
}