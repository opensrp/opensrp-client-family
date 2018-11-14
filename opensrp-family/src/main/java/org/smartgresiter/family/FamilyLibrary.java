package org.smartgresiter.family;

import org.smartregister.Context;
import org.smartregister.CoreLibrary;
import org.smartregister.repository.Repository;
import org.smartregister.repository.UniqueIdRepository;

/**
 * Created by keyman on 31/07/17.
 */
public class FamilyLibrary {
    private static FamilyLibrary instance;

    private final Context context;
    private final Repository repository;

    private int applicationVersion;
    private int databaseVersion;

    private UniqueIdRepository uniqueIdRepository;

    public static void init(Context context, Repository repository, int applicationVersion, int databaseVersion) {
        if (instance == null) {
            instance = new FamilyLibrary(context, repository, applicationVersion, databaseVersion);
        }
    }


    public static FamilyLibrary getInstance() {
        if (instance == null) {
            throw new IllegalStateException(" Instance does not exist!!! Call "
                    + CoreLibrary.class.getName()
                    + ".init method in the onCreate method of "
                    + "your Application class ");
        }
        return instance;
    }

    private FamilyLibrary(Context contextArg, Repository repositoryArg, int applicationVersion, int databaseVersion) {
        this.context = contextArg;
        this.repository = repositoryArg;
        this.applicationVersion = applicationVersion;
        this.databaseVersion = databaseVersion;
    }

    public Context context() {
        return context;
    }

    public Repository getRepository() {
        return repository;
    }

    public int getApplicationVersion() {
        return applicationVersion;
    }

    public int getDatabaseVersion() {
        return databaseVersion;
    }

    public UniqueIdRepository getUniqueIdRepository() {
        if (uniqueIdRepository == null) {
            uniqueIdRepository = new UniqueIdRepository(getRepository());
        }
        return uniqueIdRepository;
    }

    /**
     * Use this method when testing.
     * It should replace org.smartregister.Context#setInstance(org.smartregister.Context, org.smartregister.repository.Repository) which has been removed
     *
     * @param context
     */
    public static void reset(Context context, Repository repository, int applicationVersion, int databaseVersion) {
        if (context != null) {
            instance = new FamilyLibrary(context, repository, applicationVersion, databaseVersion);
        }
    }
}
