package com.samgye.orderapp.api

import com.samgye.orderapp.api.request.NoticeDetailRequest
import com.samgye.orderapp.api.response.BaseResponse
import com.samgye.orderapp.api.response.NoticeDetailResponse
import com.samgye.orderapp.api.response.NoticeInfoResponse
import com.samgye.orderapp.utils.Constants
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiBasicService {
    @POST(Constants.NOTICE_DETAIL_PATH)
    fun getNoticeDetail(noticeSeq: NoticeDetailRequest): Call<BaseResponse<NoticeDetailResponse>>
    @GET(Constants.LATEST_NOTICE_PATH)
    fun getLatestNoticeTitle(): Call<BaseResponse<NoticeInfoResponse>>
    @GET(Constants.All_NOTICE_PATH)
    fun getAllNoticeTitle(): Call<BaseResponse<List<NoticeInfoResponse>>>
}