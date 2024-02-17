package org.doc.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.doc.enums.RoleEnum;

@Data
@TableName("db_user")
public class User {
    //    @Id
    @TableId
    private String account;

    private String password;

    private String avatarUrl;

    private String token;

    private RoleEnum role;

    private Long created_time;
    private Long updated_time;

    private String ip;

    public User() {
    }

    public User(User user) {
        account = user.getAccount();
        password = user.getPassword();
        avatarUrl = user.getAvatarUrl();
        token = user.getToken();
        role = user.getRole();
        created_time = user.getCreated_time();
        updated_time = user.getUpdated_time();
    }
}
