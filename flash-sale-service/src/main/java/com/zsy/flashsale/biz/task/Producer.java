package com.zsy.flashsale.biz.task;

import com.zsy.flashsale.biz.ExportFileDto;
import com.zsy.flashsale.dao.mapper.OrderMapper;
import com.zsy.flashsale.dao.po.ExportFile;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @Author Allenzsy
 * @Date 2022/4/2 1:28
 * @Description:
 */
@Slf4j
public class Producer implements Runnable{

    public static final ExportFileDto POISON_PILL = new ExportFileDto(-1L, -1L, null);

    LinkedBlockingQueue<ExportFileDto> queue;

    OrderMapper orderMapper;

    int pageSize = 20;

    Long dataLength = 62L;

    @Override
    public void run() {
        int count = orderMapper.count();
        int times = (int) Math.ceil(count / (float) pageSize);
        try {
            for (int i = 0; i < times; i++) {
                List<ExportFile> exportFiles = orderMapper.selectPage(i * pageSize, pageSize);
                queue.put(new ExportFileDto(i * pageSize * dataLength, pageSize * dataLength, exportFiles));
                log.info("生产者线程{}查询回{}条数据",Thread.currentThread().getName(), exportFiles.size());
            }
            queue.put(POISON_PILL);
            log.info("数据查询完毕，生产者线程{}放入毒丸", Thread.currentThread().getName());
        } catch (InterruptedException e) {
            log.info("中断: {}", e);
        }

    }

    public Producer(LinkedBlockingQueue<ExportFileDto> queue, OrderMapper orderMapper) {
        this.queue = queue;
        this.orderMapper = orderMapper;
    }

}
