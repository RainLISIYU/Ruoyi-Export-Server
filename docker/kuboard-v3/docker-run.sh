sudo docker run -d \
  --restart=unless-stopped \
  --name=kuboard \
  -p 8001:80/tcp \
  -p 10081:10081/tcp \
  -e KUBOARD_ENDPOINT="http://127.0.0.1:80" \
  -e KUBOARD_AGENT_SERVER_TCP_PORT="10081" \
  -v ./root/kuboard-data:/data \
  eipwork/kuboard:v3
  # 也可以使用镜像 swr.cn-east-2.myhuaweicloud.com/kuboard/kuboard:v3 ，可以更快地完成镜像下载。
  # 请不要使用 127.0.0.1 或者 localhost 作为内网 IP \
  # Kuboard 不需要和 K8S 在同一个网段，Kuboard Agent 甚至可以通过代理访问 Kuboard Server \