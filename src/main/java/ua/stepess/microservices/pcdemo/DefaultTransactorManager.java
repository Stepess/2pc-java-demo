package ua.stepess.microservices.pcdemo;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import org.postgresql.xa.PGXADataSource;

import javax.sql.XAConnection;
import javax.transaction.xa.XAException;
import javax.transaction.xa.XAResource;
import javax.transaction.xa.Xid;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

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

    public static void demo() {
        var flyDs = new PGXADataSource();
        flyDs.setUrl("jdbc:postgresql://localhost:5436/postgres");
        flyDs.setUser("postgres");
        flyDs.setPassword("postgres123");

        var hotelDs = new PGXADataSource();
        hotelDs.setUrl("jdbc:postgresql://localhost:5435/postgres");
        hotelDs.setUser("postgres");
        hotelDs.setPassword("postgres123");

        try {
            var xaFlyConn = flyDs.getXAConnection();
            var xaHotelConn = hotelDs.getXAConnection();

            XAResource xaRes1 = xaFlyConn.getXAResource();
            XAResource xaRes2 = xaHotelConn.getXAResource();

            XID xid = new XID(100, gtrid, bqual);
            xaRes1.start(xid, TMNOFLAGS);
            xaRes2.start(xid, TMNOFLAGS);

            Connection conn1 = xaFlyConn.getConnection();
            Statement st1 = conn1.createStatement();
            st1.execute("INSERT INTO fly.fly_booking" +
                    "(client_name, fly_number, arrival_place, departure_place, arrival_date) VALUES" +
                    "('Stepan Yersh1', 'KLM 1382', 'KBP', 'AMS', '01/05/2015')");

            Connection conn2 = xaHotelConn.getConnection();
            Statement st2 = conn2.createStatement();
            st2.execute("INSERT INTO " +
                    "hotel.hotel_booking(client_name, hotel_name, arrival_date, departure_date) VALUES" +
                    "('Stepan Yershov', 'Hilton', '01/05/2015', '07/05/2015')");


            xaRes1.end(xid, TMSUCCESS);
            xaRes2.end(xid, TMSUCCESS);

            try {
                int rc1 = xaRes1.prepare(xid);
                if (rc1 == javax.transaction.xa.XAResource.XA_OK) {
                    int rc2 = xaRes2.prepare(xid);
                    if (rc2 == javax.transaction.xa.XAResource.XA_OK) { // Both connections prepared successfully and neither was read-only.
                        xaRes1.commit(xid, false);
                        xaRes2.commit(xid, false);
                    } else if (rc2 == javax.transaction.xa.XAException.XA_RDONLY) { // The second connection is read-only, so just commit the
                        // first connection.
                        xaRes1.commit(xid, false);
                    }
                } else if (rc1 == javax.transaction.xa.XAException.XA_RDONLY) { // SQL for the first connection is read-only (such as a SELECT).
                    // The prepare committed it. Prepare the second connection.
                    int rc2 = xaRes2.prepare(xid);
                    if (rc2 == javax.transaction.xa.XAResource.XA_OK) { // The first connection is read-only but the second is not.
                        // Commit the second connection.
                        xaRes2.commit(xid, false);
                    } else if (rc2 == javax.transaction.xa.XAException.XA_RDONLY) { // Both connections are read-only, and both already committed,
                        // so there is nothing more to do.
                    }
                }

            } catch (javax.transaction.xa.XAException xae) { // Distributed transaction failed, so roll it back.
                // Report XAException on prepare/commit.
                System.out.println("Distributed transaction prepare/commit failed. " +
                        "Rolling it back.");
                System.out.println("XAException error code = " + xae.errorCode);
                System.out.println("XAException message = " + xae.getMessage());
                xae.printStackTrace();
                try {
                    xaRes1.rollback(xid);
                } catch (javax.transaction.xa.XAException xae1) { // Report failure of rollback.
                    conn1.rollback();
                    System.out.println("distributed Transaction rollback xares1 failed");
                    System.out.println("XAException error code = " + xae1.errorCode);
                    System.out.println("XAException message = " + xae1.getMessage());
                }
                try {
                    xaRes2.rollback(xid);
                } catch (javax.transaction.xa.XAException xae2) { // Report failure of rollback.
                    System.out.println("distributed Transaction rollback xares2 failed");
                    System.out.println("XAException error code = " + xae2.errorCode);
                    System.out.println("XAException message = " + xae2.getMessage());
                }
            }

        } catch (SQLException sqe) {
            System.out.println("SQLException caught: " + sqe.getMessage());
            sqe.printStackTrace();
        }
        catch (XAException xae) {
            System.out.println("XA error is " + xae.getMessage());
            xae.printStackTrace();
        }


    }

    public static void main(String[] args) {
        demo();
    }

}
/* PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO " +
                    "fly.fly_booking(client_name, fly_number, arrival_place, departure_place, arrival_date) " +
                    "VALUES (?, ?, ?, ?, ?)");*/