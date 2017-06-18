package de.sp.database.stores;

import de.sp.database.connection.ConnectionPool;
import de.sp.database.daos.basic.ValueDAO;
import de.sp.database.model.DatabaseStore;
import de.sp.database.model.Value;
import de.sp.database.valuelists.ValueListType;
import de.sp.tools.validation.ValidationException;
import org.sql2o.Connection;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by martin on 11.05.2017.
 */
public class ValueListStore implements DatabaseStore {

    private static ValueListStore instance = null;

    private Map<Long, Map<Long, Map<Long, Value>>>
            schoolIdToValueStoreKeyToKeyToValueMap = new ConcurrentHashMap<>();


    public static ValueListStore getInstance(){

        if(instance == null){
            instance = new ValueListStore();
        }

        return instance;
    }

    public List<Value> getValueList (Long school_id, Long valueStoreKey){

        List<Value> valueList = new ArrayList<>();

        Map<Long, Map<Long, Value>> valueStoreKeyToKeyToValueMap
                = schoolIdToValueStoreKeyToKeyToValueMap.get(school_id);

        if(valueStoreKeyToKeyToValueMap == null){
            return valueList;
        }

        Map<Long, Value> keyToValueMap = valueStoreKeyToKeyToValueMap.get(valueStoreKey);

        if(keyToValueMap == null){
            return valueList;
        }

        valueList.addAll(keyToValueMap.values());

        return valueList;

    }

    public Value getValue(Long school_id, Long valueStoreKey, Long key){

        Map<Long, Map<Long, Value>> valueStoreKeyToKeyToValueMap
                = schoolIdToValueStoreKeyToKeyToValueMap.get(school_id);

        if(valueStoreKeyToKeyToValueMap == null){
            return null;
        }

        Map<Long, Value> keyToValueMap = valueStoreKeyToKeyToValueMap.get(valueStoreKey);

        if(keyToValueMap == null){
            return null;
        }

        return keyToValueMap.get(key);

    }

    public void loadFromDatabase(){

        try (Connection con = ConnectionPool.open()) {


            List<Value> values = ValueDAO.getAll(con);

            for (Value value : values) {

                addValue(value);

            }

        }

    }

    @Override
    public void removeSchool(Long school_id) {
        schoolIdToValueStoreKeyToKeyToValueMap.remove(school_id);
    }

    private void addValue(Value value) {

        Map<Long, Map<Long, Value>> valueStoreKeyToKeyToValueMap
                = schoolIdToValueStoreKeyToKeyToValueMap.get(value.getSchool_id());

        if(valueStoreKeyToKeyToValueMap == null){
            valueStoreKeyToKeyToValueMap = new ConcurrentHashMap<>();
            schoolIdToValueStoreKeyToKeyToValueMap.put(value.getSchool_id(), valueStoreKeyToKeyToValueMap);
        }

        Map<Long, Value> keyToValueMap = valueStoreKeyToKeyToValueMap.get(value.getValuestore_key());

        if(keyToValueMap == null){
            keyToValueMap = new ConcurrentHashMap<>();
            valueStoreKeyToKeyToValueMap.put(value.getValuestore_key(), keyToValueMap);
        }

        keyToValueMap.put(value.getId(), value);


    }

    /**
     *
     * Stores new Value in database and in ValueListStore
     * If value with given external id already exists then
     * don't store value but return existing one.
     *
     * @param school_id
     * @param valueList
     * @param name
     * @param abbreviation
     * @param external_key
     * @param con
     * @return
     * @throws Exception
     */
    public Value addValue(Long school_id, ValueListType valueList,
                          String name, String abbreviation, String external_key,
                          Connection con) throws Exception {

        List<Value> vl = getValueList(school_id, valueList.getKey());

        int maxSortingOrder = 0;

        for (Value value : vl) {

            if(value.getSortingOrder() > maxSortingOrder){
                maxSortingOrder = value.getSortingOrder();
            }

            if(value.getExternal_key() != null && external_key != null && value.getExternal_key().equals(external_key) ){
                return value;
            }

        }

        Value value = ValueDAO.insert(valueList.getKey(), school_id,
                name, abbreviation, external_key, maxSortingOrder + 1, con);

        addValue(value);

        return value;

    }


    public Value findByExernalKey(Long school_id, Long key, String schluessel) {

        for (Value value : getValueList(school_id, key)) {
            if(value.getExternal_key() != null && value.getExternal_key().equals(schluessel)){
                return value;
            }
        }

        return null;

    }

    public void validateID(ValueListType valueListType, Long school_id, Long id, boolean nullPermitted) throws ValidationException {

        if(id != null) {
            List<Value> valueList = getValueList(school_id, valueListType.getKey());

            for (Value value : valueList) {
                if (value.getId().equals(id)) {
                    return;
                }
            }
            throw new ValidationException("Der Wert " + id + " wurde in der Werteliste " + valueListType.toString() + " nicht gefunden.");
        }

        if(!nullPermitted) {
            throw new ValidationException("Der Wert null wurde in der Werteliste " + valueListType.toString() + " nicht gefunden.");
        }
    }


}
