package io.qdb.server.controller;

import io.qdb.server.model.Database;
import io.qdb.server.model.Queue;
import io.qdb.server.model.Repository;

import java.io.IOException;

/**
 * Queue operations.
 */
public class QueueController extends CrudController {

    private final Repository repo;
    private final Database db;

    public QueueController(Repository repo, Database db) {
        this.repo = repo;
        this.db = db;
    }

    @Override
    protected void list(Call call) throws IOException {
        call.setJson(repo.findQueues(db));
    }

    @Override
    protected void show(Call call, String id) throws IOException {
        Queue q = repo.findQueue(db, id);
        if (q == null) {
            call.setCode(404);
        } else {
            call.setJson(q);
        }
    }
}
