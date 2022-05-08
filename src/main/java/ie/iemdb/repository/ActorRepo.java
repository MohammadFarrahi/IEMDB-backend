package ie.iemdb.repository;


import ie.iemdb.exception.ActorAlreadyExistsException;
import ie.iemdb.exception.ActorNotFoundException;
import ie.iemdb.exception.CustomException;
import ie.iemdb.model.Actor;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;



public class ActorRepo extends Repo<Actor, Integer> {
    private static ActorRepo instance = null;
    public static ActorRepo getInstance() {
        if (instance == null) {
            instance = new ActorRepo();
        }
        return instance;
    }

    private ActorRepo() {
        this.notFoundException = new ActorNotFoundException();
    }

    @Override
    protected String getGetElementByIdStatement() {
        return null;
    }

    @Override
    protected void fillGetElementByIdValues(PreparedStatement st, Integer id) {

    }

    @Override
    protected String getGetAllElementsStatement() {
        return null;
    }

    @Override
    protected Actor convertResultSetToDomainModel(ResultSet rs) {
        return null;
    }

    @Override
    protected ArrayList<Actor> convertResultSetToDomainModelList(ResultSet rs) {
        return null;
    }

    @Override
    public void addElement(Actor newObject) throws CustomException {
    }


    public ArrayList<Actor> getCastForMovie(int movieId) {
        return new ArrayList<>();
    }
}
