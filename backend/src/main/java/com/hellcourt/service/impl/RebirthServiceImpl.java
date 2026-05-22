package com.hellcourt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hellcourt.common.annotation.OpLog;
import com.hellcourt.common.constant.SystemConstant;
import com.hellcourt.common.exception.BusinessException;
import com.hellcourt.dto.export.RebirthExportDTO;
import com.hellcourt.dto.request.RebirthRequest;
import com.hellcourt.entity.Rebirth;
import com.hellcourt.entity.Soul;
import com.hellcourt.entity.User;
import com.hellcourt.mapper.RebirthMapper;
import com.hellcourt.mapper.SoulMapper;
import com.hellcourt.mapper.UserMapper;
import com.hellcourt.service.RebirthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RebirthServiceImpl implements RebirthService {
    private final RebirthMapper rebirthMapper;
    private final SoulMapper soulMapper;
    private final UserMapper userMapper;

    private static final Map<Integer, String> PATH_NAMES = Map.of(
            1, "天道", 2, "阿修罗道", 3, "人道",
            4, "畜生道", 5, "饿鬼道", 6, "地狱道"
    );
    private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public IPage<Rebirth> page(int page, int size) {
        return rebirthMapper.selectPage(new Page<>(page, size),
                new LambdaQueryWrapper<Rebirth>().orderByDesc(Rebirth::getCreatedAt));
    }

    @Override
    public List<Rebirth> listAll() {
        return rebirthMapper.selectList(
                new LambdaQueryWrapper<Rebirth>().orderByDesc(Rebirth::getCreatedAt));
    }

    @Override
    @OpLog(action = "分配轮回", targetType = "rebirth")
    @Transactional
    public void create(RebirthRequest request, Long operatorId) {
        Soul soul = soulMapper.selectById(request.getSoulId());
        if (soul == null) throw new BusinessException("魂魄不存在");
        if (soul.getSoulStatus() < SystemConstant.SOUL_STATUS_JUDGED) {
            throw new BusinessException("魂魄尚未完成判决");
        }

        Rebirth rebirth = new Rebirth();
        rebirth.setSoulId(request.getSoulId());
        rebirth.setPath(request.getPath());
        rebirth.setRebirthDate(request.getRebirthDate());
        rebirth.setDestination(request.getDestination());
        rebirth.setOperatorId(operatorId);
        rebirthMapper.insert(rebirth);

        soul.setSoulStatus(SystemConstant.SOUL_STATUS_REBIRTH);
        soulMapper.updateById(soul);
    }

    @Override
    public List<RebirthExportDTO> exportRebirths() {
        List<Rebirth> records = listAll();

        Set<Long> soulIds = records.stream().map(Rebirth::getSoulId).filter(Objects::nonNull).collect(Collectors.toSet());
        Set<Long> userIds = records.stream().map(Rebirth::getOperatorId).filter(Objects::nonNull).collect(Collectors.toSet());

        Map<Long, Soul> soulMap = soulIds.isEmpty() ? Collections.emptyMap() :
                soulMapper.selectBatchIds(soulIds).stream().collect(Collectors.toMap(Soul::getId, s -> s));
        Map<Long, User> userMap = userIds.isEmpty() ? Collections.emptyMap() :
                userMapper.selectBatchIds(userIds).stream().collect(Collectors.toMap(User::getId, u -> u));

        List<RebirthExportDTO> exportList = new ArrayList<>();
        for (Rebirth r : records) {
            RebirthExportDTO dto = new RebirthExportDTO();
            dto.setId(r.getId());
            dto.setSoulId(r.getSoulId());
            dto.setPathName(PATH_NAMES.getOrDefault(r.getPath(), "未知"));
            dto.setDestination(r.getDestination());
            dto.setRebirthDate(r.getRebirthDate() != null ? r.getRebirthDate().format(DTF) : "");
            dto.setCreatedAt(r.getCreatedAt() != null ? r.getCreatedAt().format(DTF) : "");

            Soul soul = soulMap.get(r.getSoulId());
            dto.setSoulName(soul != null ? soul.getName() : "未知");

            User operator = userMap.get(r.getOperatorId());
            dto.setOperatorName(operator != null ? operator.getRealName() : "未知");

            exportList.add(dto);
        }
        return exportList;
    }
}
