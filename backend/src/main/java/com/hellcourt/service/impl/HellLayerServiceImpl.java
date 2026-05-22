package com.hellcourt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hellcourt.entity.HellLayer;
import com.hellcourt.mapper.HellLayerMapper;
import com.hellcourt.service.HellLayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "hellLayer")
public class HellLayerServiceImpl implements HellLayerService {
    private final HellLayerMapper hellLayerMapper;

    @Override
    @Cacheable(key = "'all'")
    public List<HellLayer> listAll() {
        return hellLayerMapper.selectList(
                new LambdaQueryWrapper<HellLayer>().orderByAsc(HellLayer::getLayerNum));
    }
}
