import request from '@/utils/request'

// 查询测试列表
export function listTest(query) {
  return request({
    url: '/business/test/list',
    method: 'get',
    params: query
  })
}

// 查询测试详细
export function getTest(id) {
  return request({
    url: '/business/test/' + id,
    method: 'get'
  })
}

// 新增测试
export function addTest(data) {
  return request({
    url: '/business/test',
    method: 'post',
    data: data
  })
}

// 修改测试
export function updateTest(data) {
  return request({
    url: '/business/test',
    method: 'put',
    data: data
  })
}

// 删除测试
export function delTest(id) {
  return request({
    url: '/business/test/' + id,
    method: 'delete'
  })
}

// 发送消息
export function sendMsg(msg) {
  return request({
    url: '/business/direct/producer/sendDirectMessage?msg=' + msg,
    method: 'get'
  })
}

// 发送topic消息1
export function sendTopicMsgFirst(msg) {
  return request({
    url: '/business/direct/producer/sendTopicMessageFirst?msg=' + msg,
    method: 'get'
  })
}

// 发送topic消息2
export function sendTopicMsgSecond(msg) {
  return request({
    url: '/business/direct/producer/sendTopicMessageSecond?msg=' + msg,
    method: 'get'
  })
}
