package com.example.medicalsystem.service.biz;

import com.example.medicalsystem.common.BusinessException;
import com.example.medicalsystem.dto.PrescriptionReq;
import com.example.medicalsystem.entity.Medicine;
import com.example.medicalsystem.entity.Prescription;
import com.example.medicalsystem.entity.PrescriptionItem;
import com.example.medicalsystem.entity.Registration;
import com.example.medicalsystem.mapper.MedicineMapper;
import com.example.medicalsystem.mapper.PrescriptionItemMapper;
import com.example.medicalsystem.mapper.PrescriptionMapper;
import com.example.medicalsystem.mapper.RegistrationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 开处方业务编排层：把"查挂号 → 写处方 → 逐项扣库存 → 写明细"串成一个事务。
 * 任一种药品库存不足，整个处方的所有扣减与写入全部回滚，保证事务一致性。
 */
@Service
public class PrescriptionBizService {

    @Autowired
    private RegistrationMapper registrationMapper;

    @Autowired
    private PrescriptionMapper prescriptionMapper;

    @Autowired
    private MedicineMapper medicineMapper;

    @Autowired
    private PrescriptionItemMapper prescriptionItemMapper;

    /**
     * 开具处方：事务内完成，任何一步失败整体回滚。
     *
     * @return 新增处方主键
     */
    @Transactional(rollbackFor = Exception.class)
    public Long issue(PrescriptionReq req) {
        // 1. 查挂号记录，确保处方挂在真实挂号上
        Registration reg = registrationMapper.selectById(req.getRegistrationId());
        if (reg == null) {
            throw new BusinessException(404, "挂号记录不存在，无法开具处方");
        }

        // 2. 先写处方主表（拿到 id 后才能写明细）
        Prescription p = new Prescription();
        p.setPrescNo(nextNo("PRE"));
        p.setRegistrationId(req.getRegistrationId());
        p.setDiagnosis(req.getDiagnosis());
        p.setAdvice(req.getAdvice());
        p.setStatus((byte) 1);
        prescriptionMapper.insert(p);

        // 3. 逐项校验并扣减库存：带条件 UPDATE，受影响行数为 0 即库存不足
        for (PrescriptionReq.PrescriptionItemReq item : req.getItems()) {
            Medicine med = medicineMapper.selectById(item.getMedicineId());
            if (med == null) {
                throw new BusinessException(404, "药品不存在，id=" + item.getMedicineId());
            }
            int deducted = medicineMapper.deductStock(item.getMedicineId(), item.getQuantity());
            if (deducted == 0) {
                throw new BusinessException(409, "库存不足：" + med.getName() + "，当前仅剩 "
                        + med.getStock() + " 件");
            }

            // 4. 写处方明细，单价取开方时刻的快照
            PrescriptionItem pi = new PrescriptionItem();
            pi.setPrescId(p.getId());
            pi.setMedicineId(item.getMedicineId());
            pi.setQuantity(item.getQuantity());
            pi.setUnitPrice(med.getPrice());
            prescriptionItemMapper.insert(pi);
        }

        // 5. 模拟异常开关：扣完库存后抛错，用来现场验证事务回滚（库存与处方都应恢复）
        if (Boolean.TRUE.equals(req.getSimulateFail())) {
            throw new RuntimeException("模拟异常：触发事务回滚，库存与处方应恢复原状");
        }

        return p.getId();
    }

    private String nextNo(String prefix) {
        return prefix + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
    }
}
