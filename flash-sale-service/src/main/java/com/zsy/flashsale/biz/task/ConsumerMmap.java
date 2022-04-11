package com.zsy.flashsale.biz.task;

import com.zsy.flashsale.biz.ExportFileDto;
import com.zsy.flashsale.dao.po.ExportFile;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @Author Allenzsy
 * @Date 2022/4/10 10:44
 * @Description:
 */
@Slf4j
public class ConsumerMmap implements Runnable {
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
                StringBuilder contents = new StringBuilder();
                for (ExportFile e : list) {
                    contents.append(e.getContent());
                    contents.append("\r\n");
                }
                buffer.put(contents.toString().getBytes());
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

    public ConsumerMmap(LinkedBlockingQueue<ExportFileDto> queue, FileChannel fileChannel, CountDownLatch latch) {
        this.queue = queue;
        this.fileChannel = fileChannel;
        this.latch = latch;
    }
}
