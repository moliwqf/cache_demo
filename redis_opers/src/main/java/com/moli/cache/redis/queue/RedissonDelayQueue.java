package com.moli.cache.redis.queue;

import com.moli.cache.redis.constant.RedisDelayQueueEnum;
import com.moli.cache.redis.handler.RedisDelayQueueHandler;
import com.moli.cache.redis.utils.RedisDelayQueueUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author moli
 * @time 2024-03-15 15:22:24
 * @description redisson实现延迟队列
 */
@Slf4j
@Component
public class RedissonDelayQueue implements CommandLineRunner {

    @Autowired
    private RedisDelayQueueUtil redisDelayQueueUtil;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private ThreadPoolTaskExecutor ptask;

    @Resource(name = "customThreadPool")
    private TaskExecutor taskExecutor;

    @Override
    public void run(String... args) throws Exception {
        ptask.execute(() -> {
            while (true) {
                try {
                    RedisDelayQueueEnum[] queueEnums = RedisDelayQueueEnum.values();
                    for (RedisDelayQueueEnum queueEnum : queueEnums) {
                        String orderId = redisDelayQueueUtil.getDelayQueue(queueEnum.getCode());
                        if (orderId != null) {
                            RedisDelayQueueHandler<String> redisDelayQueueHandle = (RedisDelayQueueHandler<String>) applicationContext.getBean(queueEnum.getBeanId());
                            taskExecutor.execute(() -> {
                                redisDelayQueueHandle.execute(orderId);
                            });
                        }
                    }
                    TimeUnit.MILLISECONDS.sleep(500);
                } catch (InterruptedException e) {
                    log.error("(Redission延迟队列监测异常中断) {}", e.getMessage());
                }
            }
        });
        log.info("(Redission延迟队列监测启动成功)");
    }
}
