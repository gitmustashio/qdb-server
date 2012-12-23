package io.qdb.server;

import io.qdb.server.model.*;
import org.codehaus.jackson.type.TypeReference;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Simple repo for development using data from json files in etc. Later this info will be stored in ZooKeeper.
 */
@Singleton
public abstract class SimpleRepository implements Repository {

    private final JsonService jsonService;
    private final List<User> users;
    private final List<Database> databases;
    private final List<Queue> queues;
    private final Date upSince;

    @Inject
    public SimpleRepository(JsonService jsonService) throws IOException {
        this.jsonService = jsonService;
        users = load("users", new TypeReference<List<User>>(){});
        databases = load("databases", new TypeReference<List<Database>>(){});
        queues = load("queues", new TypeReference<List<Queue>>(){});
        upSince = new Date();
    }

    private <T> T load(String filename, TypeReference typeRef) throws IOException {
        InputStream in = new FileInputStream(new File("etc", filename + ".json"));
        try {
            return jsonService.fromJson(in, typeRef);
        } finally {
            in.close();
        }
    }

    private <T extends ModelObject> T find(String id, List<T> list) {
        for (T o : list) {
            if (o.getId().equals(id)) return o;
        }
        return null;
    }

    @Override
    public Status getStatus() {
        Status s = new Status();
        s.upSince = upSince;
        return s;
    }

    @Override
    public User findUser(String id) {
        return find(id, users);
    }

    @Override
    public List<Database> findDatabasesVisibleTo(User user, int offset, int limit) {
        List<Database> ans = new ArrayList<Database>();
        for (Database db : databases) {
            if (db.isVisibleTo(user)) ans.add(db);
        }
        return ans;
    }

    @Override
    public Database findDatabase(String id) {
        return find(id, databases);
    }

}
