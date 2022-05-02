local function close_redis(red)
    if not red then
        return
    end
    --释放连接(连接池实现)
    local pool_max_idle_time = 1000 --毫秒
    local pool_size = 10 --连接池大小
    local ok, err = red:set_keepalive(pool_max_idle_time, pool_size)
    if not ok then
        ngx.say("set keepalive error : ", err)
    end
end

local redis = require "resty.redis"
local red = redis:new()
red:set_timeout(1000)
local ok, err = red:connect("127.0.0.1", 6379)
if not ok then
    ngx.say("failed to connect: ", err)
    return

end

local request_uri = string.sub(ngx.var.request_uri,2)
temp1 = 0
temp1  =  red:exists(request_uri)

local url="http://www.u8.work/404.html"
if  temp1 == 0  then
    close_redis(red)
    ngx.redirect(url);
else
    url=red:get(request_uri);
    close_redis(red)
    ngx.redirect(url);
end