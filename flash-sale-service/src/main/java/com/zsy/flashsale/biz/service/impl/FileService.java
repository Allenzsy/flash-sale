package com.zsy.flashsale.biz.service.impl;

import com.zsy.flashsale.biz.ExportFileDto;
import com.zsy.flashsale.biz.service.OrderService;
import com.zsy.flashsale.biz.task.Consumer;
import com.zsy.flashsale.biz.task.Producer;
import com.zsy.flashsale.dao.mapper.OrderMapper;
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
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

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

    private static ExecutorService pool = Executors.newFixedThreadPool(2);

    private LinkedBlockingQueue<ExportFileDto> queue = new LinkedBlockingQueue<>(2);
    /**
     * 当前应写入的启示位置
     */
    private int index;

    public void export() {
        FileOutputStream fos = null;
        RandomAccessFile accessFile = null;
        RandomAccessFile threadWriteFile = null;
        try {

            File orderFile = new File("E:/java/test/orderFile.bin");
            threadWriteFile = new RandomAccessFile(orderFile, "rwd");
            FileChannel channel = threadWriteFile.getChannel();

            for (int i = 0; i < 2; i++) {
                pool.execute(new Consumer(queue, channel));
            }

            new Thread(new Producer(queue, orderMapper)).start();

            List<ExportFile> lists = orderMapper.selectPage(0,5);
            System.out.println(orderMapper.count());
            File file = new File("E:/java/test/test.bin");
            File file1 = new File("E:/java/test/test1.bin");
            accessFile = new RandomAccessFile(file1, "rwd");
            System.out.println("字节数1："+lists.get(0).getContent().getBytes().length);
            System.out.println("字节数2："+lists.get(1).getContent().getBytes().length);
            System.out.println("字节数3："+lists.get(2).getContent().getBytes().length);
            System.out.println("123好");
            System.out.println("123好的字符串长度: "+"123好".length());
            System.out.println("123好的byte数组长度: "+"123好".getBytes(Charset.forName("utf8")).length);

            if (!file.exists()) {
                file.createNewFile();
            }
            if (!file1.exists()) {
                file1.createNewFile();
            }

            FileChannel fileChannel = accessFile.getChannel();
            MappedByteBuffer buffer = fileChannel.map(MapMode.READ_WRITE, 0L, 120L);
            MappedByteBuffer buffer2 = fileChannel.map(MapMode.READ_WRITE, 120L, 180L);
            buffer.put(lists.get(1).getContent().getBytes());
            buffer.put(lists.get(3).getContent().getBytes());
            //buffer.put("\r\n".getBytes());
            buffer = null;
            buffer2.put(lists.get(2).getContent().getBytes());
            //buffer2.put("\r\n".getBytes());
            buffer2 = null;
            fileChannel.close();



            for (ExportFile e: lists) {
                fos = new FileOutputStream(file, true);
                fos.write(e.getContent().getBytes());
                fos.write("\r\n".getBytes());
                log.info("导出字段为: {}, 长度为: {}", e.getContent(), e.getContent().length());
            }
            System.gc();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                    fos = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
