package ie.iemdb.repository;


import ie.iemdb.exception.ActorNotFoundException;
import ie.iemdb.model.Actor;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class ActorRepo extends Repo<Actor, Integer> {
    private static ActorRepo instance = null;
    public static final String ACTOR_TABLE = "Actor";
    public static ActorRepo getInstance() {
        if (instance == null) {
            instance = new ActorRepo();
        }
        return instance;
    }

    private ActorRepo() {
        this.initActorTable();
        this.notFoundException = new ActorNotFoundException();
    }
    private void initActorTable() {
        this.initTable(
                String.format(
                        "CREATE TABLE IF NOT EXISTS %s(id INTEGER," +
                        "\nname VARCHAR(225)," +
                        "\nnationality VARCHAR(225)," +
                        "\nimgUrl VARCHAR(225)," +
                        "\nbirthDate VARCHAR(225)," +
                        "\nPRIMARY KEY(id));",
                        ACTOR_TABLE
                )
        );
    }


    @Override
    protected String getGetElementByIdStatement() {
        return String.format("SELECT* FROM %s actor WHERE actor.id = ?;", ACTOR_TABLE);
    }

    @Override
    protected void fillGetElementByIdValues(PreparedStatement st, Integer id) {
        try {
            st.setString(1, String.valueOf(id));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected String getGetAllElementsStatement() {
        return String.format("SELECT * FROM %s;", ACTOR_TABLE);
    }

    @Override
    protected Actor convertResultSetToDomainModel(ResultSet rs) {
        try {
            var actor = new Actor(rs.getInt("id"), rs.getString("name"), rs.getString("birthDate"), rs.getString("nationality"), rs.getString("imgUrl"));
            actor.setRetriever(new Retriever());
            return actor;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected ArrayList<Actor> convertResultSetToDomainModelList(ResultSet rs) {
        ArrayList<Actor> actors = new ArrayList<>();
        try {
            while (rs.next()) {
                actors.add(this.convertResultSetToDomainModel(rs));
            }
            return actors;
        } catch (SQLException e) {
            e.printStackTrace();
            return actors;
        }
    }
    @Override
    protected String getAddElementStatement() {
        return String.format("INSERT INTO %s (id, name, birthDate, nationality, imgUrl) VALUES (?,?,?,?,?);", ACTOR_TABLE);
    }


    @Override
    public void addElement(Actor newObject) throws SQLException {
        var dbTuple = newObject.getDBTuple();
        executeUpdate(getAddElementStatement(), List.of(dbTuple.get("id"), dbTuple.get("name"), dbTuple.get("birthDate"), dbTuple.get("nationality"), dbTuple.get("imgUrl")));
    }

    public String getGetCastForMovieStatement() {
        return String.format("SELECT a.id, a.name, a.birthDate, a.nationality, a.imgUrl FROM %s a, %s c WHERE a.id = c.actorId AND c.movieId = ?;", ACTOR_TABLE, MovieRepo.CAST_TABLE);
    }
    public ArrayList<Actor> getCastForMovie(int movieId) throws SQLException {
        var resultSet = executeQuery(getGetCastForMovieStatement(), List.of(Integer.valueOf(movieId).toString()));
        return convertResultSetToDomainModelList(resultSet);
    }
}
