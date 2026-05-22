package com.hellcourt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hellcourt.common.annotation.OpLog;
import com.hellcourt.common.constant.SystemConstant;
import com.hellcourt.common.exception.BusinessException;
import com.hellcourt.dto.request.SoulPageRequest;
import com.hellcourt.dto.request.SoulRequest;
import com.hellcourt.entity.Soul;
import com.hellcourt.mapper.SoulMapper;
import com.hellcourt.service.SoulService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "soul")
public class SoulServiceImpl implements SoulService {
    private final SoulMapper soulMapper;

    @Override
    public IPage<Soul> page(SoulPageRequest request) {
        LambdaQueryWrapper<Soul> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(request.getName())) {
            wrapper.like(Soul::getName, request.getName());
        }
        if (request.getSoulStatus() != null) {
            wrapper.eq(Soul::getSoulStatus, request.getSoulStatus());
        }
        wrapper.orderByDesc(Soul::getCreatedAt);
        return soulMapper.selectPage(new Page<>(request.getPage(), request.getSize()), wrapper);
    }

    @Override
    @Cacheable(value = "soul", key = "#id")
    public Soul getById(Long id) {
        Soul soul = soulMapper.selectById(id);
        if (soul == null) throw new BusinessException("魂魄不存在");
        return soul;
    }

    @Override
    @OpLog(action = "新增魂魄", targetType = "soul")
    public void create(SoulRequest request, Long userId) {
        Soul soul = new Soul();
        soul.setName(request.getName());
        soul.setGender(request.getGender());
        soul.setBirthDate(request.getBirthDate());
        soul.setDeathDate(request.getDeathDate());
        soul.setSoulStatus(SystemConstant.SOUL_STATUS_PENDING);
        if (request.getBirthDate() != null && request.getDeathDate() != null) {
            long years = ChronoUnit.YEARS.between(request.getBirthDate(), request.getDeathDate());
            soul.setLifespan((int) years);
        }
        soul.setDescription(request.getDescription());
        soulMapper.insert(soul);
    }

    @Override
    @CacheEvict(value = "soul", key = "#id")
    @OpLog(action = "修改魂魄", targetType = "soul")
    public void update(Long id, SoulRequest request, Long userId) {
        Soul soul = getById(id);
        soul.setName(request.getName());
        soul.setGender(request.getGender());
        soul.setBirthDate(request.getBirthDate());
        soul.setDeathDate(request.getDeathDate());
        soul.setDescription(request.getDescription());
        soulMapper.updateById(soul);
    }

    @Override
    @CacheEvict(value = "soul", key = "#id")
    public void delete(Long id) {
        soulMapper.deleteById(id);
    }

    @Override
    @CacheEvict(key = "#id")
    @OpLog(action = "勾魂", targetType = "soul")
    public void updateStatus(Long id, Integer status, Long userId) {
        Soul soul = getById(id);
        soul.setSoulStatus(status);
        if (status == SystemConstant.SOUL_STATUS_ARRIVED) {
            soul.setArrivalDate(LocalDateTime.now());
        }
        soulMapper.updateById(soul);
    }
}
