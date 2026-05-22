package com.hellcourt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hellcourt.entity.Demon;
import com.hellcourt.mapper.DemonMapper;
import com.hellcourt.service.DemonService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "demon")
public class DemonServiceImpl implements DemonService {
    private final DemonMapper demonMapper;

    @Override
    @Cacheable(key = "'all'")
    public List<Demon> listAll() {
        return demonMapper.selectList(
                new LambdaQueryWrapper<Demon>().orderByAsc(Demon::getId));
    }
}
