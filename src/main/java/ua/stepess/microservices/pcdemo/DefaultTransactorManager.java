package ua.stepess.microservices.pcdemo;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import org.postgresql.xa.PGXADataSource;

import javax.sql.XAConnection;
import javax.transaction.xa.XAException;
import javax.transaction.xa.XAResource;
import javax.transaction.xa.Xid;
import java.sql.SQLException;

import static javax.transaction.xa.XAResource.TMNOFLAGS;
import static javax.transaction.xa.XAResource.TMSUCCESS;

public class DefaultTransactorManager {

    public static byte[] gtrid = new byte[] { 0x44, 0x11, 0x55, 0x66 };
    public static byte[] bqual = new byte[] { 0x00, 0x22, 0x00 };

    @EqualsAndHashCode
    @AllArgsConstructor
    public static class XID implements Xid {

        private Integer formatId;
        private byte[] globalTxId;
        private byte[] branchQualifier;

        @Override
        public int getFormatId() {
            return formatId;
        }

        @Override
        public byte[] getGlobalTransactionId() {
            return globalTxId;
        }

        @Override
        public byte[] getBranchQualifier() {
            return branchQualifier;
        }
    }

    public static void twoPhaseCommitTransaction() {
        var flyDataSource = new PGXADataSource();
        flyDataSource.setUrl("jdbc:postgresql://localhost:5436/postgres");
        flyDataSource.setUser("postgres");
        flyDataSource.setPassword("postgres123");

        var hotelDataSource = new PGXADataSource();
        hotelDataSource.setUrl("jdbc:postgresql://localhost:5435/postgres");
        hotelDataSource.setUser("postgres");
        hotelDataSource.setPassword("postgres123");

        var moneyDataSource = new PGXADataSource();
        moneyDataSource.setUrl("jdbc:postgresql://localhost:5437/postgres");
        moneyDataSource.setUser("postgres");
        moneyDataSource.setPassword("postgres123");

        var xid = new XID(100, gtrid, bqual);

        try {

            var insertFlyQuery = "INSERT INTO fly.fly_booking" +
                    "(client_name, fly_number, arrival_place, departure_place, arrival_date) " +
                    "VALUES ('Stepan Yershov', 'KLM 1382', 'KBP', 'AMS', '01/05/2015')";

            var xaFlyResource = prepareTransaction(flyDataSource, xid, insertFlyQuery);

            var insertHotelQuery = "INSERT INTO hotel.hotel_booking" +
                    "(client_name, hotel_name, arrival_date, departure_date) " +
                    "VALUES ('Stepan Yershov', 'Hilton', '01/05/2015', '07/05/2015')";

            var xaHotelResource = prepareTransaction(hotelDataSource, xid, insertHotelQuery);

            var updateMoneyQuery = "UPDATE money.money " +
                    "SET amount = amount - 0" +
                    "WHERE client_name = 'Stepan'";

            var xaMoneyResource = prepareTransaction(moneyDataSource, xid,updateMoneyQuery);

            commitTwoPhase(xaFlyResource, xaHotelResource, xaMoneyResource, xid);

        } catch (SQLException sqe) {
            System.out.println("SQLException caught: " + sqe.getMessage());
            sqe.printStackTrace();
        }
        catch (XAException xae) {
            System.out.println("XA error is " + xae.getMessage());
            xae.printStackTrace();
        }


    }

    private static XAResource prepareTransaction(PGXADataSource dataSource,
                                                   Xid xid, String query) throws XAException, SQLException {
        var xaConnection = dataSource.getXAConnection();
        var xaResource = xaConnection.getXAResource();

        xaResource.start(xid, TMNOFLAGS);
        executeQuery(xaConnection, query);
        xaResource.end(xid, TMSUCCESS);

        return xaResource;
    }

    private static void executeQuery(XAConnection xaConnection, String query) throws SQLException {
        var jdbcConnection = xaConnection.getConnection();
        var statement = jdbcConnection.createStatement();
        statement.execute(query);
    }

    private static void commitTwoPhase(XAResource xaRes1, XAResource xaRes2, XAResource xaRes3,  XID xid) {
        try {
            xaRes1.prepare(xid);
            xaRes2.prepare(xid);
            xaRes3.prepare(xid);

            xaRes1.commit(xid, false);
            xaRes2.commit(xid, false);
            xaRes3.commit(xid, false);
        } catch (XAException e) {
            try {
                xaRes1.rollback(xid);
                xaRes2.rollback(xid);
                xaRes3.rollback(xid);
            } catch (XAException ex) {
                ex.printStackTrace();
            }
        }

    }

    private static void commitTwoPhase(XAResource xaRes1, XAResource xaRes2, XID xid) {
        try {
            int rc1 = xaRes1.prepare(xid);
            if (rc1 == XAResource.XA_OK) {
                int rc2 = xaRes2.prepare(xid);
                if (rc2 == XAResource.XA_OK) {
                    xaRes1.commit(xid, false);
                    xaRes2.commit(xid, false);
                } else if (rc2 == XAException.XA_RDONLY) {
                    xaRes1.commit(xid, false);
                }
            } else if (rc1 == XAException.XA_RDONLY) {
                int rc2 = xaRes2.prepare(xid);
                if (rc2 == XAResource.XA_OK) {
                    xaRes2.commit(xid, false);
                }
            }

        } catch (XAException xae) {
            System.out.println("Distributed transaction prepare/commit failed. Rolling it back.");
            System.out.println("XAException error code = " + xae.errorCode);
            System.out.println("XAException message = " + xae.getMessage());
            xae.printStackTrace();
            try {
                xaRes1.rollback(xid);
            } catch (XAException xae1) {
                System.out.println("distributed Transaction rollback xaRes1 failed");
                System.out.println("XAException error code = " + xae1.errorCode);
                System.out.println("XAException message = " + xae1.getMessage());
            }
            try {
                xaRes2.rollback(xid);
            } catch (XAException xae2) {
                System.out.println("distributed Transaction rollback xaRes2 failed");
                System.out.println("XAException error code = " + xae2.errorCode);
                System.out.println("XAException message = " + xae2.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        twoPhaseCommitTransaction();
    }

}
