# HBase Shell语法

## 1. namespace

#### 创建命名空间 

```shell
create_namespace 'bigdata'
```

#### 查看所有的命名空间  

```shell
list_namespace
```

## 2. DDL(创建表)

创建列族(大json中的第二级目录)

```shell
create 'bigdata:student','info','msg'
```

## 3. cell

### 写入数据

```shell
# put '命名空间:表名','rowkey','列族:列名','数据'
# 重复写入会添加多个版本
put 'bigdata:student','1001','info:name','zhangsan'
```

### 获取数据

```shell
get 'bigdata:student','1001'
get 'bigdata:student','1001' , {COLUMN => 'info:name'}
```

```shell
# 通过scan进行范围扫描rowkey，startrow和stoprow来决定范围
scan 'bigdata:student',{STARTROW => '1001',STOPROW => '1002'}
```

### 删除数据

```shell
# 删除最新的版本
delete 'bigdata:student','1001','info:name'
# 删除所有版本
deleteall 'bigdata:student','1001','info:name'
```

