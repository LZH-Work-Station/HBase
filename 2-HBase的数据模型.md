# HBase的数据模型

## 1. 数据模型

### Name Space（数据模型）

类似于关系数据库的database概念

### Table（表）

类似于关系数据库的表，但是写入的数据可以动态，按需求指定。没有固定的字段

### Row

HBase 表中的每行数据都由一个 RowKey 和多个 Column（列）组成，数据是按照 RowKey 的字典顺序存储的，并且查询数据时只能根据 RowKey 进行检索，**所以 RowKey 的设计十分重要**。

### Column  

HBase 中的每个列都由 Column Family(列族)和 Column Qualifier（列限定符）进行限 定，例如 info：name，info：age。建表时，只需指明列族，而列限定符无需预先定义。 

### Time Stamp  

用于标识数据的不同版本（version），每条数据写入时，系统会自动为其加上该字段， 其值为写入 HBase 的时间。 

### Cell 

 由{rowkey, column Family：column Qualifier, timestamp} 唯一确定的单元。cell 中的数 据全部是字节码形式存贮
