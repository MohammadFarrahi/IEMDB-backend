package ie.generic.model;

import ie.exception.CustomException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class Manager <T> {
    private Map<String,T> objectMap;

    public abstract String addElement(T object);
    public abstract String updateElement(String id, T newObject);

    public T getElementById(String id) throws CustomException {
        if (!objectMap.containsKey(id)) {
            throw new CustomException("ObjectNotFound"); // TODO: handle exception message properly
        }
        return objectMap.get(id);
    }
    public List<T> getElementsById(List<String> ids) throws CustomException {
        List<T> objects = new ArrayList<>();
        for (String id : ids) {
            objects.add(getElementById(id));
        }
        return objects;
    }
    public boolean isIdValid(String id) {
        return objectMap.containsKey(id);
    }
    public boolean isIdListValid(List<String> ids) {
        return objectMap.keySet().containsAll(ids);
    }
}
