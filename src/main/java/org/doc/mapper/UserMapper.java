package org.doc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.doc.entity.User;

/**
 * @Description:
 * @Date: Created in 19:54 2023/7/11
 * @Author:vicente
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
    User findUserByAccount(@Param("account") String account);
    boolean existsByAccount(@Param("account") String account);

    @Update("update ln_user set token = ''")
    int cleanUpToken();

    int modifyUser(User user);
}
