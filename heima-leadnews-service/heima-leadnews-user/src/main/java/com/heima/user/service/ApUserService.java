package com.heima.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.user.dtos.LoginDto;
import com.heima.user.pojos.ApUser;

public interface ApUserService extends IService<ApUser>{
    public ResponseResult login(LoginDto dto);
}
