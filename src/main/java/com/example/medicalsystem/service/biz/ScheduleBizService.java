package com.example.medicalsystem.service.biz;

import com.example.medicalsystem.common.BusinessException;
import com.example.medicalsystem.dto.ScheduleReq;
import com.example.medicalsystem.entity.DoctorSchedule;
import com.example.medicalsystem.service.DoctorScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 排班业务编排层：创建排班时保证剩余号源与总号源一致，避免手工填错导致号源异常。
 */
@Service
public class ScheduleBizService {

    @Autowired
    private DoctorScheduleService scheduleService;

    /** 新增排班：剩余号源强制等于总号源。 */
    public Long create(ScheduleReq req) {
        DoctorSchedule s = new DoctorSchedule();
        s.setDoctorId(req.getDoctorId());
        s.setWorkDate(req.getWorkDate());
        s.setTimeSlot(req.getTimeSlot());
        s.setTotalQuota(req.getTotalQuota());
        s.setRemaining(req.getTotalQuota());
        s.setFee(req.getFee());
        s.setStatus((byte) 1);
        scheduleService.save(s);
        return s.getId();
    }

    /** 停诊：号源未用完也不可再约。 */
    public void close(Long id) {
        DoctorSchedule s = scheduleService.getById(id);
        if (s == null) {
            throw new BusinessException(404, "排班不存在");
        }
        s.setStatus((byte) 0);
        scheduleService.updateById(s);
    }
}
