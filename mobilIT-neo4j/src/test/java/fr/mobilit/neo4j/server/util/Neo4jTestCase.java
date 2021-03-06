/**
 * This file is part of MobilIT.
 *
 * MobilIT is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * MobilIT is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with MobilIT. If not, see <http://www.gnu.org/licenses/>.
 * 
 * @See https://github.com/sim51/mobilIT
 */
package fr.mobilit.neo4j.server.util;

import java.io.File;

import junit.framework.TestCase;

import org.neo4j.gis.spatial.SpatialDatabaseService;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;
import org.neo4j.kernel.EmbeddedGraphDatabase;
import org.neo4j.kernel.impl.nioneo.store.PropertyStore;

/**
 * Base class for neo4j tests.
 */
public abstract class Neo4jTestCase extends TestCase {

    private static File            dbPath = new File("neo4j-test");
    private GraphDatabaseService   graphDb;
    private SpatialDatabaseService spatial;
    private Transaction            tx;

    protected void setUp(boolean resetDb) throws Exception {
        setUp(resetDb, false, false);
        spatial = new SpatialDatabaseService(graphDb);
    }

    /**
     * Configurable options for text cases, with or without deleting the previous database, and with or without using
     * the BatchInserter for higher creation speeds. Note that tests that need to delete nodes or use transactions
     * should not use the BatchInserter.
     * 
     * @param deleteDb
     * @param useBatchInserter
     * @throws Exception
     */
    protected void setUp(boolean deleteDb, boolean useBatchInserter, boolean autoTx) throws Exception {
        super.setUp();
        reActivateDatabase(deleteDb, useBatchInserter, autoTx);
    }

    /**
     * Some tests require switching between normal EmbeddedGraphDatabase and BatchInserter, so we allow that with this
     * method. We also allow deleting the previous database, if that is desired (probably only the first time this is
     * called).
     * 
     * @param deleteDb
     * @param useBatchInserter
     * @throws Exception
     */
    protected void reActivateDatabase(boolean deleteDb, boolean useBatchInserter, boolean autoTx) throws Exception {
        if (tx != null) {
            tx.success();
            tx.finish();
            tx = null;
        }
        if (graphDb != null) {
            graphDb.shutdown(); // shuts down batchInserter also, if this was made from that
            graphDb = null;
        }
        if (deleteDb) {
            deleteDatabase();
        }
        graphDb = new EmbeddedGraphDatabase(dbPath.getAbsolutePath());
        if (autoTx) {
            // with the batch inserter the tx is a dummy that simply succeeds all the time
            tx = graphDb.beginTx();
        }
    }

    @Override
    protected void tearDown() throws Exception {
        if (tx != null) {
            tx.success();
            tx.finish();
        }
        beforeShutdown();
        graphDb.shutdown();
        super.tearDown();
    }

    protected void beforeShutdown() {
    }

    protected File getNeoPath() {
        return dbPath;
    }

    protected static void deleteDatabase() {
        deleteFileOrDirectory(dbPath);
    }

    protected static void deleteFileOrDirectory(File file) {
        if (!file.exists()) {
            return;
        }

        if (file.isDirectory()) {
            for (File child : file.listFiles()) {
                deleteFileOrDirectory(child);
            }
        }
        else {
            file.delete();
        }
    }

    protected long calculateDiskUsage(File file) {
        if (file.isDirectory()) {
            long count = 0;
            for (File sub : file.listFiles()) {
                count += calculateDiskUsage(sub);
            }
            return count;
        }
        else {
            return file.length();
        }
    }

    protected long databaseDiskUsage() {
        return calculateDiskUsage(dbPath);
    }

    protected long countNodes(Class<?> cls) {
        return ((EmbeddedGraphDatabase) graphDb).getNodeManager().getNumberOfIdsInUse(cls);
    }

    protected void printDatabaseStats() {
        System.out.println("Database stats:");
        System.out.println("\tTotal disk usage: " + ((float) databaseDiskUsage()) / (1024.0 * 1024.0) + "MB");
        System.out.println("\tTotal # nodes:    " + countNodes(Node.class));
        System.out.println("\tTotal # rels:     " + countNodes(Relationship.class));
        System.out.println("\tTotal # props:    " + countNodes(PropertyStore.class));
    }

    protected void restartTx() {
        restartTx(true);
    }

    protected void restartTx(boolean success) {
        if (tx != null) {
            if (success) {
                tx.success();
            }
            else {
                tx.failure();
            }
            tx.finish();
            tx = graphDb.beginTx();
        }
    }

    protected GraphDatabaseService graphDb() {
        return graphDb;
    }

    protected SpatialDatabaseService spatial() {
        return spatial;
    }

}
