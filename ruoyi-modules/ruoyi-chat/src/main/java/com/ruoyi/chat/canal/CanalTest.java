package com.ruoyi.chat.canal;/*
 *@Author:cq
 *@Date:2025/6/6 17:25
 */

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.canal.protocol.Message;
import com.google.protobuf.InvalidProtocolBufferException;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lsy
 * @description ceshi
 * @date 2025/6/6
 */
public class CanalTest {


    public static void main(String[] args) {
        List<InetSocketAddress> servers = new ArrayList<>();
        servers.add(new InetSocketAddress("localhost", 11111));
        CanalConnector connector = CanalConnectors.newClusterConnector(
                servers, "example", "", ""
        );
        try {
            connector.connect();
            connector.subscribe();
            int batchSize = 100;
            while (true) {
                Message message = connector.getWithoutAck(batchSize);
                long batchId = message.getId();
                if (batchId == -1 || message.getEntries().isEmpty()) {
                    continue;
                }
                for (CanalEntry.Entry entry : message.getEntries()) {
                    if (entry.getEntryType() == CanalEntry.EntryType.TRANSACTIONBEGIN
                        || entry.getEntryType() == CanalEntry.EntryType.TRANSACTIONEND) {
                        System.out.println("跳过事务头尾");
                        continue;
                    }
                    CanalEntry.RowChange rowChange = null;
                    try {
                        rowChange = CanalEntry.RowChange.parseFrom(entry.getStoreValue());
                    } catch (InvalidProtocolBufferException e) {
                        throw new RuntimeException(e);
                    }
                    CanalEntry.EventType eventType = rowChange.getEventType();
                    System.out.println("事件类型：" + eventType);
                    for (CanalEntry.RowData rowData: rowChange.getRowDatasList()) {
                        if (eventType == CanalEntry.EventType.DELETE) {
                            printColumn(rowData.getBeforeColumnsList(), "删除数据");
                        } else if (eventType == CanalEntry.EventType.UPDATE) {
                            printColumn(rowData.getBeforeColumnsList(), "更新前的数据");
                            printColumn(rowData.getAfterColumnsList(), "更新后的数据");
                        } else if(eventType == CanalEntry.EventType.INSERT) {
                            printColumn(rowData.getAfterColumnsList(), "新增数据");
                        }
                    }
                }
                connector.ack(batchId);
            }
        } finally {
            connector.disconnect();
        }
    }

    private static void printColumn (List<CanalEntry.Column> columns, String prefix) {
        StringBuilder sb = new StringBuilder(prefix).append(": ");
        for (CanalEntry.Column column: columns) {
            sb.append(column.getName()).append("=").append(column.getValue()).append(",");
        }
        System.out.println(sb.substring(0, sb.length() - 1));
    }

}
