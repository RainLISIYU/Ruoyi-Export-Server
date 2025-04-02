package com.ruoyi.chat;/*
 *@Author:cq
 *@Date:2024/8/7 11:16
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;

/**
 * @author lsy
 * @description
 * @date 2024/8/7
 */
public class test {

    public static void main(String[] args) throws FileNotFoundException {
        String filePath = "D:\\ruoy\\ruoyi-modules\\ruoyi-chat\\src\\main\\java\\com\\ruoyi\\chat\\a.txt";
        try (FileInputStream inputStream = new FileInputStream(filePath);
             FileChannel fileChannel = inputStream.getChannel()) {

            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            while (fileChannel.read(byteBuffer) != -1) {
                byteBuffer.flip();
                byte[] bytes = new byte[byteBuffer.limit()];
                byteBuffer.get(bytes);
                System.out.println(new String(bytes));
                byteBuffer.clear();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        int[] arr = {3, 2, 1, 4, 22, 8, 5, 2, 9, 12, 14, 12, 7};
        heapSort(arr);

    }

    /**
     * 从父节点开始构建大顶堆
     *
     * @param arr 数组
     * @param parent 父节点索引
     * @param len 当前数组长度
     */
    public static void heapAdjust(int[] arr, int parent, int len) {
        // 记录父节点
        int parentVal = arr[parent];
        // 左孩子索引
        int child = parent * 2 + 1;
        // 构建堆
        while (child < len) {
            // 右子节点存在且大于左子节点，索引指向右子节点
            if (child + 1 < len && arr[child] < arr[child + 1]) {
                ++child;
            }
            // 父节点大于最大子节点比较
            if (parentVal > arr[child]) {
                // 父节点大则结束循环
                break;
            }
            // 父节点赋值为子节点值
            arr[parent] = arr[child];
            // 以最大子节点为父节点进行遍历
            parent = child;
            child = child * 2 + 1;
        }
        // 遍历结束后设置父节点值
        arr[parent] = parentVal;
    }

    /**
     * 构建大顶堆
     *
     * @param arr 原始数组
     */
    public static void heapSort(int[] arr) {
        // 构建初始堆
        for (int i = arr.length / 2 - 1; i >= 0; i--) {
            heapAdjust(arr, i, arr.length);
        }
        System.out.println("初始化大顶堆：" + Arrays.toString(arr));
        // 排序
        for (int i = arr.length - 1; i > 0; i--) {
            // 交换堆顶和当前索引位置
            arr[i] ^= arr[0];
            arr[0] ^= arr[i];
            arr[i] ^= arr[0];
            // 堆调整
            heapAdjust(arr, 0, i);
        }
        System.out.println("排序后数组：" + Arrays.toString(arr));
    }

}
