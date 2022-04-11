package com.zsy.flashsale.biz.task;

import com.zsy.flashsale.biz.ExportFileDto;
import com.zsy.flashsale.dao.mapper.OrderMapper;
import com.zsy.flashsale.dao.mapper.TradeLogMapper;
import com.zsy.flashsale.dao.po.ExportFile;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import static com.zsy.flashsale.biz.task.Producer.POISON_PILL;

/**
 * @Author Allenzsy
 * @Date 2022/4/10 10:43
 * @Description:
 */
@Slf4j
public class ProducerSingle implements Runnable {
    LinkedBlockingQueue<ExportFileDto> queue;

    TradeLogMapper tradeLogMapper;

    int pageSize;

    Long dataLength;

    @Override
    public void run() {

        String primaryKey = "";
        long cursor = 0L;
        int resultNum = 0;
        List<ExportFile> exportFiles = null;
        do {
            exportFiles = tradeLogMapper.selectPage(primaryKey, pageSize);
            if (exportFiles == null || exportFiles.isEmpty()) {
                break;
            }
            resultNum = exportFiles.size();
            try {
                queue.put(new ExportFileDto(cursor * resultNum * dataLength, resultNum * dataLength, exportFiles));
                //log.info("生产者线程{}查询回{}条数据",Thread.currentThread().getName(), resultNum);
                primaryKey = exportFiles.get(resultNum-1).getPrimaryKey();
                cursor++;
            } catch (InterruptedException e) {
                log.info("中断: {}", e);
            }
        } while (resultNum == pageSize );//&& cursor < 1);


        try {
            queue.put(POISON_PILL);
            log.info("数据查询完毕，生产者线程{}放入毒丸", Thread.currentThread().getName());
        } catch (InterruptedException e) {
            log.info("中断: {}", e);
        }

    }

    public ProducerSingle(LinkedBlockingQueue<ExportFileDto> queue, TradeLogMapper tradeLogMapper, int pageSize, Long dataLength) {
        this.queue = queue;
        this.tradeLogMapper = tradeLogMapper;
        this.pageSize = pageSize;
        this.dataLength = dataLength;
    }

}
