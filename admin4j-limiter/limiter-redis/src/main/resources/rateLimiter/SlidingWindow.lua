local key = KEYS[1]          -- Redis键名
local window_size = tonumber(ARGV[2])   -- 窗口大小
local max_requests = tonumber(ARGV[1])  -- 最大请求数
local sub_window_size = tonumber(ARGV[2]) / 2  -- 子时间窗口大小，单位秒
local now = redis.call("TIME")[1]           -- 当前时间戳
local request_num = 1       -- 新增值
local bucket = math.floor(tonumber(window_size) * math.floor(now / tonumber(window_size)))
local sub_bucket = math.floor(tonumber(sub_window_size) * math.floor(now / tonumber(sub_window_size)))

redis.call("HSET", 'test-Rate', bucket, sub_bucket)

local count = 0
local hAll = redis.call("HGETALL", key)
local hKey
for k, v in pairs(hAll)
do
    if not hKey then
        hKey = v
    else
        redis.call("HSET", 'test-Rate-1', hKey, v)

        if tonumber(hKey) < bucket then
            redis.call("HDEL", key, hKey)
        else
            count = count + v
            if count >= max_requests then
                return 0
            end
        end
        hKey = nil
    end
end

-- 更新数据，并将数据添加到对应的子窗口中
redis.call("HINCRBY", key, sub_bucket, request_num)
redis.call("EXPIRE", key, window_size * 10)   -- 设置过期时间

return 1