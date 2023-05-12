-- KEYS[1]：Redis 中的 key 值，用于存储请求时间戳与请求数量的映射
-- ARGV[1]：最大请求次数
-- ARGV[2]：窗口大小（秒）
local bucket_key = KEYS[1] .. ':' .. math.floor(tonumber(ARGV[2]) * math.floor(redis.call("TIME")[1] / tonumber(ARGV[2])))
local num_requests = tonumber(redis.call("get", bucket_key) or "0")
if num_requests >= tonumber(ARGV[1]) then
    return 0
end
redis.call("incr", bucket_key)
redis.call("expire", bucket_key, tonumber(ARGV[2]) + 1)
return 1