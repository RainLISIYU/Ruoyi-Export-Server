import request from "@/utils/request";

export function listSubject(query) {
  return request({
    url: '/state/vote/subject/list',
    method: 'get',
    params: query
  });
}

export function addOrUpdateSubject(data) {
  return request({
    url: '/state/vote/subject/addOrUpdate',
    method: 'post',
    data: data
  });
}

export function removeSubject(id) {
  return request({
    url: '/state/vote/subject/remove',
    method: 'delete',
    params: {id}
  });
}
