-- KEYS[1]：Redis 中的 key 值，用于存储有序集合
-- ARGV[1]：最大请求次数
-- ARGV[2]：时间窗口大小（秒）
local current_time = tonumber(redis.call('time')[1])
local attempts = tonumber(redis.call('zcard', KEYS[1]))
local windows_size = tonumber(ARGV[2])
if attempts < tonumber(ARGV[1]) then
    redis.call('zadd', KEYS[1], ARGV[2], current_time)
    redis.call("expire", KEYS[1], windows_size * 2)
    return 1
end

local earliest_time = tonumber(redis.call('zrange', KEYS[1], 0, 0)[1])

if current_time - earliest_time <= windows_size then
    return 0
else
    redis.call('zremrangebyrank', KEYS[1], 0, 0)
    redis.call('zadd', KEYS[1], windows_size, current_time)
    redis.call("expire", KEYS[1], windows_size * 2)
    return 1
end