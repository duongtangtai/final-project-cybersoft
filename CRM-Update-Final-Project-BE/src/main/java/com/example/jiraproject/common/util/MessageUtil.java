package com.example.jiraproject.common.util;

import com.example.jiraproject.notification.model.Notification;
import com.example.jiraproject.operation.model.Operation;
import com.example.jiraproject.task.model.Task;
import com.example.jiraproject.user.model.User;
import lombok.experimental.UtilityClass;
import org.springframework.context.MessageSource;

import java.util.Arrays;
import java.util.Locale;

@UtilityClass
public class MessageUtil {
    public static final String UUID_NOT_FOUND = "Không tìm thấy mã ID(UUID) trùng khớp";
    public static final String INVALID_UUID_FORMAT = "Mã ID(UUID) không hợp lệ";
    public static final String INVALID_DATE_FORMAT
            = "Date Format không hợp lệ. Date Format yêu cầu: " + DateTimeUtil.DATE_FORMAT;
    public static final String INVALID_OPERATION_TYPE
            = "Loại chức năng không hợp lệ. Loại chức năng gồm: " + Arrays.toString(Operation.Type.values());
    public static final String INVALID_USER_ACCOUNT_STATUS
            = "Trạng thái người dùng không hợp lệ. Trạng thái gồm: " + Arrays.toString(User.AccountStatus.values());
    public static final String INVALID_TASK_STATUS
            = "Trạng thái công việc không hợp lệ. Trạng thái công việc gồm: " + Arrays.toString(Task.Status.values());
    public static final String INVALID_NOTIFICATION_STATUS
            = "Trạng thái thông báo không hợp lệ. Trạng thái thông báo gồm: " + Arrays.toString(Notification.Status.values());
    public static String getMessage(MessageSource messageSource, String messageCode) {
        return messageSource.getMessage(messageCode, null, Locale.getDefault());
    }
}
