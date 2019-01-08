package com.hao.m.entity.data

/**
 * Created by wangpw
 */
data class BaseResultEntity<T>(var errorCode: Int, var msg: String = "错误", var data: T)