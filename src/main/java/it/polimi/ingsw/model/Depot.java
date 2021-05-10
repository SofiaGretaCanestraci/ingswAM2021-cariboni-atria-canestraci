package it.polimi.ingsw.model;
import it.polimi.ingsw.enumerations.ResourceType;
import it.polimi.ingsw.exceptions.NotPossibleToAdd;

import java.util.ArrayList;
import java.util.Objects;

/**
 * @author Alessandra Atria
 */

    public class Depot {
        private int id;
        protected int dimension;
        protected ArrayList<Resource> resources;

        public Depot(int dimension, int id, ArrayList<Resource> resources) {
            this.id = id;
            this.dimension = dimension;
            this.resources = resources;
        }


    /**
     * This method adds resources to the depot
     * @param res represents the resource to add
     * @throws NotPossibleToAdd if the resource can't be added
     */
        public void addResource(Resource res) throws NotPossibleToAdd {
            int countRes;
                countRes = this.resources.size() + 1 ;
            if(this.resources.isEmpty() || (this.resources.contains(res) && countRes <= dimension)){
                this.resources.add(res);
            }else
                throw new NotPossibleToAdd();



        }

    /**
     * This method removes resources from the depot
     * @param res represents the resource to remove
     */
        public void removeResource(Resource res){
               this.resources.remove(res);
        }

    /**
     * This method gets the resources that are in the depot
     */

    public ArrayList<Resource> getDepot(){
            return this.resources;
        }

    /**
     * This method gets the maximum number of resources that the depot can contain
     */
        public int getDimension() {
            return this.dimension;
        }

    /**
     * This method checks if the depot doesn't have any resources
     */
        public boolean isEmpty(){
          return  resources.isEmpty();
        }


        public Resource getType(){
            if(this.isEmpty()){
                return new Resource(ResourceType.NONE);
            }
            return this.resources.get(0);
        }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Depot depot = (Depot) o;
        return dimension == depot.dimension && Objects.equals(resources, depot.resources);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dimension, resources);
    }

    public int getId() {
        return id;
    }
}


