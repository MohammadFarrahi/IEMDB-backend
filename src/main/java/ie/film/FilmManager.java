package ie.film;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.fasterxml.jackson.databind.node.ObjectNode;
import ie.Iemdb;
import ie.user.UserManager;
import java.util.ArrayList;
import ie.types.Constant;
import java.util.HashMap;
import java.util.HashSet;

public class FilmManager {
    private final HashMap<String, Film> filmMap;
    private final ObjectMapper mapper;
    private final Iemdb database;

    public FilmManager(Iemdb database) {
        this.database = database;

        mapper = new ObjectMapper();
        mapper.enable(DeserializationFeature.FAIL_ON_READING_DUP_TREE_KEY);
        filmMap = new HashMap<>();
    }

    public Film addMovie(String data) throws Exception {
        var newFilm = mapper.readValue(data, Film.class);

        var jsonNode = mapper.readTree(data);
        var id = jsonNode.get(Constant.Movie.ID).asText();
        var cast = Iemdb.convertListToString(mapper.convertValue(mapper.readTree(data).get(Constant.Movie.CAST), ArrayList.class));

        if(!database.modelListExists(cast, Constant.Model.ACTOR)) {
            throw new Exception("Actor not found");
        }
        filmMap.put(id, newFilm);
        return newFilm;
    }

    public void rateMovie(String jsonData, UserManager userManager) throws Exception {
        JsonNode rateJsonNode = mapper.readTree(jsonData);
        ValidateVoteJson(rateJsonNode);
        ValidateVoteData(rateJsonNode, userManager);

        var userEmail = rateJsonNode.get(Constant.Rate.U_ID).asText();
        var filmId = rateJsonNode.get(Constant.Rate.M_ID).asText();
        var rate = rateJsonNode.get(Constant.Rate.RATE).asInt();
        filmMap.get(filmId).updateFilmRating(userEmail, rate);
    }

    private void ValidateVoteData(JsonNode rateJsonNode, UserManager userManager) throws Exception {
        var userEmail = rateJsonNode.get(Constant.Rate.U_ID).asText();
        var filmId = rateJsonNode.get(Constant.Rate.M_ID).asText();
        var rate = rateJsonNode.get(Constant.Rate.RATE).asInt();
        if (!userManager.isIdValid(userEmail)) {
            throw new Exception("ne user with this id");
        }
        if (filmMap.get(filmId) == null) {
            throw new Exception("ne film with this id");
        }    // TODO: consider using getFilm method
        if (!(1 <= rate && rate <= 10)) {
            throw new Exception("invalid rate number");
        }
    }

    private void ValidateVoteJson(JsonNode rateJsonNode) throws Exception {
        ArrayList<String> jsonFiledNames = new ArrayList<>();
        rateJsonNode.fieldNames().forEachRemaining(jsonFiledNames::add);

        var voteJsonFieldNames = Constant.Rate.getSet();
        boolean exceptionFlag = (jsonFiledNames.size() != voteJsonFieldNames.size());
        exceptionFlag |= !(voteJsonFieldNames.equals(new HashSet<String>(jsonFiledNames)));
        exceptionFlag |= !(rateJsonNode.get(Constant.Rate.M_ID).isInt() &&
                rateJsonNode.get(Constant.Rate.U_ID).isTextual() &&
                rateJsonNode.get(Constant.Rate.RATE).isInt());
        if (exceptionFlag) {
            throw new Exception("invalid input vote");
        }
    }

    public boolean isIdValid(String id){
        return filmMap.containsKey(id);
    }

    public boolean isIdListValid(ArrayList<String> ids) {
        for (var id : ids){
            if(!filmMap.containsKey(id))
                return false;
        }
        return true;
    }

    public Film getElement(String id) throws Exception{
        if (filmMap.containsKey(id)) {
            return filmMap.get(id);
        }
    throw new Exception("Movie not found");
    }

    public JsonNode serializeElement(String id, Constant.SER_MODE mode){
        try {
            var film = getElement(id);
            var jsonNode = mapper.valueToTree(film);

            if(mode == Constant.SER_MODE.LONG) {
                var castList = film.getCast();
                var castNode = database.serializeElementList(castList, Constant.Model.ACTOR, Constant.SER_MODE.LONG);
                var newNode = ((ObjectNode) jsonNode).putArray("cast").add(castNode);
                return newNode;
            }

            return jsonNode;

        }catch (Exception e){
            return null;
        }
    }


}
