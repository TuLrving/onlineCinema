package com.stylefeng.guns.rest.modular.cinema;

import com.stylefeng.guns.cinema.vo.CinemaRequestVO;
import com.stylefeng.guns.rest.modular.vo.ResponseVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cinema/")
public class CinemaController {

    /**
     *  查询影院列表-根据条件查询所有影院
     * @param cinemaRequestVO
     * @return ResponseVO
     */
    @GetMapping("getCinemas")
    public ResponseVO getCinemas(CinemaRequestVO cinemaRequestVO){
        return null;
    }

    /**
     * 获取影院列表查询条件
     * @param cinemaRequestVO
     * @return
     */
    @GetMapping("getCondition")
    public ResponseVO getCondition(CinemaRequestVO cinemaRequestVO){
        return null;
    }

    /**
     * 获取播放场次接口
     * @param cinemaId
     * @return
     */
    @RequestMapping("getFields")
    public ResponseVO getFields(@RequestParam("cinemaId") Integer cinemaId){
        return null;
    }

    /**
     * 获取场次详细信息接口
     * @param cinemaId
     * @param fieldId
     * @return
     */
    @RequestMapping("getFieldInfo")
    public ResponseVO getFieldInfo(@RequestParam("cinemaId") Integer cinemaId,@RequestParam("fieldId")Integer fieldId){
        return null;
    }

}
