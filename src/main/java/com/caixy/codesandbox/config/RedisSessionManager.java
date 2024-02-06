package com.caixy.codesandbox.config;

import com.caixy.codesandbox.config.properties.RedissonProperties;
import com.caixy.codesandbox.expection.CodeSandBoxException;
import com.caixy.codesandbox.models.enums.CodeSandBoxCodeEnum;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * RedisSession会话管理，用来配置分布式redis配置，根据要求客户端类型加载对应的客户端
 *
 * @name: com.caixy.project.manager.RedisSessionManager
 * @author: CAIXYPROMISE
 * @since: 2024-01-07 22:01
 **/
@Service
@EnableConfigurationProperties(RedissonProperties.class)
public class RedisSessionManager
{
    @Resource
    private RedissonProperties redissonProperties;

    public RedissonClient getRedissonClient(String clientName)
    {
        RedissonProperties.RedisInstanceProperties properties = redissonProperties.getInstances().get(clientName);
        if (properties == null)
        {
            throw new CodeSandBoxException(CodeSandBoxCodeEnum.SYSTEM_ERROR, "Redisson配置不存在");
        }
        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://" + properties.getHost() + ":" + properties.getPort())
                .setDatabase(properties.getDatabase())
                .setPassword(properties.getPassword() == null ? null : properties.getPassword());
        return Redisson.create(config);
    }
}
