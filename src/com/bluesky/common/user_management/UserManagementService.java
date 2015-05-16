package com.bluesky.common.user_management;

/**
 * Created by liangc on 16/05/15.
 */
public interface UserManagementService {
    public GroupInfo[] getGroupsInfo(long gid);
    public UserInfo[] getGroupMembersInfo(long gid);

    /** sign up user
     *
     * @param name
     * @param intro
     * @return user info, failed if uid==-1
     */
    public UserInfo signUp(final String name, final String intro);

    /** join group
     *
     * @param uid
     * @param gid
     * @return true if success
     */
    public boolean joinGroup(long uid, long gid);
}
