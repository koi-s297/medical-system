package com.example.medicalsystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import com.example.medicalsystem.entity.Medicine;

/**
 * 药品表 Mapper 接口
 *
 * @author koi
 * @since 2026-11-19
 */
public interface MedicineMapper extends BaseMapper<Medicine> {

    /**
     * 带条件扣库存：只有库存 >= 购买数量才扣减成功。
     * 返回受影响行数，0 表示库存不足。
     */
    int deductStock(@Param("id") Long id, @Param("quantity") int quantity);
}
