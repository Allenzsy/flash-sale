package com.zsy.flashsale.biz.service.impl;

import com.zsy.flashsale.biz.ExportFileDto;
import com.zsy.flashsale.biz.service.OrderService;
import com.zsy.flashsale.biz.task.*;
import com.zsy.flashsale.dao.mapper.OrderMapper;
import com.zsy.flashsale.dao.mapper.TradeLogMapper;
import com.zsy.flashsale.dao.po.ExportFile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.*;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel.*;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.List;
import java.util.concurrent.*;

/**
 * @Author Allenzsy
 * @Date 2022/3/30 0:00
 * @Description:
 */
@Service
@Slf4j
public class FileService {

    @Resource
    OrderMapper orderMapper;

    @Resource
    TradeLogMapper tradeLogMapper;

    private static ExecutorService pool = Executors.newFixedThreadPool(2);
    private static ExecutorService pool2 = Executors.newFixedThreadPool(2);
    private static ExecutorService pool3 = Executors.newFixedThreadPool(2);
    private static ExecutorService pool4 = Executors.newFixedThreadPool(2);
    private static ExecutorService pool5 = Executors.newFixedThreadPool(2);

    private LinkedBlockingQueue<ExportFileDto> queue = new LinkedBlockingQueue<>(2);
    private LinkedBlockingQueue<ExportFileDto> queue2 = new LinkedBlockingQueue<>(2);
    private LinkedBlockingQueue<ExportFileDto> queue3 = new LinkedBlockingQueue<>(2);
    private LinkedBlockingQueue<ExportFileDto> queue4 = new LinkedBlockingQueue<>(2);
    /**
     * 当前应写入的启示位置
     */
    private int index;

    public void export() {
        CountDownLatch latch = new CountDownLatch(2);
        FileOutputStream fos = null;
        FileChannel channel = null;
        RandomAccessFile threadWriteFile = null;
        try {

            File orderFile = new File("E:/java/test/orderFile.bin");
            threadWriteFile = new RandomAccessFile(orderFile, "rwd");
            channel = threadWriteFile.getChannel();
            queue.clear();

            for (int i = 0; i < 2; i++) {
                pool.execute(new Consumer(queue, channel, latch));
            }

            // new Thread(new Producer(queue, orderMapper)).start();
            new Thread(new ProducerSingle(queue, tradeLogMapper, 1000, 997L)).start();

            long stime = System.currentTimeMillis();
            latch.await();
            long etime = System.currentTimeMillis();
            log.info("每次直接写耗时{}毫秒", etime-stime);
            System.gc();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            try {
                if (channel != null) {
                    channel.close();
                    channel = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public void export2() {
        CountDownLatch latch = new CountDownLatch(2);
        FileOutputStream fos = null;
        FileChannel channel = null;
        RandomAccessFile threadWriteFile = null;
        try {

            File logfile = new File("E:/java/test/logfile.bin");
            threadWriteFile = new RandomAccessFile(logfile, "rwd");
            channel = threadWriteFile.getChannel();
            queue2.clear();

            for (int i = 0; i < 2; i++) {
                pool2.execute(new ConsumerMmap(queue2, channel, latch));
            }
            new Thread(new ProducerSingle(queue2, tradeLogMapper, 1000, 997L)).start();

            long stime = System.currentTimeMillis();
            latch.await();
            long etime = System.currentTimeMillis();
            log.info("先拼接再直接写耗时{}毫秒", etime-stime);
            System.gc();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            try {
                if (channel != null) {
                    channel.close();
                    channel = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void export3() {
        CountDownLatch latch = new CountDownLatch(2);
        FileOutputStream fos = null;
        FileChannel channel = null;
        RandomAccessFile threadWriteFile = null;
        try {

            File logfile = new File("E:/java/test/logfileChannel.bin");
            threadWriteFile = new RandomAccessFile(logfile, "rwd");
            channel = threadWriteFile.getChannel();
            queue3.clear();

            for (int i = 0; i < 2; i++) {
                pool3.execute(new ConsumerChannel(queue3, channel, latch));
            }

            new Thread(new ProducerSingle(queue3, tradeLogMapper, 500, 997L)).start();
            long stime = System.currentTimeMillis();
            latch.await();
            long etime = System.currentTimeMillis();
            log.info("用filechannel每次直接写入写耗时{}毫秒", etime-stime);
            System.gc();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            try {
                if (channel != null) {
                    //channel.
                    channel.close();
                    channel = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void export4() {
        CountDownLatch latch = new CountDownLatch(2);
        FileOutputStream fos = null;
        FileChannel channel = null;
        RandomAccessFile threadWriteFile = null;
        try {

            File logfile = new File("E:/java/test/logfileChannelone.bin");
            threadWriteFile = new RandomAccessFile(logfile, "rwd");
            channel = threadWriteFile.getChannel();
            queue4.clear();

            for (int i = 0; i < 2; i++) {
                pool4.execute(new ConsumerChannelone(queue4, channel, 997, latch));
            }

            new Thread(new ProducerSingle(queue4, tradeLogMapper, 1000, 997L)).start();
            long stime = System.currentTimeMillis();
            latch.await();
            long etime = System.currentTimeMillis();
            log.info("用filechannel用bytebuffer每次缓存写入耗时{}毫秒", etime-stime);
            System.gc();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            try {
                if (channel != null) {
                    channel.close();
                    channel = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void onlySearch() {
        String primaryKey = "";
        int pageSize = 1000;
        long cursor = 0L;
        int resultNum = 0;
        List<ExportFile> exportFiles = null;
        long stime = System.currentTimeMillis();
        do {
            exportFiles = tradeLogMapper.selectPage(primaryKey, pageSize);
            if (exportFiles == null || exportFiles.isEmpty()) {
                break;
            }
            resultNum = exportFiles.size();
            //try {
                //queue.put(new ExportFileDto(cursor * resultNum * dataLength, resultNum * dataLength, exportFiles));
                //log.info("生产者线程{}查询回{}条数据",Thread.currentThread().getName(), resultNum);
                primaryKey = exportFiles.get(resultNum-1).getPrimaryKey();
                cursor++;
            //} catch (InterruptedException e) {
            //    log.info("中断: {}", e);
            //}
        } while (resultNum == pageSize );

        long etime = System.currentTimeMillis();
        log.info("查询完毕耗时{}毫秒", etime-stime);
    }


    public void onlySearchMulti() {
        // 获取分段数据
        String begin = "";
        String mid = "202204x0000005000000";
        String end = "202204x0000010000000";
        CountDownLatch latch = new CountDownLatch(2);
        long stime = System.currentTimeMillis();
        pool5.execute(new ProducerMulti(queue, tradeLogMapper, 1000, 997L, begin, mid, latch));
        pool5.execute(new ProducerMulti(queue, tradeLogMapper, 1000, 997L, mid, end, latch));
        try {
            latch.await();
            long etime = System.currentTimeMillis();
            log.info("多线程查询完毕耗时{}毫秒", etime-stime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }



}
