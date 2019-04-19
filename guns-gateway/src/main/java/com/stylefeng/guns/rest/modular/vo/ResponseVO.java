package com.stylefeng.guns.rest.modular.vo;

import lombok.Data;

@Data
public class ResponseVO<M> {
    //状态码[0 代表成功,1 代表 业务失败 ,999 代表系统错误]
    private int status;
    //错误信息
    private String msg;
    //返回数据实体
    private M data;
    //返回图片前缀
    private String imgPre;

    //分页使用
    private Integer nowPage;
    private Integer totalPages;

    private ResponseVO(){}

    //业务成功并返回数据体和图片前缀,当前页数，总页数
    public static<M> ResponseVO success(String imgPre,M m,int nowPage,int totalPages){
        ResponseVO responseVO = new ResponseVO();
        responseVO.setStatus(0);
        responseVO.setImgPre(imgPre);
        responseVO.setData(m);
        responseVO.setNowPage(nowPage);
        responseVO.setTotalPages(totalPages);

        return responseVO;
    }

    //业务成功并返回数据体和图片前缀
    public static<M> ResponseVO success(String imgPre,M m){
        ResponseVO responseVO = new ResponseVO();
        responseVO.setStatus(0);
        responseVO.setImgPre(imgPre);
        responseVO.setData(m);

        return responseVO;
    }

    //业务成功并返回数据体
    public static<M> ResponseVO success(M m){
        ResponseVO responseVO = new ResponseVO();
        responseVO.setStatus(0);
        responseVO.setData(m);

        return responseVO;
    }

    //业务成功并返回信息
    public static<M> ResponseVO success(String msg){
        ResponseVO responseVO = new ResponseVO();
        responseVO.setStatus(0);
        responseVO.setMsg(msg);
        return responseVO;
    }

    //业务失败
    public static<M> ResponseVO serviceFail(String msg){
        ResponseVO responseVO = new ResponseVO();
        responseVO.setStatus(1);
        responseVO.setMsg(msg);
        return responseVO;
    }

    //系统错误
    public static<M> ResponseVO applicationFail(String msg){
        ResponseVO responseVO = new ResponseVO();
        responseVO.setStatus(999);
        responseVO.setMsg(msg);

        return responseVO;
    }
}
