-- KEYS[1]：Redis 中的 key 值，用于存储请求时间戳和分值的映射
-- ARGV[1]：最大请求次数
-- ARGV[2]：窗口大小（秒）
local current_time = redis.call("TIME")[1]
redis.call("zremrangebyscore", KEYS[1], 0, current_time - tonumber(ARGV[2]))
if tonumber(redis.call("zcard", KEYS[1])) + 1 > tonumber(ARGV[1]) then
    return 0
end
redis.call("zadd", KEYS[1], current_time, current_time)
redis.call('expire', KEYS[1], ARGV[2] * 2)
return 1