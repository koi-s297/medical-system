package com.example.medicalsystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import com.example.medicalsystem.entity.DoctorSchedule;

/**
 * 医生排班表 Mapper 接口
 *
 * @author koi
 * @since 2026-11-19
 */
public interface DoctorScheduleMapper extends BaseMapper<DoctorSchedule> {

    /**
     * 带条件扣号源：只有剩余号源大于 0 才扣减成功。
     * 返回受影响行数，0 表示号源已满——这是防超卖和"号源已满"异常的关键。
     */
    int deductQuota(@Param("id") Long id);
}
