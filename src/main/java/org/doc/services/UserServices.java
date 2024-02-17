package org.doc.services;


import org.doc.entity.User;

public interface UserServices {


    /**
     * 根据账号查询
     *
     * @param account
     * @return
     */
    User findUserByAccount(String account);

    /**
     * 保存用户
     *
     * @param user
     * @return
     */
    User saveUser(User user);

    /**
     * 服务启动、销毁时清除用户token信息
     *
     * @return
     */
    boolean cleanUpToken();

    boolean modifyUser(User user);
}
