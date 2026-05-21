package com.hellcourt.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hellcourt.dto.request.RebirthRequest;
import com.hellcourt.entity.Rebirth;

import java.util.List;

public interface RebirthService {
    IPage<Rebirth> page(int page, int size);
    List<Rebirth> listAll();
    void create(RebirthRequest request, Long operatorId);
}
