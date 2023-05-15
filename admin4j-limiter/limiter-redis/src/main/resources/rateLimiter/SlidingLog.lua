local limit = tonumber(ARGV[1])         -- 在时间窗口内允许的最大请求数
local window_size = tonumber(ARGV[2])   -- 时间窗口大小，单位秒

local current_time = tonumber(redis.call('TIME')[1])
local window_start = current_time - window_size

-- 从列表中删除过期的元素
local count = redis.call('LLEN', KEYS[1])
local start = 0;
while count > 0 do
    local index = redis.call('LINDEX', KEYS[1], start)
    if not index then
        break
    end
    if tonumber(index) <= window_start then
        start = start + 1
    else
        break
    end
end

if start > 0 then
    count = count - start
    redis.call('LTRIM', KEYS[1], start, -1)
end

if count >= limit then
    return 0
else
    redis.call('RPUSH', KEYS[1], current_time)
    redis.call("expire", KEYS[1], tonumber(window_size) * 2)
    return 1
end