package com.bluesky.common.user_management;

/**
 * Created by liangc on 16/05/15.
 */
public interface UserManagementService {
    /** get info of all groups
     *
     * @return GroupInfo, if error, then id == -1 and intro is the reason
     */
    public GroupInfo[] listGroup();

    /** get group members
     *
     * @param gid
     * @return array of members, empty for empty group or error
     */
    public UserInfo[] getGroupMembers(long gid);

    /** get groups that the uid belongs to
     *
     * @param uid
     * @return
     */
    public GroupInfo[] getMyGroups(long uid);

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
