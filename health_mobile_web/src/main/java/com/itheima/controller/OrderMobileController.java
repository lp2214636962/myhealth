package com.itheima.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.aliyuncs.exceptions.ClientException;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisMessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.CheckItem;
import com.itheima.pojo.Order;
import com.itheima.pojo.Setmeal;
import com.itheima.service.OrderService;
import com.itheima.service.SetmealService;
import com.itheima.utils.SMSUtils;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrderMobileController {

    @Reference
    private OrderService orderService;

    @Autowired
    private JedisPool jedisPool;

    @Reference
    private SetmealService setmealService;

    @RequestMapping("/submit")
    public Result submit(@RequestBody Map<String, Object> map) {
        //获取手机号
        String telephone = (String) map.get("telephone");
        //从redis中获取验证码
        String codeRedis = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_ORDER);
        // 获取页面输入的验证码
        String validateCode = (String) map.get("validateCode");
        //验证验证码是否正确
        if (codeRedis == null || !codeRedis.equals(validateCode)) {
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }
        Result result = null;
        //调用体检预约服务
        try {
            map.put("orderType", Order.ORDERTYPE_WEIXIN);
            result = orderService.submit(map);
        } catch (Exception e) {
            e.printStackTrace();
            return result;
        }

        if (result != null && result.isFlag()) {
            //预约成功，发送短信通知，短信通知内容可以是“预约时间”，“预约人”，“预约地点”，“预约事项”等信息
            String orderDate = (String) map.get("orderDate");
            try {
                SMSUtils.sendShortMessage(SMSUtils.ORDER_NOTICE, telephone, orderDate);
            } catch (ClientException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    // 根据用户的会员id查询用户信息
    @RequestMapping("/findById")
    public Result findById(Integer id) {
        try {
            Map map = orderService.findById(id);
            return new Result(true, MessageConstant.QUERY_ORDER_SUCCESS, map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_ORDER_FAIL);
        }
    }

    //exportSetmealInfo
    @RequestMapping("/exportSetmealInfo")
    public Result exportSetmealInfo(Integer id, HttpServletRequest request, HttpServletResponse response) {
        try {
            Map map = orderService.findById(id);
            //获取套餐id
            Integer setmealId = (Integer) map.get("setmealId");

            // 根据套餐id查询套餐及关联检查组检查项信息
            Setmeal setmeal = setmealService.findById(setmealId);

            // 下载导出
            //设置头信息
            response.setContentType("application/vnd.ms-excel");
            String fileName = "exportPDF.pdf";
            //设置以附件的形式导出
            response.setHeader("content-Disposition", "attachment;filename=" + fileName);

            //生成PDF文件
            Document document = new Document();
            PdfWriter.getInstance(document, response.getOutputStream());
            document.open();

            // 设置表格字体
            BaseFont cn = BaseFont.createFont("STSongStd-Light", "UniGB-UCS2-H", false);
            Font font = new Font(cn, 10, Font.NORMAL, Color.RED); // 字体样式 颜色 大小

            //写出PDF数据
            //输出订单和套餐信息
            document.add(new Paragraph("体检人:" + (String) map.get("member"), font)); //体检人
            document.add(new Paragraph("体检套餐:" + (String) map.get("setmeal"), font)); //体检套餐
            document.add(new Paragraph("体检时间:" + (String) map.get("orderDate").toString(), font)); //体检时间
            document.add(new Paragraph("预约类型:" + (String) map.get("orderType"), font)); //预约类型

            // 项doucument中生成pdf表格   检查组检查项数据
            Table table = new Table(3); //创建3列表格
            table.setWidth(80); // 宽度
            table.setBorder(1); // 边框
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER); //水平对齐方式
            table.getDefaultCell().setVerticalAlignment(Element.ALIGN_TOP); // 垂直对齐方式
            /*设置表格属性*/
            table.setBorderColor(new Color(255, 0, 255)); //将边框的颜色设置为蓝色
            table.setPadding(5);//设置表格与字体间的间距
            //table.setSpacing(5);//设置表格上下的间距
            table.setAlignment(Element.ALIGN_CENTER);//设置字体显示居中样式
            // 写表头
            table.addCell(buileCell("项目名称", font));
            table.addCell(buileCell("项目内容", font));
            table.addCell(buileCell("项目解读", font));

            List<CheckGroup> checkGroups = setmeal.getCheckGroups();  //获取检查组数据
            if (checkGroups != null && checkGroups.size() > 0) {
                for (CheckGroup checkGroup : checkGroups) {
                    //往table中添加数据
                    table.addCell(buileCell(checkGroup.getName(), font));
                    //将所有检查项名称 拼接
                    StringBuffer stringBuffer = new StringBuffer();
                    List<CheckItem> checkItems = checkGroup.getCheckItems();
                    if (checkItems != null && checkItems.size() > 0) {
                        for (CheckItem checkItem : checkItems) {
                            stringBuffer.append(checkItem.getName() + " ");
                        }
                        // 添加检查项数据
                        table.addCell(buileCell(stringBuffer.toString(),font));
                        table.addCell(buileCell(checkGroup.getRemark(),font)); //添加解读内容
                    }
                }
            }
            // 将table表格放进document文档中中
            document.add(table);
            document.close();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_ORDER_FAIL);
        }
    }

    // 传递内推和字体样式  生成单元格   抽取方法
    private Cell buileCell(String value, Font font) throws BadElementException {
        Phrase phrase = new Paragraph(value, font);
        return new Cell(phrase);
    }
}
