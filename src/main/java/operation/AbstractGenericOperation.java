/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.operation;

import main.java.dbb.DBRepository;
import main.java.dbb.impl.RepositoryDBGeneric;
import main.java.domain.GenericEntity;
import main.java.repository.Repository;

public abstract class AbstractGenericOperation<T extends GenericEntity, R> {
    protected final Repository repository;

    public AbstractGenericOperation() {
        this.repository = new RepositoryDBGeneric();
    }

    public final void execute(T entity) throws Exception {
        try {
            preconditions(entity);
            startTransaction();
            executeOperation(entity);
            commitTransaction();
        } catch (Exception ex) {
            ex.printStackTrace();
            rollbackTransaction();
            throw ex;
        } finally {
            disconnect();
        }
    }

    protected abstract void preconditions(T entity) throws Exception;

    private void startTransaction() throws Exception {
        ((DBRepository) repository).connect();
    }

    protected abstract void executeOperation(T entity) throws Exception;

    private void commitTransaction() throws Exception {
        ((DBRepository) repository).commit();
    }

    private void rollbackTransaction() throws Exception {
        ((DBRepository) repository).rollback();
    }

    private void disconnect() throws Exception {
        ((DBRepository) repository).disconnect();
    }
}
