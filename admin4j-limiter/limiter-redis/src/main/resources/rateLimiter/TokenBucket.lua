-- KEYS[1]：Redis 中的 key 值，用于存储令牌桶
-- ARGV[1]： 令牌生成速率（个/秒）
-- ARGV[2]：令牌桶容量
-- requested: 请求令牌个数，默认是1个
local tokens_key = KEYS[1]
local timestamp_key = tokens_key .. ":timestamp"

local rate = tonumber(ARGV[1])
local capacity = tonumber(ARGV[2])
local now = tonumber(redis.call('time')[1])
local requested = 1

local fill_time = capacity / rate
local ttl = math.floor(fill_time * 2)

local last_tokens_and_refreshed = redis.call('mget', tokens_key, timestamp_key)

local last_tokens = tonumber(last_tokens_and_refreshed[1] or capacity)
local last_refreshed = tonumber(last_tokens_and_refreshed[2] or 0)

local delta = math.max(0, now - last_refreshed)
local filled_tokens = math.min(capacity, last_tokens + (delta * rate))
local allowed = filled_tokens >= requested
local new_tokens = filled_tokens
local allowed_num = 0
if allowed then
    new_tokens = filled_tokens - requested
    allowed_num = 1
end

redis.call("setex", tokens_key, ttl, new_tokens)
redis.call("setex", timestamp_key, ttl, now)

return allowed_num