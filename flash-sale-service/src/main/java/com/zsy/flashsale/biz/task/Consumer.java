package com.zsy.flashsale.biz.task;


import com.zsy.flashsale.biz.ExportFileDto;
import com.zsy.flashsale.dao.po.ExportFile;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @Author Allenzsy
 * @Date 2022/4/2 0:55
 * @Description:
 */
@Slf4j
public class Consumer implements Runnable{

    LinkedBlockingQueue<ExportFileDto> queue;

    FileChannel fileChannel;

    CountDownLatch latch;

    @Override
    public void run() {
        Thread th = Thread.currentThread();
        try {
            while (true) {
                ExportFileDto fileDto = queue.take();
                if(fileDto.getResultSet() == null) {
                    log.info("数据查询完毕，消费者线程{}结束并回归线程池", th.getName());
                    queue.put(Producer.POISON_PILL);
                    break;
                }
                MappedByteBuffer buffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, fileDto.getIndex(), fileDto.getLength());
                List<ExportFile> list = fileDto.getResultSet();
                for (ExportFile e : list) {
                    buffer.put(e.getContent().getBytes());
                    buffer.put("\r\n".getBytes());
                }
                //log.info("消费者线程{}将{}条数据写入文件", th.getName(), list.size());
            }
        } catch (InterruptedException e) {
            log.error("中断: {}", e);
            th.interrupt();
        } catch (IOException e) {
            log.error("消费者线程{}异常:{}", e);
        } finally {
            latch.countDown();
        }
    }

    public Consumer(LinkedBlockingQueue<ExportFileDto> queue, FileChannel fileChannel, CountDownLatch latch) {
        this.queue = queue;
        this.fileChannel = fileChannel;
        this.latch = latch;
    }

}
