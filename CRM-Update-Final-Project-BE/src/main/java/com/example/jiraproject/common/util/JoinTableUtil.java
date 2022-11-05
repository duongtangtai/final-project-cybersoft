package com.example.jiraproject.common.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class JoinTableUtil {
    //COMMON
    public static final String ROLE_ID = "J_ROLE_ID";
    public static final String USER_ID = "J_USER_ID";
    public static final String PROJECT_ID = "J_PROJECT_ID";

    //USER & ROLE
    public static final String USER_JOIN_WITH_ROLE = "J_USER_ROLE";
    public static final String ROLE_MAPPED_BY_USER = "roles";

    //USER & PROJECT
    public static final String PROJECT_CREATOR_REFERENCE_USER = "creator";
    public static final String PROJECT_LEADER_REFERENCE_USER = "leader";
    public static final String PROJECT_JOIN_WITH_USER = "J_PROJECT_USER";
    public static final String USER_MAPPED_BY_PROJECT = "users";

    //TASK & PROJECT
    public static final String TASK_REFERENCE_PROJECT = "project";
    //TASK & USER
    public static final String TASK_REFERENCE_USER = "reporter";

    //COMMENT & USER
    public static final String COMMENT_REFERENCE_USER = "writer";
    //COMMENT & TASK
    public static final String COMMENT_REFERENCE_TASK = "task";
    //COMMENT & COMMENT
    public static final String COMMENT_REFERENCE_COMMENT = "responseTo";

    //NOTIFICATION & USER
    public static final String NOTIFICATION_SENDER_REFERENCE_USER = "sender";
    public static final String NOTIFICATION_RECEIVER_REFERENCE_RECEIVER = "receiver";
}
