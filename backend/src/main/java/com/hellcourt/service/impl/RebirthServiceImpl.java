package com.hellcourt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hellcourt.common.annotation.OpLog;
import com.hellcourt.common.constant.SystemConstant;
import com.hellcourt.common.exception.BusinessException;
import com.hellcourt.dto.request.RebirthRequest;
import com.hellcourt.entity.Rebirth;
import com.hellcourt.entity.Soul;
import com.hellcourt.mapper.RebirthMapper;
import com.hellcourt.mapper.SoulMapper;
import com.hellcourt.service.RebirthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RebirthServiceImpl implements RebirthService {
    private final RebirthMapper rebirthMapper;
    private final SoulMapper soulMapper;

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
}
