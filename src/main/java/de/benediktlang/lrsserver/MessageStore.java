package de.benediktlang.lrsserver;

import com.google.gson.Gson;
import de.benediktlang.lrsserver.entity.LogMsg;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class MessageStore {

    private List<LogMsg> store = Collections.synchronizedList(new LinkedList<LogMsg>());

    public void add(LogMsg logMsg) {
        store.add(logMsg);
    }

    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(store);
    }
}
