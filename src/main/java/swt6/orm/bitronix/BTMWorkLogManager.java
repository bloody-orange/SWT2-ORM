package swt6.orm.bitronix;

import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.destination.DriverManagerDestination;
import swt6.orm.DataOperations;
import swt6.orm.dao.*;
import swt6.orm.dao.impl.*;
import swt6.orm.domain.*;
import swt6.orm.persistence.PersistenceManager;
import swt6.orm.persistence.PersistenceManagerFactory;
import swt6.util.DateUtil;

public class BTMWorkLogManager {
    private static PersistenceManager mgr = PersistenceManagerFactory.getManager();

    public static void main(String[] args) {
        try {
            mgr.initFactory();
            new DbSetup(new DriverManagerDestination("jdbc:derby://localhost:1527/worklogdb;create=true",
                            "app", "derby"), DataOperations.DELETE_INSERT_ALL)
                    .launch();

        } finally {
            mgr.closeFactory();
        }
    }

    private static void addEntities() {
        markTransactionStart("Create employees");

        markTransactionEnd();
    }

    private static void markTransactionStart(String name) {
        System.out.println("!--------------     TRANSACTION START     --------------!");
        String leftIndent = "";
        String rightIndent = "";
        for (int i = 0; i < (25 - name.length()) / 2; ++i) {
            leftIndent += " ";
        }
        for (int i = 0; i < (26 - name.length()) / 2; ++i) {
            rightIndent += " ";
        }

        System.out.println("!-------------- " + leftIndent + name.toUpperCase() + rightIndent + " --------------!");
    }

    private static void markTransactionEnd() {
        System.out.println("!--------------      TRANSACTION END      --------------!");
    }
}
