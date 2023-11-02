package com.heima.user.service.Impl;


import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.user.dtos.LoginDto;
import com.heima.user.mapper.ApUserMapper;
import com.heima.user.pojos.ApUser;
import com.heima.user.service.ApUserService;
import com.heima.utils.common.AppJwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.HashMap;
import java.util.Map;

@Service
@Transactional
@Slf4j
public class ApUserServiceImpl extends ServiceImpl<ApUserMapper, ApUser> implements ApUserService {
    @Override
    public ResponseResult login(LoginDto dto) {
        System.out.println(dto);
        if (StringUtils.isNotBlank(dto.getPhone()) && StringUtils.isNotBlank(dto.getPassword())){
            ApUser dpUser = getOne(Wrappers.<ApUser>lambdaQuery().eq(ApUser::getPhone, dto.getPhone()));

            System.out.println(dpUser);
            if (dpUser==null){
                return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST,"用户信息不存在");
            }

            String salt = dpUser.getSalt();
            String password = dto.getPassword();
            String s = DigestUtils.md5DigestAsHex((password + salt).getBytes());
            if (!s.equals(dpUser.getPassword())){
                return ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_PASSWORD_ERROR);
            }
            String token = AppJwtUtil.getToken(dpUser.getId().longValue());
            Map<String,Object> map = new HashMap<>();
            map.put("token",token);
            dpUser.setSalt("");
            dpUser.setPassword("");
            map.put("user",dpUser);

            return ResponseResult.okResult(map);
        }else {
            Map<String,Object> map = new HashMap<>();
            map.put("token",AppJwtUtil.getToken(0L));
            return ResponseResult.okResult(map);
        }
    }
}
