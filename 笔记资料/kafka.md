## 重要组件

### Partition（分区）



### Cordinator（协调分区）

1. 如何确定？
2. 如何分配？



## 配置属性

**auto_offset_reset_config**：

- earliest：可以从最早的offset处开始消费
- lastest：从最新的offset处开始消费

**partition.assignment.strategy**:

- Range：范围分区

- RoundRobin：轮询分区
- Stricky：粘滞分区