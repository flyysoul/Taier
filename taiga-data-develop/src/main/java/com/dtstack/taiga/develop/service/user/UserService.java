package com.dtstack.taiga.develop.service.user;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dtstack.taiga.common.enums.Deleted;
import com.dtstack.taiga.dao.domain.ScheduleTaskShade;
import com.dtstack.taiga.dao.domain.User;
import com.dtstack.taiga.dao.dto.UserDTO;
import com.dtstack.taiga.dao.mapper.ScheduleTaskShadeMapper;
import com.dtstack.taiga.dao.mapper.UserMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService extends ServiceImpl<UserMapper, User> {


    public String getUserName(Long userId) {
        User user = this.baseMapper.selectById(userId);
        return null == user ? "" : user.getUserName();
    }

    public Map<Long, User> getUserMap(Collection<Long> userIds) {
        List<User> users = this.baseMapper.selectBatchIds(userIds);
        if(CollectionUtils.isEmpty(users)){
            return new HashMap<>();
        }
        return users.stream().collect(Collectors.toMap(User::getId, u -> u));
    }

    public User getById(Long userId) {
       return this.baseMapper.selectById(userId);
    }


    public List<User> listAll() {
        return this.baseMapper.selectList(Wrappers.lambdaQuery(User.class).eq(User::getIsDeleted, Deleted.NORMAL.getStatus()));
    }

    public User getByUserName(String username) {
        return this.baseMapper.selectOne(Wrappers.lambdaQuery(User.class).eq(User::getUserName, username));
    }

    public UserDTO getUserByDTO(Long userId) {
        if (userId == null) {
            return null;
        }
        User one = getById(userId);
        if (Objects.isNull(one)) {
            return null;
        }
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(one, userDTO);
        return userDTO;
    }
}
