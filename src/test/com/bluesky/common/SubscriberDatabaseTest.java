package test.com.bluesky.common; 

import com.bluesky.common.SubscriberDatabase;
import org.junit.Test;
import org.junit.Before; 
import org.junit.After;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Iterator;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/** 
* SubscriberDatabase Tester. 
* 
* @author <Authors name> 
* @since <pre>Apr 1, 2015</pre> 
* @version 1.0 
*/ 
public class SubscriberDatabaseTest { 

@Before
public void before() throws Exception { 
} 

@After
public void after() throws Exception { 
} 

@Test
public void test_OnlineRecord_equals() throws Exception {
    long su1 = 100, su2 = 200;
    InetSocketAddress addr1 = new InetSocketAddress(1), addr2 = new InetSocketAddress(2);
    SubscriberDatabase.OnlineRecord record1 = new SubscriberDatabase.OnlineRecord(su1, addr1);
    SubscriberDatabase.OnlineRecord record2 = new SubscriberDatabase.OnlineRecord(su1, addr2);
    SubscriberDatabase.OnlineRecord record3 = new SubscriberDatabase.OnlineRecord(su2, addr1);

    assertTrue(record1.equals(record1));
    assertTrue(record1.equals(record2));
    assertFalse(record1.equals(record3));
}

    SubscriberDatabase database = new SubscriberDatabase();
    long su1 = 100, su2 = 200, su3 = 300, su4 = 400, su5 = 500;
    long grp1 = 1000, grp2 = 2000, grp3 = 3000;
    InetSocketAddress addr1 = new InetSocketAddress(1), addr2 = new InetSocketAddress(2),
        addr3 = new InetSocketAddress(3);

    /**
     *      grp1    grp2    grp3
     * su1  Y       Y       Y
     * su2          Y       Y
     * su3                  Y
     * su4  Y       Y
     * su5  Y               Y
     */
private void initDatabase(){
    database.addSubscriber(su1);
    database.addSubscriber(su2);
    database.addSubscriber(su3);
    database.addSubscriber(su4);
    database.addSubscriber(su5);

    database.addGroup(grp1);
    database.addGroup(grp2);
    database.addGroup(grp3);

    database.signup(su1, grp1);
    database.signup(su1, grp2);
    database.signup(su1, grp3);

    database.signup(su2, grp2);
    database.signup(su2, grp3);

    database.signup(su3, grp3);

    database.signup(su4, grp1);
    database.signup(su4, grp2);

    database.signup(su5, grp1);
    database.signup(su1, grp3);

}

private boolean isGroupHasOnlineMember(Iterator<SubscriberDatabase.OnlineRecord>list, long su, InetSocketAddress addr){
    while(list.hasNext()){
        SubscriberDatabase.OnlineRecord record = list.next();
        if(record.su_id == su && record.addr.equals(addr)){
            return true;
        }
    }

    return false;
}

@Test
public void test_online_shall_update_onlineMembers() throws Exception {
    initDatabase();

    // su1 online, verify that it shows up on all group
    database.online(su1, addr1);
    assertTrue(isGroupHasOnlineMember(database.getOnlineMembers(grp1).iterator(), su1, addr1));
    assertTrue(isGroupHasOnlineMember(database.getOnlineMembers(grp2).iterator(), su1, addr1));
    assertTrue(isGroupHasOnlineMember(database.getOnlineMembers(grp3).iterator(), su1, addr1));

    database.online(su2, addr2);
    database.online(su3, addr3);

    // verify that grp1 has 1,
    // verify that grp2 has 1, 2,
    // verify that grp3 has 1, 2, 3
    assertTrue(isGroupHasOnlineMember(database.getOnlineMembers(grp1).iterator(), su1, addr1));
    assertFalse(isGroupHasOnlineMember(database.getOnlineMembers(grp1).iterator(), su2, addr2));
    assertFalse(isGroupHasOnlineMember(database.getOnlineMembers(grp1).iterator(), su3, addr3));

    assertTrue(isGroupHasOnlineMember(database.getOnlineMembers(grp2).iterator(), su1, addr1));
    assertTrue(isGroupHasOnlineMember(database.getOnlineMembers(grp2).iterator(), su2, addr2));
    assertFalse(isGroupHasOnlineMember(database.getOnlineMembers(grp2).iterator(), su3, addr3));

    assertTrue(isGroupHasOnlineMember(database.getOnlineMembers(grp3).iterator(), su1, addr1));
    assertTrue(isGroupHasOnlineMember(database.getOnlineMembers(grp3).iterator(), su2, addr2));
    assertTrue(isGroupHasOnlineMember(database.getOnlineMembers(grp3).iterator(), su3, addr3));

    database.offline(su2);
    assertTrue(isGroupHasOnlineMember(database.getOnlineMembers(grp1).iterator(), su1, addr1));
    assertFalse(isGroupHasOnlineMember(database.getOnlineMembers(grp1).iterator(), su2, addr2));
    assertFalse(isGroupHasOnlineMember(database.getOnlineMembers(grp1).iterator(), su3, addr3));

    assertTrue(isGroupHasOnlineMember(database.getOnlineMembers(grp2).iterator(), su1, addr1));
    assertFalse(isGroupHasOnlineMember(database.getOnlineMembers(grp2).iterator(), su2, addr2));
    assertFalse(isGroupHasOnlineMember(database.getOnlineMembers(grp2).iterator(), su3, addr3));

    assertTrue(isGroupHasOnlineMember(database.getOnlineMembers(grp3).iterator(), su1, addr1));
    assertFalse(isGroupHasOnlineMember(database.getOnlineMembers(grp3).iterator(), su2, addr2));
    assertTrue(isGroupHasOnlineMember(database.getOnlineMembers(grp3).iterator(), su3, addr3));

    database.offline(su1);
    assertFalse(isGroupHasOnlineMember(database.getOnlineMembers(grp1).iterator(), su1, addr1));
    assertFalse(isGroupHasOnlineMember(database.getOnlineMembers(grp1).iterator(), su2, addr2));
    assertFalse(isGroupHasOnlineMember(database.getOnlineMembers(grp1).iterator(), su3, addr3));

    assertFalse(isGroupHasOnlineMember(database.getOnlineMembers(grp2).iterator(), su1, addr1));
    assertFalse(isGroupHasOnlineMember(database.getOnlineMembers(grp2).iterator(), su2, addr2));
    assertFalse(isGroupHasOnlineMember(database.getOnlineMembers(grp2).iterator(), su3, addr3));

    assertFalse(isGroupHasOnlineMember(database.getOnlineMembers(grp3).iterator(), su1, addr1));
    assertFalse(isGroupHasOnlineMember(database.getOnlineMembers(grp3).iterator(), su2, addr2));
    assertTrue(isGroupHasOnlineMember(database.getOnlineMembers(grp3).iterator(), su3, addr3));

}

} 
