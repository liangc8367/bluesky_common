package com.bluesky.common;

import java.net.InetSocketAddress;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * subscriber database contains the records of all legitimate subs and groups, and
 * contains the records of sign-up relations, and the records of online sub's net addresses.
 *
 * it provides following methods:
 * - for group management:
 *      + add, remove, update
 * - for sub management
 *      + add, remove, update
 * - for sub-group relation
 *      + sign-up, unsign
 * - for online info
 *      + online, offline
 * - for query
 *      + getOnlineMembers
 *
 * Created by liangc on 08/02/15.
 */
public class SubscriberDatabase {

    private static class Subscriber {
        public long su_id = 0;
        public long default_group = -1; // illegal group id
        public final HashSet<Long> groups = new HashSet<Long>();
    }

    private static class Group {
        public long grp_id = 0;
        public final HashSet<Long> subs = new HashSet<Long>();
    }

    private final HashMap<Long, Subscriber> mSubscribers = new HashMap<Long, Subscriber>();
    private final HashMap<Long, Group> mGroups = new HashMap<Long, Group>();

    public static class OnlineRecord{
        public OnlineRecord(long su_id, InetSocketAddress addr){
            this.su_id = su_id;
            this.addr = addr;
        }
        public long su_id;
        public InetSocketAddress addr;


        /** equal means two records are for the same sub
         *
         * @param obj
         * @return
         */
        @Override
        public boolean equals(Object obj) {
            if( this == obj ){
                return true;
            }

            boolean equal = false;
            if(obj instanceof OnlineRecord ){
                return su_id == ((OnlineRecord)obj).su_id;
            }
            return equal;
        }

        @Override
        public String toString() {
            return "" + su_id + ":" + addr;
        }
    }

    private final HashMap<Long, CopyOnWriteArrayList<OnlineRecord>> mGroupOnlineMembers
            = new HashMap<Long, CopyOnWriteArrayList<OnlineRecord>>();
    private final CopyOnWriteArrayList<OnlineRecord> EMPTY_GROUP = new CopyOnWriteArrayList<OnlineRecord>();

    public boolean hasSubscriber(long su_id){
        return mSubscribers.containsKey(su_id);
    }

    public boolean hasGroup(long grp_id){
        return mGroups.containsKey(grp_id);
    }

    public boolean isGroupMember(long su_id, long grp_id){
        Subscriber su = mSubscribers.get(su_id);
        if(su == null){
            return false;
        }

        return su.groups.contains(grp_id);
    }

    public List<Long> getGroupMember(long grp_id){
        return new ArrayList<Long>(mGroups.get(grp_id).subs);
    }

    /** get all registered subscribers
     *
     * @return
     */
    public List<Long> getSubscribers(){
        Set<Long> subs = mSubscribers.keySet();
        List<Long> listOfSubs = new ArrayList<Long>();
        listOfSubs.addAll(subs);
        return listOfSubs;
    }

    /** get the groups the sub belongs to, the first grp is the default grp
     *
     * @param sub_id
     * @return empty list if the sub is not registered
     */
    public List<Long> getGroups(long sub_id){
        List<Long> listOfGroups = new ArrayList<Long>();
        Subscriber su = mSubscribers.get(sub_id);
        if(su != null){
            listOfGroups.add(su.default_group);
            Iterator iter = su.groups.iterator();
            while(iter.hasNext()){
                Long grp_id = (Long)iter.next();
                if(grp_id != su.default_group){
                    listOfGroups.add(grp_id);
                }
            }
        }
        return listOfGroups;
    }

    public void addSubscriber(long su_id){
        if(hasSubscriber(su_id)){
            return;
        }
        Subscriber sub = new Subscriber();
        sub.su_id = su_id;
        mSubscribers.put(su_id, sub);
    }

    public void addGroup(long grp_id){
        if(hasGroup(grp_id)){
            return;
        }
        Group grp = new Group();
        grp.grp_id = grp_id;
        mGroups.put(grp_id, grp);
        mGroupOnlineMembers.put(grp_id, new CopyOnWriteArrayList<OnlineRecord>());
    }

    public void signup(long su_id, long grp_id){
        if(!hasSubscriber(su_id) || !hasGroup(grp_id)){
            return;
        }
        Subscriber sub = mSubscribers.get(su_id);
        sub.groups.add(grp_id);
        mGroups.get(grp_id).subs.add(su_id);

        if(sub.default_group == -1){
            sub.default_group = grp_id;
        }
    }

    //// online methods
    public void online(long su_id, InetSocketAddress addr){
        OnlineRecord record = new OnlineRecord(su_id, addr);

        for( long group : getGroups(su_id)){
            CopyOnWriteArrayList<OnlineRecord> onlineMembers = mGroupOnlineMembers.get(group);
            onlineMembers.remove(record);
            onlineMembers.add(record);
        }
    }

    public void offline(long su_id){
        OnlineRecord record = new OnlineRecord(su_id, new InetSocketAddress(0));
        for( long group : getGroups(su_id)) {
            CopyOnWriteArrayList<OnlineRecord> onlineMembers = mGroupOnlineMembers.get(group);
            onlineMembers.remove(record);
        }
    }

    public List<OnlineRecord> getOnlineMembers(long group){
        CopyOnWriteArrayList<OnlineRecord> onlineMembers = mGroupOnlineMembers.get(group);
        if( onlineMembers != null) {
            return onlineMembers;
        } else {
            return EMPTY_GROUP;
        }
    }


}
