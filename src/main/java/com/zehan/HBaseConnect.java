package com.zehan;

import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.NamespaceDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;

public class HBaseConnect {
    // 设置静态属性 hbase 连接
    public static Connection connection = null;
    static {
        // 创建 hbase 的连接
        try {
            // 使用配置文件的方法
            System.out.println("创建连接");
            connection = ConnectionFactory.createConnection();
            System.out.println("创建成功");
        } catch (IOException e) {
            System.out.println("连接获取失败");
            e.printStackTrace();
        }
    }
    /**
     * 连接关闭方法,用于进程关闭时调用
     * @throws IOException
     */
    public static void closeConnection() throws IOException {
        if (connection != null) {
            connection.close();
        }
    }

    public static void main(String[] args) throws IOException {
        createNamespace("test");
    }

    /**
     * 创建命名空间
     * @param namespace 命名空间名称
     */
    public static void createNamespace(String namespace) throws IOException {
        // 1. 获取 admin
        // 此处的异常先不要抛出 等待方法写完 再统一进行处理
        // admin 的连接是轻量级的 不是线程安全的 不推荐池化或者缓存这个连接
        Admin admin = connection.getAdmin();

        // 2. 调用方法创建命名空间
        // 代码相对 shell 更加底层 所以 shell 能够实现的功能 代码一定能实现
        // 所以需要填写完整的命名空间描述
        // 2.1 创建命令空间描述建造者 => 设计师
        NamespaceDescriptor.Builder builder = NamespaceDescriptor.create(namespace);

        // 2.2 给命令空间添加需求
        builder.addConfiguration("user","atguigu");

        // 2.3 使用 builder 构造出对应的添加完参数的对象 完成创建
        // 创建命名空间出现的问题 都属于本方法自身的问题 不应该抛出

        try {
            System.out.println("创建Namespace");
            admin.createNamespace(builder.build());
        } catch (IOException e) {
            System.out.println("命令空间已经存在");
            e.printStackTrace();
        }
        // 3. 关闭 admin
        admin.close();
    }

    public static void getCells(String namespace, String tableName,
                                String rowKey, String columnFamily, String columnName) throws IOException {
        // 1. 获取 table
        Table table = connection.getTable(TableName.valueOf(namespace, tableName));
        // 2. 创建 get 对象
        Get get = new Get(Bytes.toBytes(rowKey));
        // ***如果直接调用 get 方法读取数据 此时读一整行数据***
        // 如果想读取某一列的数据 需要添加对应的参数
        get.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes(columnName));
        // 设置读取数据的版本
        get.readAllVersions();
        try {
            // 读取数据 得到 result 对象
            Result result = table.get(get);
            // 处理数据
            Cell[] cells = result.rawCells();
            // 测试方法: 直接把读取的数据打印到控制台
            // 如果是实际开发 需要再额外写方法 对应处理数据
            for (Cell cell : cells) {
                // cell 存储数据比较底层
                String value = new String(CellUtil.cloneValue(cell));
                System.out.println(value);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 关闭 table
        table.close();
    }

}

