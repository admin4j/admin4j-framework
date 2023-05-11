-- KEYS[1]：Redis 中的 key 值，用于存储漏桶
-- ARGV[1]：漏桶容量
-- ARGV[2]：时间间隔（秒）
local current_time = tonumber(redis.call('time')[1])
local num_requests = tonumber(redis.call('llen', KEYS[1]) or 0)
local limit_timestamp = current_time - tonumber(ARGV[2])
if num_requests >= tonumber(ARGV[1]) then
    local oldest_request = redis.call('lrange', KEYS[1], 0, 0)
    if tonumber(oldest_request[1]) < limit_timestamp then
        redis.call('lpop', KEYS[1])
        num_requests = num_requests - 1
    else
        return 0
    end
end
redis.call('rpush', KEYS[1], current_time)
redis.call('expire', KEYS[1], tonumber(ARGV[2]) + 1)
return 1