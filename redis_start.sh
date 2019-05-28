if [[ -z $(docker ps | grep 6379) ]]; then
   docker run -p6379:6379 -d redis redis-server --appendonly yes
fi