import request from "@/utils/request";

export function addOrUpdateVote(data) {
  return request({
    url: "/state/vote/option/addOrUpdate",
    method: "post",
    data: data
  });
}

export function listVote(query) {
  return request({
    url: "/state/vote/option/list",
    method: "get",
    params: query
  });
}
